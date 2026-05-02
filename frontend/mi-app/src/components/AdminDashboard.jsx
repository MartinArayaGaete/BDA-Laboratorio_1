import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import usuarioService from "../api/apiUsuarios";
import torneoService from "../api/apiTorneos";
import participacionService from "../api/apiParticipaciones";
import TorneoModal from "./TorneoModal"; // Importamos el nuevo componente

function AdminDashboard() {
  const [usuarios, setUsuarios] = useState([]);
  const [torneos, setTorneos] = useState([]);
  const [counts, setCounts] = useState({});
  const [loading, setLoading] = useState(true);

  // Estados para el modal
  const [modalOpen, setModalOpen] = useState(false);
  const [modalType, setModalType] = useState("");
  const [selectedTorneo, setSelectedTorneo] = useState(null);

  const navigate = useNavigate();

  const loadData = async () => {
    try {
      setLoading(true);
      const [u, t] = await Promise.all([
        usuarioService.obtenerTodos(),
        torneoService.obtenerTodos(),
      ]);
      setUsuarios(u);
      setTorneos(t);

      const cMap = {};
      for (const item of t) {
        const ins = await participacionService.obtenerInscritosPorTorneo(
          item.idTorneo,
        );
        cMap[item.idTorneo] = ins.length;
      }
      setCounts(cMap);
    } catch (e) {
      navigate("/login");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [navigate]);

  const openModal = (torneo, type) => {
    setSelectedTorneo(torneo);
    setModalType(type);
    setModalOpen(true);
  };

  const handleFinalizar = async (id) => {
    if (window.confirm("¿Cerrar torneo y calcular ranking?")) {
      await torneoService.finalizarTorneo(id);
      loadData();
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="fw-bold mb-4">🛠️ Panel de Administración</h2>

      <div className="row">
        {torneos.map((t) => (
          <div className="col-md-6 mb-3" key={t.idTorneo}>
            <div className="card shadow-sm border-0">
              <div className="card-body">
                <div className="d-flex justify-content-between align-items-start">
                  <h6 className="fw-bold text-primary">{t.nombreTorneo}</h6>
                  <span
                    className={`badge ${t.estadoTorneo === "COMPLETED" ? "bg-success" : "bg-warning"}`}
                  >
                    {t.estadoTorneo}
                  </span>
                </div>
                <p className="small mb-2">
                  👥 Participantes: {counts[t.idTorneo] || 0}
                </p>

                <div className="d-flex gap-2 mt-3">
                  {t.estadoTorneo !== "COMPLETED" ? (
                    <>
                      <button
                        className="btn btn-sm btn-primary"
                        onClick={() => openModal(t, "PUNTAJES")}
                      >
                        🎯 Puntos
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => handleFinalizar(t.idTorneo)}
                      >
                        Cerrar (SP2)
                      </button>
                    </>
                  ) : (
                    <button
                      className="btn btn-sm btn-outline-dark"
                      onClick={() => openModal(t, "PODIO")}
                    >
                      🏆 Ver Podio
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* MODAL */}
      <TorneoModal
        show={modalOpen}
        onClose={() => setModalOpen(false)}
        torneo={selectedTorneo}
        tipo={modalType}
      />
    </div>
  );
}

export default AdminDashboard;
