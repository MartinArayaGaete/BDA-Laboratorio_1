import React, { useState, useEffect } from "react";
import torneoService from "../api/apiTorneos";
import participacionService from "../api/apiParticipaciones";

function TorneoModal({ show, onClose, torneo, tipo }) {
  const [inscritos, setInscritos] = useState([]);
  const [idUsuarioSel, setIdUsuarioSel] = useState("");
  const [numRonda, setNumRonda] = useState(1);
  const [flechas, setFlechas] = useState(["", ""]);

  // Estados para Podio (SP2)
  const [podio, setPodio] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (show && torneo) {
      if (tipo === "PUNTAJES") {
        cargarInscritos();
      } else if (tipo === "PODIO") {
        cargarPodio();
      }
    }
  }, [show, torneo, tipo]);

  const cargarInscritos = async () => {
    const data = await participacionService.obtenerInscritosPorTorneo(
      torneo.idTorneo,
    );
    setInscritos(data);
  };

  const cargarPodio = async () => {
    setLoading(true);
    try {
      const data = await torneoService.obtenerPodio(torneo.idTorneo);
      setPodio(data);
    } catch (error) {
      console.error("Error al cargar podio");
    } finally {
      setLoading(false);
    }
  };

  const handleGuardarPuntajes = async (e) => {
    e.preventDefault();
    const flechasInt = flechas.map((f) => parseInt(f));

    // Validación Trigger 1 (Conceptualmente)
    if (flechasInt.some((f) => f < 0 || f > 10 || isNaN(f))) {
      return alert("Cada flecha debe ser un número entre 0 y 10");
    }

    try {
      await torneoService.registrarPuntajes(
        torneo.idTorneo,
        idUsuarioSel,
        numRonda,
        flechasInt,
      );
      alert("¡Puntajes registrados con éxito");
      onClose();
    } catch (error) {
      alert("Error al registrar puntajes");
    }
  };

  if (!show || !torneo) return null;

  return (
    <div
      className="modal show d-block"
      style={{ backgroundColor: "rgba(0,0,0,0.6)" }}
    >
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 shadow-lg">
          <div className="modal-header bg-dark text-white">
            <h5 className="modal-title">
              {tipo === "PODIO"
                ? `🏆 Podio Final: ${torneo.nombreTorneo}`
                : `🏹 Registrar Puntos: ${torneo.nombreTorneo}`}
            </h5>
            <button
              type="button"
              className="btn-close btn-close-white"
              onClick={onClose}
            ></button>
          </div>

          <div className="modal-body p-4">
            {tipo === "PODIO" ? (
              /* PODIO */
              loading ? (
                <p>Cargando resultados...</p>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover">
                    <thead className="table-light">
                      <tr>
                        <th>Posición</th>
                        <th>Arquero</th>
                        <th>RUT</th>
                      </tr>
                    </thead>
                    <tbody>
                      {podio.map((p, index) => (
                        <tr key={p.idParticipacion}>
                          <td className="fw-bold">
                            {index === 0
                              ? "🥇 1er Lugar"
                              : index === 1
                                ? "🥈 2do Lugar"
                                : "🥉 3er Lugar"}
                          </td>
                          <td>{p.nombre}</td>
                          <td>{p.rut}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )
            ) : (
              /* --- VISTA DE REGISTRO DE PUNTAJES (SP1) --- */
              <form onSubmit={handleGuardarPuntajes}>
                <div className="row g-3">
                  <div className="col-md-6">
                    <label className="form-label fw-bold">
                      Seleccionar Arquero
                    </label>
                    <select
                      className="form-select"
                      required
                      value={idUsuarioSel}
                      onChange={(e) => setIdUsuarioSel(e.target.value)}
                    >
                      <option value="">Seleccione un arquero...</option>
                      {inscritos.map((i) => (
                        <option key={i.idUsuario} value={i.idUsuario}>
                          {i.nombre} ({i.rut})
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="col-md-6">
                    <label className="form-label fw-bold">
                      Número de Ronda
                    </label>
                    <input
                      type="number"
                      className="form-control"
                      value={numRonda}
                      onChange={(e) => setNumRonda(e.target.value)}
                      min="1"
                      required
                    />
                  </div>
                  <div className="col-12">
                    <label className="form-label fw-bold d-block">
                      Puntaje de Flechas (Rango 0-10)
                    </label>
                    <div className="d-flex gap-2">
                      {flechas.map((f, index) => (
                        <input
                          key={index}
                          type="number"
                          className="form-control text-center"
                          style={{ width: "80px" }}
                          value={f}
                          min="0"
                          max="10"
                          placeholder={`F${index + 1}`}
                          required
                          onChange={(e) => {
                            const newFlechas = [...flechas];
                            newFlechas[index] = e.target.value;
                            setFlechas(newFlechas);
                          }}
                        />
                      ))}
                    </div>
                  </div>
                </div>
                <div className="mt-4 d-flex justify-content-end gap-2">
                  <button
                    type="button"
                    className="btn btn-light"
                    onClick={onClose}
                  >
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Guardar Ronda
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default TorneoModal;
