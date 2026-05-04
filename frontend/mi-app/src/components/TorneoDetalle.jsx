import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import torneoService from "../api/apiTorneos";
import participacionService from "../api/apiParticipaciones";
import api from "../api/api";

function TorneoDetalle() {
  const { idTorneo } = useParams();
  const navigate = useNavigate();

  const [torneo, setTorneo] = useState(null);
  const [inscritos, setInscritos] = useState([]);
  const [rondas, setRondas] = useState([]);
  const [podio, setPodio] = useState([]);
  const [loading, setLoading] = useState(true);

  const [usuarioSel, setUsuarioSel] = useState("");
  const [rondaSel, setRondaSel] = useState("");
  const [flechas, setFlechas] = useState(["", ""]);
  const [guardandoPuntaje, setGuardandoPuntaje] = useState(false);
  const [notificacion, setNotificacion] = useState({ tipo: "", texto: "" });

  const mostrarMensaje = (tipo, texto) => {
    setNotificacion({ tipo, texto });
    setTimeout(() => setNotificacion({ tipo: "", texto: "" }), 4000);
  };

  useEffect(() => {
    cargarDatosGenerales();
  }, [idTorneo]);

  useEffect(() => {
    if (usuarioSel && rondaSel && torneo?.estadoTorneo === "IN_COURSE") {
      buscarFlechasExistentes();
    } else {
      setFlechas(["", ""]);
    }
  }, [usuarioSel, rondaSel, torneo]);

  const cargarDatosGenerales = async () => {
    try {
      setLoading(true);
      const todos = await torneoService.obtenerTodos();
      const t = todos.find((x) => x.idTorneo === parseInt(idTorneo));
      if (!t) return navigate("/admin");

      setTorneo(t);
      const ins =
        await participacionService.obtenerInscritosPorTorneo(idTorneo);
      setInscritos(ins || []);

      const resRondas = await api.get(`/rondas/torneo/${idTorneo}`);
      setRondas(resRondas.data || []);

      // Si el torneo ya terminó, cargamos el podio automáticamente
      if (t.estadoTorneo === "COMPLETED") {
        const resPodio = await torneoService.obtenerPodio(idTorneo);
        setPodio(resPodio || []);
      }
    } catch (e) {
      mostrarMensaje("error", "Error cargando detalles.");
    } finally {
      setLoading(false);
    }
  };

  const buscarFlechasExistentes = async () => {
    try {
      const res = await api.get(
        `/torneos/${idTorneo}/rondas/${rondaSel}/arqueros/${usuarioSel}/flechas`,
      );
      setFlechas(
        res.data.length > 0
          ? res.data.map((f) => f.puntaje.toString())
          : ["", ""],
      );
    } catch (e) {
      setFlechas(["", ""]);
    }
  };

  const handleIniciarTorneo = async () => {
    if (window.confirm("¿Iniciar torneo? No se aceptarán más inscritos.")) {
      try {
        await torneoService.iniciarTorneo(idTorneo);
        mostrarMensaje("success", "Torneo iniciado.");
        cargarDatosGenerales();
      } catch (err) {
        mostrarMensaje("error", "Error al iniciar el torneo.");
      }
    }
  };

  const handleGuardarPuntaje = async (e) => {
    e.preventDefault();
    const flechasInt = flechas.map((f) => parseInt(f));
    if (flechasInt.some((f) => isNaN(f)))
      return mostrarMensaje("warning", "Completa todas las flechas.");

    setGuardandoPuntaje(true);
    try {
      await torneoService.registrarPuntajes(
        idTorneo,
        usuarioSel,
        rondaSel,
        flechasInt,
      );
      mostrarMensaje("success", "Puntajes guardados.");
    } catch (error) {
      mostrarMensaje("error", "Error al guardar en BD.");
    } finally {
      setGuardandoPuntaje(false);
    }
  };

  const handleFinalizarTorneo = async () => {
    if (
      window.confirm(
        "¿Estás seguro de finalizar el torneo? Se calcularán las posiciones finales y no se podrán modificar más puntajes.",
      )
    ) {
      try {
        setLoading(true);
        // Llama al SP2 a través del servicio
        await torneoService.finalizarTorneo(idTorneo);

        mostrarMensaje(
          "success",
          "¡Torneo finalizado y posiciones calculadas con éxito!",
        );

        await cargarDatosGenerales();
      } catch (err) {
        const msg =
          err.response?.data?.message ||
          err.response?.data ||
          "Error al finalizar.";
        mostrarMensaje("error", `Error: ${msg}`);
      } finally {
        setLoading(false);
      }
    }
  };

  if (loading || !torneo)
    return <div className="container mt-4">Cargando...</div>;

  return (
    <div className="container-fluid py-2">
      {notificacion.texto && (
        <div
          className={`alert ${notificacion.tipo === "success" ? "alert-success" : "alert-danger"} position-fixed top-0 start-50 translate-middle-x mt-3 z-3 w-75 text-center shadow`}
        >
          {notificacion.texto}
        </div>
      )}

      {/* Cabecera */}
      <div
        className="card shadow-sm mb-4 border-0"
        style={{ borderLeft: "4px solid #FFD700" }}
      >
        <div className="card-body d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
          <div>
            <h3 className="fw-bold mb-1 text-dark">{torneo.nombreTorneo}</h3>
            <span
              className={`badge ${torneo.estadoTorneo === "COMPLETED" ? "bg-success" : torneo.estadoTorneo === "IN_COURSE" ? "bg-primary" : "bg-warning text-dark"}`}
            >
              {torneo.estadoTorneo}
            </span>
          </div>
          <div className="d-flex flex-wrap gap-2">
            {torneo.estadoTorneo === "NOT_STARTED" && (
              <button
                className="btn fw-bold"
                style={{ backgroundColor: "#FFD700" }}
                onClick={handleIniciarTorneo}
              >
                ▶️ Iniciar
              </button>
            )}

            {torneo.estadoTorneo === "IN_COURSE" && (
              <button
                className="btn btn-success fw-bold shadow-sm"
                onClick={handleFinalizarTorneo}
              >
                🏁 Finalizar
              </button>
            )}

            <button
              className="btn btn-outline-dark fw-bold"
              onClick={() => navigate("/admin")}
            >
              Volver
            </button>
          </div>
        </div>
      </div>

      <div className="row g-4">
        {/* Panel Participantes */}
        <div className="col-12 col-lg-5">
          <div className="card shadow-sm border-0 h-100">
            <div className="card-header text-white fw-bold bg-dark">
              👥 Inscritos ({inscritos.length})
            </div>
            <div
              className="card-body p-0"
              style={{ maxHeight: "400px", overflowY: "auto" }}
            >
              <ul className="list-group list-group-flush">
                {inscritos.length === 0 ? (
                  <li className="list-group-item text-muted text-center p-4">
                    Sin inscritos.
                  </li>
                ) : (
                  inscritos.map((ins, idx) => (
                    <li
                      key={ins.idUsuario}
                      className="list-group-item d-flex justify-content-between align-items-center p-3"
                      onClick={() =>
                        torneo.estadoTorneo === "IN_COURSE" &&
                        (setUsuarioSel(ins.idUsuario), setRondaSel(""))
                      }
                      style={{
                        cursor:
                          torneo.estadoTorneo === "IN_COURSE"
                            ? "pointer"
                            : "default",
                        backgroundColor:
                          usuarioSel === ins.idUsuario
                            ? "#FFF9D6"
                            : "transparent",
                        borderLeft:
                          usuarioSel === ins.idUsuario
                            ? "4px solid #FFD700"
                            : "4px solid transparent",
                      }}
                    >
                      <div className="text-truncate me-2">
                        <span className="badge bg-secondary me-2">
                          {idx + 1}
                        </span>
                        <strong className="text-dark">{ins.nombre}</strong>
                      </div>
                      <span className="text-muted small d-none d-sm-inline">
                        {ins.rut}
                      </span>
                    </li>
                  ))
                )}
              </ul>
            </div>
          </div>
        </div>

        {/* Panel Formulario */}
        <div className="col-12 col-lg-7">
          <div className="card shadow-sm border-0 h-100">
            <div
              className="card-header text-dark fw-bold"
              style={{ backgroundColor: "#FFD700" }}
            >
              🎯 Gestión de Flechas
            </div>
            <div className="card-body p-3 p-md-4">
              {torneo.estadoTorneo === "NOT_STARTED" ? (
                <div className="text-center p-4">
                  <h4 className="fw-bold">Torneo en Espera</h4>
                  <p className="text-muted">
                    Inicia el torneo en la cabecera para ingresar puntajes.
                  </p>
                </div>
              ) : torneo.estadoTorneo === "COMPLETED" ? (
                <div className="text-center p-4">
                  <h4 className="fw-bold text-success">Torneo Finalizado</h4>
                  <p className="text-muted">
                    Los puntajes han sido registrados y el ranking calculado.
                  </p>
                </div>
              ) : !usuarioSel ? (
                <div className="text-center p-4">
                  <h5 className="fw-bold">👈 Selecciona un participante</h5>
                  <p className="text-muted">
                    Haz clic en la lista para ingresar sus flechas.
                  </p>
                </div>
              ) : (
                <form onSubmit={handleGuardarPuntaje}>
                  <div className="alert alert-secondary text-center">
                    Arquero:{" "}
                    <strong>
                      {
                        inscritos.find((i) => i.idUsuario === usuarioSel)
                          ?.nombre
                      }
                    </strong>
                  </div>

                  <div className="mb-4">
                    <label className="form-label fw-bold">
                      Seleccionar Ronda
                    </label>
                    <select
                      className="form-select form-select-lg"
                      required
                      value={rondaSel}
                      onChange={(e) => setRondaSel(e.target.value)}
                    >
                      <option value="">-- Elige la ronda --</option>
                      {rondas.map((r) => (
                        <option key={r.idRonda} value={r.numeroRonda}>
                          Ronda N° {r.numeroRonda}
                        </option>
                      ))}
                    </select>
                  </div>

                  {rondaSel && (
                    <div className="p-3 p-md-4 bg-light border rounded text-center">
                      <label className="form-label fw-bold mb-3">
                        Puntaje (0 a 10)
                      </label>
                      <div className="d-flex flex-wrap justify-content-center gap-3">
                        {flechas.map((val, idx) => (
                          <div key={idx}>
                            <input
                              type="number"
                              className="form-control form-control-lg text-center fw-bold"
                              style={{
                                width: "90px",
                                fontSize: "1.5rem",
                                border: "2px solid #1A1A1A",
                              }}
                              value={val}
                              min="0"
                              max="10"
                              required
                              onChange={(e) => {
                                const nuevas = [...flechas];
                                nuevas[idx] = e.target.value;
                                setFlechas(nuevas);
                              }}
                            />
                            <small className="d-block mt-1 fw-bold text-muted">
                              F{idx + 1}
                            </small>
                          </div>
                        ))}
                      </div>
                      <button
                        type="submit"
                        className="btn btn-dark btn-lg w-100 mt-4 fw-bold"
                        disabled={guardandoPuntaje}
                      >
                        {guardandoPuntaje ? "Guardando..." : "💾 Guardar"}
                      </button>
                    </div>
                  )}
                </form>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Renderizado del Podio si el torneo está completado */}
      {torneo.estadoTorneo === "COMPLETED" && podio.length > 0 && (
        <div className="row mt-4">
          <div className="col-12">
            <div className="card shadow-sm border-0 bg-dark text-white p-4">
              <h4 className="text-center mb-4" style={{ color: "#FFD700" }}>
                🏆 Podio de Ganadores
              </h4>
              <div className="d-flex flex-wrap justify-content-around text-center gap-3">
                {podio.map((p, idx) => (
                  <div key={p.idUsuario} className="px-3">
                    <h2 className="display-4">
                      {idx === 0 ? "🥇" : idx === 1 ? "🥈" : "🥉"}
                    </h2>
                    <h5 className="fw-bold mb-1">{p.nombre}</h5>
                    <p className="small text-warning fw-bold mb-0">
                      Lugar {idx + 1}
                    </p>
                    <p className="small text-light opacity-50">{p.rut}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default TorneoDetalle;
