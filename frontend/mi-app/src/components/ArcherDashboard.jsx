import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import usuarioService from "../api/apiUsuarios";

function ArcherDashboard() {
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
      <h2 className="mb-1 text-dark fw-bold">🎯 Panel de Arquero</h2>
      <p className="text-dark mb-4">
        Lista de usuarios registrados en el sistema.
      </p>
      {loading ? (
        <div className="alert alert-info">Cargando datos...</div>
      ) : (
        <table className="table table-bordered table-hover">
          <thead className="table-dark">
            <tr>
              <th>Nombre</th>
              <th>RUT</th>
              <th>Rol</th>
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
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default ArcherDashboard;
