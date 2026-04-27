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
      navigate("/archer");
    } catch (err) {
      setError("Credenciales incorrectas. Inténtalo de nuevo.");
    }
  };

  return (
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card p-4 shadow" style={{ width: "350px" }}>
        <h3 className="text-center mb-4 text-dark fw-bold">Iniciar Sesión</h3>
        {error && <div className="alert alert-danger text-center">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label text-dark">RUT</label>
            <input
              type="text"
              className="form-control"
              placeholder="Ej: 11111111-1"
              value={rut}
              onChange={(e) => setRut(e.target.value)}
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label text-dark">Contraseña</label>
            <input
              type="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">
            Entrar
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
