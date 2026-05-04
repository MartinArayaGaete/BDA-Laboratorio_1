import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";

function NavbarLayout({ children }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [userInfo, setUserInfo] = useState(null);
  const [isNavCollapsed, setIsNavCollapsed] = useState(true); // Estado para móvil

  useEffect(() => {
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");
    if (usuarioGuardado) setUserInfo(JSON.parse(usuarioGuardado));
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("usuarioLogueado");
    navigate("/login");
  };

  return (
    <div className="vh-100 d-flex flex-column">
      <nav
        className="navbar navbar-expand-lg shadow-sm"
        style={{
          backgroundColor: "#1A1A1A",
          borderBottom: "3px solid #FFD700",
        }}
      >
        <div className="container-fluid">
          {/* Marca */}
          <span
            className="navbar-brand text-uppercase d-flex align-items-center"
            style={{ color: "#FFD700", fontWeight: "700" }}
          >
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
              className="me-2"
            >
              <circle cx="12" cy="12" r="10" />
              <path d="m9 12 2 2 4-4" />
            </svg>
            Arquería
          </span>

          {/* Botón Móvil */}
          <button
            className="navbar-toggler border-0"
            type="button"
            onClick={() => setIsNavCollapsed(!isNavCollapsed)}
          >
            <span
              className="navbar-toggler-icon"
              style={{ filter: "invert(1)" }}
            ></span>
          </button>

          {/* Menú Colapsable */}
          <div
            className={`${isNavCollapsed ? "collapse" : ""} navbar-collapse`}
            id="navbarNav"
          >
            <ul className="navbar-nav me-auto mb-2 mb-lg-0 mt-3 mt-lg-0">
              {userInfo?.rol === "ADMIN" ? (
                <>
                  <li className="nav-item">
                    <Link
                      className="nav-link text-white"
                      to="/admin"
                      onClick={() => setIsNavCollapsed(true)}
                    >
                      Torneos
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link
                      className="nav-link text-white"
                      to="/admin/crear-torneo"
                      onClick={() => setIsNavCollapsed(true)}
                    >
                      Nuevo Torneo
                    </Link>
                  </li>
                </>
              ) : (
                <li className="nav-item">
                  <Link
                    className="nav-link text-white"
                    to="/archer"
                    onClick={() => setIsNavCollapsed(true)}
                  >
                    Mi Perfil
                  </Link>
                </li>
              )}
            </ul>

            {/* Panel de Usuario */}
            <div className="d-flex align-items-lg-center flex-column flex-lg-row gap-3 mt-3 mt-lg-0">
              <div className="text-lg-end text-start">
                <div className="text-white small fw-bold">
                  {userInfo?.nombre}
                </div>
                <span className="badge bg-warning text-dark">
                  {userInfo?.rol}
                </span>
              </div>
              <button
                onClick={handleLogout}
                className="btn btn-outline-danger btn-sm"
              >
                Cerrar Sesión
              </button>
            </div>
          </div>
        </div>
      </nav>

      {/* Contenido Principal */}
      <main
        className="flex-grow-1 p-3 p-md-4 overflow-auto"
        style={{ backgroundColor: "#F8F9FA" }}
      >
        {children}
      </main>
    </div>
  );
}

export default NavbarLayout;
