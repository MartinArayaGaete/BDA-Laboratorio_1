import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import SidebarLayout from "./components/SidebarLayout";
import Login from "./components/Login.jsx";

function Home() {
  return <h1></h1>;
}

function App() {
  return (
    <BrowserRouter>
      <SidebarLayout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </SidebarLayout>
    </BrowserRouter>
  );
}

export default App;
