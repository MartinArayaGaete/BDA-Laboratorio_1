import React from "react";
import { Link, useNavigate } from "react-router-dom";

function SidebarLayout({ children }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    // localStorage.removeItem("token");
    // localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="d-flex vh-100">
      <div
        className="bg-dark p-3 d-flex flex-column"
        style={{ width: "250px", minWidth: "250px" }}
      >
        {" "}
        {/*TODO: Agregar el { info del token sobre el nombre del usuario }*/}
        <h5 className="text-white mb-4"></h5>
        <Link to="/" className="btn btn-dark text-start mb-2">
          Inicio
        </Link>
        <button
          onClick={handleLogout}
          className="btn btn-danger text-start mt-auto"
        >
          Cerrar sesión
        </button>
      </div>
      <div className="flex-grow-1 p-3 overflow-auto">{children}</div>
    </div>
  );
}

export default SidebarLayout;
