import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import loginService from "../api/apiLogin";

function Login() {
  const [rut, setRut] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const userInfo = await loginService.login(rut, password);
      localStorage.setItem("usuarioLogueado", JSON.stringify(userInfo));
      navigate(userInfo.rol === "ADMIN" ? "/admin" : "/archer");
    } catch (err) {
      setError("Credenciales incorrectas. Inténtalo de nuevo.");
    }
  };

  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100 bg-light px-3">
      <div
        className="card shadow-lg border-0 w-100"
        style={{ maxWidth: "400px", borderRadius: "12px" }}
      >
        <div className="card-body p-4 p-md-5">
          <div className="text-center mb-4">
            <h3 className="fw-bold text-dark">Acceso al Sistema</h3>
            <p className="text-muted small">
              Ingresa tus credenciales para continuar
            </p>
          </div>

          {error && (
            <div className="alert alert-danger text-center small">{error}</div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label fw-semibold text-secondary">
                RUT
              </label>
              <input
                type="text"
                className="form-control form-control-lg bg-light"
                placeholder="Ej: 11111111-1"
                value={rut}
                onChange={(e) => setRut(e.target.value)}
                required
              />
            </div>
            <div className="mb-4">
              <label className="form-label fw-semibold text-secondary">
                Contraseña
              </label>
              <input
                type="password"
                className="form-control form-control-lg bg-light"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="btn btn-dark btn-lg w-100 fw-bold">
              Entrar
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
