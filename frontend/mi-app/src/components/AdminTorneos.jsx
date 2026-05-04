import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import torneoService from "../api/apiTorneos";

function AdminTorneos() {
  const [torneos, setTorneos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [paginaActual, setPaginaActual] = useState(1);
  const itemsPorPagina = 6;
  const navigate = useNavigate();

  useEffect(() => {
    cargarTorneos();
  }, []);

  const cargarTorneos = async () => {
    try {
      setLoading(true);
      const t = await torneoService.obtenerTodos();
      setTorneos(t.reverse());
    } catch (e) {
      console.error("Error cargando torneos", e);
    } finally {
      setLoading(false);
    }
  };

  const indexUltimo = paginaActual * itemsPorPagina;
  const indexPrimero = indexUltimo - itemsPorPagina;
  const torneosPaginados = torneos.slice(indexPrimero, indexUltimo);
  const totalPaginas = Math.ceil(torneos.length / itemsPorPagina);

  return (
    <div className="container-fluid py-2">
      <h2 className="fw-bold mb-4 border-bottom pb-2">📋 Gestión de Torneos</h2>

      {loading ? (
        <div className="alert alert-info">Cargando catálogo de torneos...</div>
      ) : torneos.length === 0 ? (
        <div className="alert alert-warning">
          No hay torneos registrados. ¡Crea uno nuevo!
        </div>
      ) : (
        <>
          <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            {torneosPaginados.map((t) => (
              <div className="col" key={t.idTorneo}>
                <div className="card shadow-sm h-100 border-0">
                  <div className="card-body d-flex flex-column">
                    <div className="d-flex flex-wrap justify-content-between align-items-start mb-2 gap-2">
                      <h5 className="fw-bold text-dark mb-0">
                        {t.nombreTorneo}
                      </h5>
                      <span
                        className={`badge ${t.estadoTorneo === "COMPLETED" ? "bg-success" : t.estadoTorneo === "IN_COURSE" ? "bg-primary" : "bg-warning text-dark"}`}
                      >
                        {t.estadoTorneo}
                      </span>
                    </div>
                    <p className="small text-muted mb-4">
                      📅 {t.fechaInicio} al {t.fechaTermino}
                    </p>
                    <button
                      className="btn btn-dark w-100 mt-auto"
                      onClick={() => navigate(`/admin/torneo/${t.idTorneo}`)}
                    >
                      ⚙️ Administrar Torneo
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {totalPaginas > 1 && (
            <div className="d-flex justify-content-center mt-4">
              <nav>
                <ul className="pagination flex-wrap justify-content-center">
                  <li
                    className={`page-item ${paginaActual === 1 ? "disabled" : ""}`}
                  >
                    <button
                      className="page-link"
                      onClick={() => setPaginaActual((p) => p - 1)}
                    >
                      Anterior
                    </button>
                  </li>
                  {[...Array(totalPaginas)].map((_, i) => (
                    <li
                      key={i}
                      className={`page-item ${paginaActual === i + 1 ? "active" : ""}`}
                    >
                      <button
                        className="page-link"
                        onClick={() => setPaginaActual(i + 1)}
                      >
                        {i + 1}
                      </button>
                    </li>
                  ))}
                  <li
                    className={`page-item ${paginaActual === totalPaginas ? "disabled" : ""}`}
                  >
                    <button
                      className="page-link"
                      onClick={() => setPaginaActual((p) => p + 1)}
                    >
                      Siguiente
                    </button>
                  </li>
                </ul>
              </nav>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default AdminTorneos;
