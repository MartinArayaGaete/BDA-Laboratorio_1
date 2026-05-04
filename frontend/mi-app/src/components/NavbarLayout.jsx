import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";

function NavbarLayout({ children }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [userInfo, setUserInfo] = useState(null);
  const [isNavCollapsed, setIsNavCollapsed] = useState(true);

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

  const styles = {
    nav: {
      backgroundColor: "#1A1A1A",
      borderBottom: "3px solid #FFD700",
      padding: "0.5rem 1rem",
    },
    navLink: (isActive) => ({
      color: isActive ? "#FFD700" : "#FFFFFF",
      textDecoration: "none",
      fontWeight: isActive ? "700" : "500",
      padding: "0.5rem 1rem",
      borderBottom: isActive ? "2px solid #FFD700" : "2px solid transparent",
      transition: "all 0.2s ease",
    }),
  };

  return (
    <div className="vh-100 d-flex flex-column">
      <nav
        className="navbar navbar-expand-lg navbar-dark shadow"
        style={styles.nav}
      >
        <div className="container-fluid">
          <Link
            className="navbar-brand d-flex align-items-center fw-bold text-uppercase"
            to="/"
            style={{ color: "#FFD700" }}
          >
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              className="me-2"
            >
              <circle cx="12" cy="12" r="10" />
              <path d="m9 12 2 2 4-4" />
            </svg>
            Arquería
          </Link>

          <button
            className="navbar-toggler border-0"
            type="button"
            onClick={() => setIsNavCollapsed(!isNavCollapsed)}
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div
            className={`${isNavCollapsed ? "collapse" : ""} navbar-collapse`}
            id="navbarNav"
          >
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <Link
                  style={styles.navLink(location.pathname === "/leaderboard")}
                  to="/leaderboard"
                  onClick={() => setIsNavCollapsed(true)}
                >
                  🏆 Leaderboard
                </Link>
              </li>

              {userInfo?.rol === "ADMIN" ? (
                <>
                  <li className="nav-item">
                    <Link
                      style={styles.navLink(location.pathname === "/admin")}
                      to="/admin"
                      onClick={() => setIsNavCollapsed(true)}
                    >
                      Torneos
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link
                      style={styles.navLink(
                        location.pathname === "/admin/crear-torneo",
                      )}
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
                    style={styles.navLink(location.pathname === "/archer")}
                    to="/archer"
                    onClick={() => setIsNavCollapsed(true)}
                  >
                    Mi Perfil
                  </Link>
                </li>
              )}
            </ul>

            <div className="d-flex align-items-center gap-3 mt-3 mt-lg-0">
              <div className="text-end d-none d-lg-block">
                <div className="text-white small fw-bold">
                  {userInfo?.nombre}
                </div>
                <span
                  className="badge bg-warning text-dark"
                  style={{ fontSize: "0.7rem" }}
                >
                  {userInfo?.rol}
                </span>
              </div>
              <button
                onClick={handleLogout}
                className="btn btn-outline-danger btn-sm fw-bold"
              >
                Cerrar Sesión
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main
        className="flex-grow-1 p-3 p-md-4 overflow-auto"
        style={{ backgroundColor: "#F8F9FA" }}
      >
        <div className="container-fluid">{children}</div>
      </main>
    </div>
  );
}

export default NavbarLayout;
