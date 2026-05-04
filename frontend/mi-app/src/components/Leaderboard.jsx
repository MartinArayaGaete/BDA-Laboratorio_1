import React, { useEffect, useState } from "react";
import arquerosService from "../api/apiArqueros";
import torneoService from "../api/apiTorneos";

function Leaderboard() {
  const [topMes, setTopMes] = useState([]);
  const [topHistorico, setTopHistorico] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tabActiva, setTabActiva] = useState("mes"); // "mes" o "historico"

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      setLoading(true);
      const [mes, historico] = await Promise.all([
        arquerosService.obtenerMejoresMes(),
        torneoService.obtenerLeaderboardHistorico(),
      ]);
      setTopMes(mes || []);
      setTopHistorico(historico || []);
    } catch (error) {
      console.error("Error al cargar clasificaciones", error);
    } finally {
      setLoading(false);
    }
  };

  const RenderTabla = ({ datos, titulo, subtitulo }) => (
    <div className="card shadow-sm border-0">
      <div className="card-header bg-dark text-white py-3">
        <h5 className="mb-0 fw-bold">{titulo}</h5>
        <small className="text-warning">{subtitulo}</small>
      </div>
      <div className="table-responsive">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th style={{ width: "80px" }}>Pos</th>
              <th>Arquero</th>
              <th className="text-end">Promedio Puntos</th>
            </tr>
          </thead>
          <tbody>
            {datos.length > 0 ? (
              datos.map((arq) => (
                <tr key={arq.idUsuario}>
                  <td>
                    <span
                      className={`badge ${arq.posicion <= 3 ? "bg-warning text-dark" : "bg-secondary"}`}
                    >
                      {arq.posicion}°
                    </span>
                  </td>
                  <td className="fw-bold text-dark">{arq.nombre}</td>
                  <td className="text-end">
                    <span className="badge bg-info text-dark fs-6">
                      {arq.promedioPuntosFlecha.toFixed(2)}
                    </span>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="3" className="text-center py-4 text-muted">
                  No hay datos disponibles en esta categoría.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );

  if (loading)
    return (
      <div className="container mt-5 text-center">
        <h5>Analizando puntería...</h5>
      </div>
    );

  return (
    <div className="container-fluid py-4 px-lg-5">
      <div className="text-center mb-5">
        <h2 className="fw-bold text-dark text-uppercase letter-spacing-1">
          🏆 Salón de la Fama
        </h2>
        <p className="text-muted">
          Ranking oficial basado en el promedio de puntos por flecha
        </p>
      </div>

      {/* Selector de Pestañas */}
      <div className="d-flex justify-content-center mb-4">
        <div className="btn-group shadow-sm">
          <button
            className={`btn px-4 fw-bold ${tabActiva === "mes" ? "btn-dark" : "btn-outline-dark"}`}
            onClick={() => setTabActiva("mes")}
          >
            🔥 Mejores del Mes
          </button>
          <button
            className={`btn px-4 fw-bold ${tabActiva === "historico" ? "btn-dark" : "btn-outline-dark"}`}
            onClick={() => setTabActiva("historico")}
          >
            🏛️ Top 50 Histórico
          </button>
        </div>
      </div>

      <div className="row justify-content-center">
        <div className="col-12 col-xl-10">
          {tabActiva === "mes" ? (
            <RenderTabla
              datos={topMes}
              titulo="Rendimiento Mensual (Req. 9)"
              subtitulo="Basado en torneos finalizados los últimos 30 días"
            />
          ) : (
            <RenderTabla
              datos={topHistorico}
              titulo="Leaderboard Histórico (Req. 7)"
              subtitulo="Vista Materializada - Actualizada diariamente"
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default Leaderboard;
