import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function SidebarLayout({ children }) {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");
    if (usuarioGuardado) {
      setUserInfo(JSON.parse(usuarioGuardado));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("usuarioLogueado");
    navigate("/login");
  };

  return (
    <div className="d-flex vh-100">
      <div
        className="bg-dark p-3 d-flex flex-column"
        style={{ width: "250px", minWidth: "250px" }}
      >
        <h5 className="text-white mb-4 text-center">
          {userInfo ? `Hola, ${userInfo.nombre}` : "Bienvenido"}
        </h5>
        {userInfo && (
          <div className="text-center mb-4">
            <span className="badge bg-secondary">{userInfo.rol}</span>
          </div>
        )}
        <hr className="text-white" />
        <Link to="/archer" className="btn btn-dark text-start mb-2">
          Panel Principal
        </Link>
        <button
          onClick={handleLogout}
          className="btn btn-danger text-start mt-auto"
        >
          Cerrar sesión
        </button>
      </div>
      <div className="flex-grow-1 p-3 overflow-auto bg-light">{children}</div>
    </div>
  );
}

export default SidebarLayout;
