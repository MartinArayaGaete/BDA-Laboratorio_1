import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import torneoService from "../api/apiTorneos";
import categoriaService from "../api/apiCategorias";

function CrearTorneo() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState([]);
  const [nombreTorneo, setNombreTorneo] = useState("");
  const [idCategoria, setIdCategoria] = useState("");
  const [fechaInicio, setFechaInicio] = useState("");
  const [fechaTermino, setFechaTermino] = useState("");
  const [numeroRondas, setNumeroRondas] = useState(1);
  const [guardando, setGuardando] = useState(false);
  const [notificacion, setNotificacion] = useState({ tipo: "", mensaje: "" });

  useEffect(() => {
    categoriaService.obtenerTodas().then(setCategorias).catch(console.error);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (fechaInicio > fechaTermino)
      return setNotificacion({ tipo: "error", mensaje: "Fechas inválidas." });

    setGuardando(true);
    try {
      await torneoService.crearTorneo({
        nombreTorneo,
        idCategoria: parseInt(idCategoria),
        fechaInicio,
        fechaTermino,
      });
      const todos = await torneoService.obtenerTodos();
      const idNuevo = [...todos]
        .reverse()
        .find((t) => t.nombreTorneo === nombreTorneo)?.idTorneo;

      for (let i = 1; i <= parseInt(numeroRondas); i++) {
        await torneoService.crearRonda(idNuevo, i);
      }
      setNotificacion({
        tipo: "success",
        mensaje: "¡Torneo creado con éxito!",
      });
      setTimeout(() => navigate("/admin"), 2000);
    } catch (error) {
      setNotificacion({ tipo: "error", mensaje: "Fallo en la creación." });
      setGuardando(false);
    }
  };

  return (
    <div className="container-fluid py-4 d-flex justify-content-center">
      <div className="card shadow border-0 w-100" style={{ maxWidth: "700px" }}>
        <div className="card-header text-dark py-3 fw-bold bg-warning">
          Configurar Nuevo Torneo
        </div>
        <div className="card-body p-3 p-md-4">
          {notificacion.mensaje && (
            <div
              className={`alert ${notificacion.tipo === "success" ? "alert-success" : "alert-danger"}`}
            >
              {notificacion.mensaje}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label fw-bold">Nombre del Evento</label>
              <input
                type="text"
                className="form-control"
                value={nombreTorneo}
                onChange={(e) => setNombreTorneo(e.target.value)}
                required
              />
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-8">
                <label className="form-label fw-bold">Categoría</label>
                <select
                  className="form-select"
                  value={idCategoria}
                  onChange={(e) => setIdCategoria(e.target.value)}
                  required
                >
                  <option value="">Seleccione...</option>
                  {categorias.map((c) => (
                    <option key={c.idCategoria} value={c.idCategoria}>
                      {c.nombreCategoria}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-bold">N° Rondas</label>
                <input
                  type="number"
                  className="form-control"
                  min="1"
                  value={numeroRondas}
                  onChange={(e) => setNumeroRondas(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="row g-3 mb-4">
              <div className="col-12 col-sm-6">
                <label className="form-label fw-bold">Inicio</label>
                <input
                  type="date"
                  className="form-control"
                  value={fechaInicio}
                  onChange={(e) => setFechaInicio(e.target.value)}
                  required
                />
              </div>
              <div className="col-12 col-sm-6">
                <label className="form-label fw-bold">Término</label>
                <input
                  type="date"
                  className="form-control"
                  value={fechaTermino}
                  onChange={(e) => setFechaTermino(e.target.value)}
                  required
                />
              </div>
            </div>

            <div className="d-flex flex-column flex-sm-row justify-content-end gap-2">
              <button
                type="button"
                className="btn btn-light border"
                onClick={() => navigate("/admin")}
                disabled={guardando}
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="btn btn-dark fw-bold"
                disabled={guardando}
              >
                {guardando ? "Procesando..." : "Registrar"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CrearTorneo;
