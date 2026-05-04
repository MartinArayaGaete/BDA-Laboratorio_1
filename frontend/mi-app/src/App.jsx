import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import NavbarLayout from "./components/NavbarLayout";
import Login from "./components/Login.jsx";
import ArcherDashboard from "./components/ArcherDashboard.jsx";
import AdminTorneos from "./components/AdminTorneos.jsx";
import CrearTorneo from "./components/CrearTorneo.jsx";
import TorneoDetalle from "./components/TorneoDetalle.jsx";
import Leaderboard from "./components/Leaderboard.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />

        {/* --- RUTAS DEL ADMINISTRADOR --- */}
        <Route
          path="/admin"
          element={
            <NavbarLayout>
              <AdminTorneos />
            </NavbarLayout>
          }
        />
        <Route
          path="/admin/crear-torneo"
          element={
            <NavbarLayout>
              <CrearTorneo />
            </NavbarLayout>
          }
        />
        <Route
          path="/admin/torneo/:idTorneo"
          element={
            <NavbarLayout>
              <TorneoDetalle />
            </NavbarLayout>
          }
        />

        {/* --- RUTAS DEL ARQUERO --- */}
        <Route
          path="/archer"
          element={
            <NavbarLayout>
              <ArcherDashboard />
            </NavbarLayout>
          }
        />

        <Route
          path="/leaderboard"
          element={
            <NavbarLayout>
              <Leaderboard />
            </NavbarLayout>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
