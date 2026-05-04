import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

function ArcherDashboard() {
  const PAGE_SIZE = 4;
  const navigate = useNavigate();

  const [usuario, setUsuario] = useState(null);
  const [usuarioCargado, setUsuarioCargado] = useState(false);
  const [torneos, setTorneos] = useState([]);
  const [torneosCargando, setTorneosCargando] = useState(true);
  const [torneosPage, setTorneosPage] = useState(0);
  const [torneosTotalPages, setTorneosTotalPages] = useState(0);
  const [historial, setHistorial] = useState([]);
  const [historialCargando, setHistorialCargando] = useState(true);
  const [historialPage, setHistorialPage] = useState(0);
  const [historialTotalPages, setHistorialTotalPages] = useState(0);
  const [todasLasFlechas, setTodasLasFlechas] = useState([]);
  const [flechasPorTorneo, setFlechasPorTorneo] = useState({});
  const [errorCarga, setErrorCarga] = useState("");

  const esErrorAuth = (err) => {
    const status = err?.response?.status;
    return status === 401 || status === 403;
  };

  useEffect(() => {
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");
    if (usuarioGuardado) {
      try {
        setUsuario(JSON.parse(usuarioGuardado));
      } catch (err) {
        localStorage.removeItem("usuarioLogueado");
        navigate("/login");
      }
    } else {
      navigate("/login");
    }
    setUsuarioCargado(true);
  }, [navigate]);

  useEffect(() => {
    if (!usuario) return;

    const fetchTorneos = async () => {
      const fetchTorneosLegacy = async () => {
        const respAll = await api.get("/torneos");
        const todos = Array.isArray(respAll.data) ? respAll.data : [];
        const estadosDisponibles = new Set([
          "NOT_STARTED",
          "CREADO",
          "OPEN",
          "PENDING",
        ]);

        const candidatos = todos.filter((t) =>
          estadosDisponibles.has((t?.estadoTorneo || "").toUpperCase()),
        );

        const filtrados = [];
        for (const torneo of candidatos) {
          try {
            const inscritosResp = await api.get(
              `/participaciones/torneo/${torneo.idTorneo}`,
            );
            const inscritos = Array.isArray(inscritosResp.data)
              ? inscritosResp.data
              : [];
            const yaInscrito = inscritos.some(
              (i) => i.idUsuario === usuario.idUsuario,
            );
            if (!yaInscrito) filtrados.push(torneo);
          } catch (errorInscritos) {
            filtrados.push(torneo);
          }
        }

        const totalPages = Math.ceil(filtrados.length / PAGE_SIZE);
        const inicio = torneosPage * PAGE_SIZE;
        const pagina = filtrados.slice(inicio, inicio + PAGE_SIZE);
        setTorneos(pagina);
        setTorneosTotalPages(totalPages);
      };

      try {
        setTorneosCargando(true);
        setErrorCarga("");
        const resp = await api.get("/torneos/disponibles", {
          params: {
            idUsuario: usuario.idUsuario,
            page: torneosPage,
            size: PAGE_SIZE,
          },
        });

        const payload = resp.data || {};
        let disponibles = Array.isArray(payload.content) ? payload.content : [];
        let totalPages = Number(payload.totalPages || 0);

        // Fallback: algunos seeds usan estado CREADO y no NOT_STARTED.
        if (disponibles.length === 0 && torneosPage === 0) {
          await fetchTorneosLegacy();
          return;
        }

        setTorneos(disponibles);
        setTorneosTotalPages(totalPages);
      } catch (err) {
        if (esErrorAuth(err)) {
          try {
            await fetchTorneosLegacy();
            return;
          } catch (legacyErr) {
            setErrorCarga(
              "Tu sesión no está autorizada para consultar torneos. Vuelve a iniciar sesión.",
            );
            setTorneos([]);
            setTorneosTotalPages(0);
            return;
          }
        }
        if (err?.response?.status === 400 && torneosPage > 0) {
          setTorneosPage(0);
          return;
        }
        console.error("Error traer torneos:", err);
        setErrorCarga(
          "No se pudieron cargar los torneos. Revisa tu sesión o backend.",
        );
        setTorneos([]);
        setTorneosTotalPages(0);
      } finally {
        setTorneosCargando(false);
      }
    };

    fetchTorneos();
  }, [usuario, torneosPage]);

  useEffect(() => {
    if (!usuario) return;

    const fetchHistorialReal = async () => {
      const fetchHistorialLegacy = async () => {
        const allTorneos = await api.get("/torneos");
        const torneosList = Array.isArray(allTorneos.data)
          ? allTorneos.data
          : [];
        const participacionesDelUsuario = [];
        const flechasTodas = [];
        const flechasPorTorneoLocal = {};

        for (const torneo of torneosList) {
          try {
            const inscritos = await api.get(
              `/participaciones/torneo/${torneo.idTorneo}`,
            );
            const inscritosDelTorneo = Array.isArray(inscritos.data)
              ? inscritos.data
              : [];
            const estaInscrito = inscritosDelTorneo.some(
              (inscrito) => inscrito.idUsuario === usuario.idUsuario,
            );

            if (!estaInscrito) continue;

            let puntajeFinal = 0;
            let posicionFinal = "-";
            let flechasDelTorneo = [];

            try {
              const flechasResp = await api.get(
                `/torneos/${torneo.idTorneo}/arqueros/${usuario.idUsuario}/flechas`,
              );
              flechasDelTorneo = Array.isArray(flechasResp.data)
                ? flechasResp.data
                : [];
              puntajeFinal = flechasDelTorneo.reduce(
                (sum, flecha) => sum + (flecha.puntaje || 0),
                0,
              );
              flechasTodas.push(...flechasDelTorneo);
              flechasPorTorneoLocal[torneo.idTorneo] = flechasDelTorneo;
            } catch (errorFlechas) {
              flechasPorTorneoLocal[torneo.idTorneo] = [];
            }

            const rankingDelTorneo = [];
            for (const inscrito of inscritosDelTorneo) {
              try {
                const flechasDelInscrito = await api.get(
                  `/torneos/${torneo.idTorneo}/arqueros/${inscrito.idUsuario}/flechas`,
                );
                const puntajeDelInscrito = (
                  flechasDelInscrito.data || []
                ).reduce((sum, flecha) => sum + (flecha.puntaje || 0), 0);
                rankingDelTorneo.push({
                  idUsuario: inscrito.idUsuario,
                  puntaje: puntajeDelInscrito,
                });
              } catch (errorRanking) {
                rankingDelTorneo.push({
                  idUsuario: inscrito.idUsuario,
                  puntaje: 0,
                });
              }
            }

            rankingDelTorneo.sort((a, b) => b.puntaje - a.puntaje);
            const posicionUsuario =
              rankingDelTorneo.findIndex(
                (item) => item.idUsuario === usuario.idUsuario,
              ) + 1;
            posicionFinal = posicionUsuario > 0 ? posicionUsuario : "-";

            participacionesDelUsuario.push({
              idTorneo: torneo.idTorneo,
              nombreTorneo: torneo.nombreTorneo,
              puntajeFinal,
              posicionFinal,
              fechaInicio: torneo.fechaInicio,
              estadoTorneo: torneo.estadoTorneo,
              rondas: [],
            });
          } catch (errorInscritos) {
            // Ignorar torneos que no se puedan consultar
          }
        }

        const totalPages = Math.ceil(
          participacionesDelUsuario.length / PAGE_SIZE,
        );
        const inicio = historialPage * PAGE_SIZE;
        const pagina = participacionesDelUsuario.slice(
          inicio,
          inicio + PAGE_SIZE,
        );

        setHistorial(pagina);
        setHistorialTotalPages(totalPages);
        setTodasLasFlechas(flechasTodas);
        setFlechasPorTorneo(flechasPorTorneoLocal);
      };

      try {
        setHistorialCargando(true);
        setErrorCarga("");
        const resp = await api.get(`/arqueros/${usuario.idUsuario}/historial`, {
          params: {
            page: historialPage,
            size: PAGE_SIZE,
          },
        });

        const payload = resp.data || {};
        const torneosHistorial = Array.isArray(payload.torneos)
          ? payload.torneos
          : [];

        const flechasPorTorneoLocal = {};
        const flechas = [];

        torneosHistorial.forEach((torneo) => {
          const rondas = Array.isArray(torneo.rondas) ? torneo.rondas : [];
          const flechasDelTorneo = [];

          rondas.forEach((ronda) => {
            const numeroRonda = ronda?.numeroRonda;
            const flechasRonda = Array.isArray(ronda?.flechas)
              ? ronda.flechas
              : [];

            flechasRonda.forEach((flecha) => {
              const flechaConRonda = {
                ...flecha,
                numeroRonda,
              };
              flechasDelTorneo.push(flechaConRonda);
              flechas.push(flechaConRonda);
            });
          });

          flechasPorTorneoLocal[torneo.idTorneo] = flechasDelTorneo;
        });

        setHistorial(torneosHistorial);
        setHistorialTotalPages(Number(payload.totalPages || 0));
        setTodasLasFlechas(flechas);
        setFlechasPorTorneo(flechasPorTorneoLocal);
      } catch (err) {
        if (esErrorAuth(err)) {
          try {
            await fetchHistorialLegacy();
            return;
          } catch (legacyErr) {
            setErrorCarga(
              "Tu sesión no está autorizada para consultar historial. Vuelve a iniciar sesión.",
            );
            setHistorial([]);
            setHistorialTotalPages(0);
            setTodasLasFlechas([]);
            setFlechasPorTorneo({});
            return;
          }
        }
        console.error("Error traer historial:", err);
        setErrorCarga(
          "No se pudo cargar el historial. Revisa tu sesión o backend.",
        );
        setHistorial([]);
        setHistorialTotalPages(0);
        setTodasLasFlechas([]);
        setFlechasPorTorneo({});
      } finally {
        setHistorialCargando(false);
      }
    };

    fetchHistorialReal();
  }, [usuario, historialPage]);

  const calcularEstadisticas = () => {
    const totalFlechas = todasLasFlechas.length;
    const flechasAcertadas = todasLasFlechas.filter(
      (f) => (f.puntaje || 0) > 0,
    ).length;
    const porcentajeAcierto =
      totalFlechas > 0
        ? Math.round((flechasAcertadas / totalFlechas) * 100)
        : 0;
    const totalPuntos = todasLasFlechas.reduce(
      (sum, f) => sum + (f.puntaje || 0),
      0,
    );
    const promedioPuntos =
      totalFlechas > 0 ? (totalPuntos / totalFlechas).toFixed(1) : 0;

    return {
      torneosTotales: historial.length,
      totalFlechas,
      flechasAcertadas,
      porcentajeAcierto,
      promedioPuntos,
      totalPuntos,
    };
  };

  const stats = calcularEstadisticas();

  const torneosArray = Array.isArray(torneos) ? torneos : [];
  const historialArray = Array.isArray(historial) ? historial : [];
  const puntosMaximos = Math.max(
    ...historialArray.map((item) => item.puntajeFinal || 0),
    1,
  );
  const porcentajeAcierto =
    stats.totalFlechas > 0
      ? Math.round((stats.flechasAcertadas / stats.totalFlechas) * 100)
      : 0;

  const torneosDisponibles = torneosArray;
  const torneosPaginados = torneosDisponibles;
  const historialPaginado = historialArray;

  const renderPieGrafico = () => {
    const acertadas = stats.flechasAcertadas;
    const fallidas = Math.max(stats.totalFlechas - stats.flechasAcertadas, 0);
    const total = acertadas + fallidas;

    if (total === 0) {
      return (
        <div
          className="d-flex align-items-center justify-content-center bg-light rounded-circle border mx-auto"
          style={{ width: 180, height: 180 }}
        >
          <span className="text-muted">Sin datos</span>
        </div>
      );
    }

    const aciertoPorcentaje = (acertadas / total) * 100;
    return (
      <div
        className="position-relative mx-auto"
        style={{ width: 180, height: 180 }}
      >
        <div
          className="rounded-circle w-100 h-100"
          style={{
            background: `conic-gradient(#28a745 0 ${aciertoPorcentaje}%, #dc3545 ${aciertoPorcentaje}% 100%)`,
          }}
        />
        <div
          className="position-absolute top-50 start-50 translate-middle text-center bg-white rounded-circle d-flex flex-column justify-content-center align-items-center border"
          style={{ width: 110, height: 110 }}
        >
          <strong className="fs-4">{porcentajeAcierto}%</strong>
          <small className="text-muted">acierto</small>
        </div>
      </div>
    );
  };

  if (!usuarioCargado) {
    return (
      <div className="container-fluid py-4 px-4">
        <div className="alert alert-info">Cargando usuario...</div>
      </div>
    );
  }

  return (
    <div className="container-fluid py-4 px-4">
      <h1>Mi Perfil de Arquero</h1>
      {usuario && (
        <p className="text-muted">
          {usuario.nombre} (ID: {usuario.idUsuario})
        </p>
      )}
      {errorCarga && <div className="alert alert-danger">{errorCarga}</div>}

      <section className="mb-5">
        <h5 className="mb-3">Mis Estadísticas</h5>
        <div className="row g-3 mb-4">
          <div className="col-md-3">
            <div className="card text-white bg-primary p-3 text-center">
              <h6>Torneos</h6>
              <h2>{stats.torneosTotales}</h2>
            </div>
          </div>
          <div className="col-md-3">
            <div className="card text-white bg-success p-3 text-center">
              <h6>Total Flechas</h6>
              <h2>{stats.totalFlechas}</h2>
            </div>
          </div>
          <div className="col-md-3">
            <div className="card text-white bg-info p-3 text-center">
              <h6>% Acierto</h6>
              <h2>{stats.porcentajeAcierto}%</h2>
            </div>
          </div>
          <div className="col-md-3">
            <div className="card text-white bg-warning p-3 text-center">
              <h6>Promedio Puntos</h6>
              <h2>{stats.promedioPuntos}</h2>
            </div>
          </div>
        </div>

        <div className="row g-3">
          <div className="col-md-5">
            <div className="card p-3 h-100">
              <h6 className="mb-3">Aciertos vs Fallos</h6>
              {renderPieGrafico()}
              <div className="d-flex justify-content-center gap-3 mt-3 small">
                <span>
                  <span
                    className="badge me-1"
                    style={{ backgroundColor: "#28a745" }}
                  >
                    &nbsp;
                  </span>
                  Acertadas: {stats.flechasAcertadas}
                </span>
                <span>
                  <span
                    className="badge me-1"
                    style={{ backgroundColor: "#dc3545" }}
                  >
                    &nbsp;
                  </span>
                  Fallidas:{" "}
                  {Math.max(stats.totalFlechas - stats.flechasAcertadas, 0)}
                </span>
              </div>
            </div>
          </div>

          <div className="col-md-7">
            <div className="card p-3 h-100">
              <h6 className="mb-3">Puntos por Torneo</h6>
              {historialArray.length === 0 ? (
                <div className="alert alert-light border mb-0">
                  Todavía no hay torneos en el historial para graficar.
                </div>
              ) : (
                <div className="d-flex flex-column gap-3">
                  {historialArray.map((item) => {
                    const valor = item.puntajeFinal || 0;
                    const width = `${Math.max((valor / puntosMaximos) * 100, 6)}%`;
                    return (
                      <div key={item.idTorneo}>
                        <div className="d-flex justify-content-between small mb-1">
                          <span className="fw-medium">{item.nombreTorneo}</span>
                          <span>{valor} pts</span>
                        </div>
                        <div className="progress" style={{ height: 14 }}>
                          <div
                            className="progress-bar bg-primary"
                            role="progressbar"
                            style={{ width }}
                            aria-valuenow={valor}
                            aria-valuemin="0"
                            aria-valuemax={puntosMaximos}
                          />
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          </div>
        </div>
      </section>

      <section className="mb-5">
        <h5 className="mb-3">Mi Historial</h5>
        {historialCargando ? (
          <div className="alert alert-info">Cargando...</div>
        ) : historial.length === 0 ? (
          <div className="alert alert-warning">
            No hay torneos registrados aún.
          </div>
        ) : (
          <>
            <div className="row">
              {historialPaginado.map((item) => (
                <div className="col-md-6 mb-3" key={item.idTorneo}>
                  <div
                    className="card p-3 shadow-sm"
                    style={{ cursor: "pointer" }}
                  >
                    <h6 className="mb-1">{item.nombreTorneo}</h6>
                    <p className="mb-1">
                      Puntaje: <strong>{item.puntajeFinal}</strong>
                    </p>
                    <p className="mb-0">
                      Posición: <strong>{item.posicionFinal}°</strong>
                    </p>
                    <details className="mt-2">
                      <summary className="small" style={{ cursor: "pointer" }}>
                        Ver flechas (
                        {(flechasPorTorneo[item.idTorneo] || []).length})
                      </summary>
                      <div className="mt-2 small">
                        {(() => {
                          const flechasFor =
                            flechasPorTorneo[item.idTorneo] || [];
                          if (flechasFor.length === 0)
                            return (
                              <div className="text-muted">
                                Sin flechas registradas
                              </div>
                            );
                          const agrupadas = flechasFor.reduce((acc, f) => {
                            const r = f.numeroRonda || 0;
                            if (!acc[r]) acc[r] = [];
                            acc[r].push(f);
                            return acc;
                          }, {});

                          return Object.keys(agrupadas)
                            .sort((a, b) => Number(a) - Number(b))
                            .map((r) => (
                              <div key={r} className="mb-2">
                                <strong>Ronda {r}</strong>
                                <ul className="mb-0">
                                  {agrupadas[r].map((f) => (
                                    <li key={f.idFlecha}>
                                      Flecha {f.idFlecha}: {f.puntaje}
                                    </li>
                                  ))}
                                </ul>
                              </div>
                            ));
                        })()}
                      </div>
                    </details>
                  </div>
                </div>
              ))}
            </div>

            {historialTotalPages > 1 && (
              <div className="mt-2">
                {Array.from({ length: historialTotalPages }).map((_, i) => (
                  <button
                    key={i}
                    className={`btn btn-sm me-1 ${
                      i === historialPage
                        ? "btn-primary"
                        : "btn-outline-secondary"
                    }`}
                    onClick={() => setHistorialPage(i)}
                  >
                    {i + 1}
                  </button>
                ))}
              </div>
            )}
          </>
        )}
      </section>

      <section className="mb-5">
        <h5 className="mb-3">Torneos Disponibles</h5>
        {torneosCargando ? (
          <div className="alert alert-info">Cargando torneos...</div>
        ) : torneosDisponibles.length === 0 ? (
          <div className="alert alert-warning">
            No hay torneos disponibles para inscripción.
          </div>
        ) : (
          <>
            <div className="row">
              {torneosPaginados.map((t) => (
                <div className="col-md-6 mb-3" key={t.idTorneo}>
                  <div className="card p-3">
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <h6 className="mb-0">{t.nombreTorneo}</h6>
                        <small className="text-muted">
                          {t.fechaInicio} — {t.fechaTermino}
                        </small>
                      </div>
                      <button
                        className="btn btn-sm btn-primary"
                        onClick={() => console.log("Inscribirse:", t.idTorneo)}
                      >
                        Inscribirme
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {torneosTotalPages > 1 && (
              <div className="mt-2">
                {Array.from({ length: torneosTotalPages }).map((_, i) => (
                  <button
                    key={i}
                    className={`btn btn-sm me-1 ${
                      i === torneosPage
                        ? "btn-primary"
                        : "btn-outline-secondary"
                    }`}
                    onClick={() => setTorneosPage(i)}
                  >
                    {i + 1}
                  </button>
                ))}
              </div>
            )}
          </>
        )}
      </section>

      <section className="mb-5">
        <h5 className="mb-3">Mis Flechas</h5>
        <div className="alert alert-secondary">
          Total de flechas cargadas: {todasLasFlechas.length}
        </div>
      </section>
    </div>
  );
}

export default ArcherDashboard;
