import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import SidebarLayout from "./components/SidebarLayout";
import Login from "./components/Login.jsx";
import ArcherDashboard from "./components/ArcherDashboard.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Navigate to="/login" />} />
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
