import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import SidebarLayout from "./components/SidebarLayout";
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
            <SidebarLayout>
              <AdminDashboard />
            </SidebarLayout>
          }
        />
        <Route
          path="/archer"
          element={
            <SidebarLayout>
              <ArcherDashboard />
            </SidebarLayout>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
