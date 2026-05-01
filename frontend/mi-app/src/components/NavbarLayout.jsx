import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function NavbarLayout({ children }) {
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
    <div className="vh-100 d-flex flex-column">
      <nav className="navbar navbar-dark bg-dark px-3 shadow-sm">
        <div className="container-fluid d-flex justify-content-between align-items-center">
          <div className="d-flex align-items-center gap-3">
            <span className="navbar-brand mb-0 h1">
              {userInfo ? `Hola, ${userInfo.nombre}` : "Bienvenido"}
            </span>
            {userInfo && (
              <span className="badge bg-secondary">{userInfo.rol}</span>
            )}
          </div>

          <div className="d-flex align-items-center gap-3">
            <Link
              to={userInfo?.rol === "ADMIN" ? "/admin" : "/archer"}
              className="text-white text-decoration-none"
            >
              Panel Principal
            </Link>
            <button
              onClick={handleLogout}
              className="btn btn-outline-danger btn-sm"
            >
              Cerrar sesión
            </button>
          </div>
        </div>
      </nav>

      <main className="flex-grow-1 p-3 overflow-auto bg-light">{children}</main>
    </div>
  );
}

export default NavbarLayout;
