import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import NavbarLayout from "./components/NavbarLayout"; // <-- Actualizado
import Login from "./components/Login.jsx";
import ArcherDashboard from "./components/ArcherDashboard.jsx";
import AdminDashboard from "./components/AdminDashboard.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />

        <Route
          path="/admin"
          element={
            <NavbarLayout>
              <AdminDashboard />
            </NavbarLayout>
          }
        />
        <Route
          path="/archer"
          element={
            <NavbarLayout>
              <ArcherDashboard />
            </NavbarLayout>
          }
        />
        <Route
          path="/arhcer"
          element={<Navigate to="/archer" replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
