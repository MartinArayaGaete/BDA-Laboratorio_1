import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import usuarioService from "../api/apiUsuarios";

function AdminDashboard() {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await usuarioService.obtenerTodos();
        setUsuarios(data);
      } catch (error) {
        localStorage.removeItem("usuarioLogueado");
        navigate("/login");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [navigate]);

  return (
    <div className="container mt-4">
      <h2 className="mb-1 text-dark fw-bold">🛠️ Panel de Administración</h2>
      <p className="text-dark mb-4">
        Gestión global de torneos y arqueros registrados.
      </p>

      {/* Tarjetas de Estadísticas (Estructura reciclable) */}
      <div className="row mb-4">
        <div className="col-md-4">
          <div className="card text-white bg-danger shadow-sm border-0">
            <div className="card-body">
              <h6 className="card-title">Total Arqueros</h6>
              <h2 className="mb-0">{usuarios.length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card text-white bg-dark shadow-sm border-0">
            <div className="card-body">
              <h6 className="card-title">Torneos Activos</h6>
              <h2 className="mb-0">1</h2> {/* Dato estático por ahora */}
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card text-dark bg-light shadow-sm border-0">
            <div className="card-body">
              <h6 className="card-title">Torneos Finalizados</h6>
              <h2 className="mb-0">4</h2> {/* Dato estático por ahora */}
            </div>
          </div>
        </div>
      </div>

      {/* Tabla Principal */}
      <h5 className="text-dark fw-bold mb-3">Gestión de Usuarios</h5>
      {loading ? (
        <div className="alert alert-info">Cargando datos...</div>
      ) : (
        <div className="card shadow-sm border-0">
          <div className="card-body p-0">
            <table className="table table-hover mb-0">
              <thead className="table-dark">
                <tr>
                  <th>Nombre</th>
                  <th>RUT</th>
                  <th>Rol</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody className="text-dark">
                {usuarios.map((u) => (
                  <tr key={u.idUsuario}>
                    <td className="fw-medium">{u.nombre}</td>
                    <td>{u.rut}</td>
                    <td>
                      <span
                        className={`badge ${u.rol === "ADMIN" ? "bg-danger" : "bg-primary"}`}
                      >
                        {u.rol}
                      </span>
                    </td>
                    <td>
                      <button className="btn btn-sm btn-outline-secondary me-2">
                        Editar
                      </button>
                      <button className="btn btn-sm btn-outline-danger">
                        Bloquear
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;
