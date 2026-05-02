import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function ArcherDashboard() {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // Aquí iremos trayendo datos
    setLoading(false);
  }, [navigate]);

  return (
    <div className="container-fluid py-4 px-4">
      <h1>🎯 Mi Perfil de Arquero</h1>
      <p>Dashboard del Arquero</p>
    </div>
  );
}

export default ArcherDashboard;
