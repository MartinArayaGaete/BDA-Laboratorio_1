import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

// Este componente es el dashboard del arquero, donde se mostrarán sus estadísticas, logros, etc.
//   loading = true mientras carga, false cuando termina
//   navigate = para ir a otras páginas
function ArcherDashboard() {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // usuario guardará los datos reales del arquero
  // empieza en null porque al cargar la página todavía no sabemos quién es
  // setUsuario(...) será la función que lo actualiza
  const [usuario, setUsuario] = useState(null);

  const [torneos, setTorneos] = useState([]);
  const [torneosCargando, setTorneosCargando] = useState(true);

  const fetchTorneos = async () => {
    try {
      setTorneosCargando(true);
      const resp = await api.get("/torneos"); // GET /api/torneos
      setTorneos(resp.data || []);
    } catch (err) {
      console.error("Error al traer torneos:", err);
    } finally {
      setTorneosCargando(false);
    }
  };


  useEffect(() => {
    // aqui para ir trayendo datos
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");

    if (usuarioGuardado) {
      const usuarioParseado = JSON.parse(usuarioGuardado);
      setUsuario(usuarioParseado);
    } else {
      navigate("/login");
    }

    if (usuario) {
      fetchTorneos();
    }
    
    setLoading(false);
  }, [navigate], [usuario]); // se vuelve a ejecutar si cambia el usuario (ej: al cargarlo desde localStorage)

  return (
    <div className="container-fluid py-4 px-4">
      <h1>🎯 Mi Perfil de Arquero</h1>
      <p>Dashboard del Arquero</p>

      {usuario && (
        <p>
          Usuario actual: {usuario.nombre} - ID: {usuario.idUsuario}
        </p>
      )}

      {/* SECCIÓN 1: ESTADÍSTICAS - 3 tarjetas */}
      <div className="row mt-5">
        <div className="col-md-4">
          <div className="card text-white bg-primary p-3">
            <h5>Torneos</h5>
            <h2>0</h2>
          </div>
        </div>
      </div>



      {/* Panel: Torneos abiertos/activos */}
      <section className="mt-4">
        <h5>🏁 Torneos Abiertos</h5>

        {torneosCargando ? (
          <div className="alert alert-info">Cargando torneos...</div>
        ) : (
          <div className="row">
            {torneos
              .filter(t => (t.estadoTorneo || "").toUpperCase() === "COMPLETED")
              .map(t => (
                <div className="col-md-6 mb-3" key={t.idTorneo}>
                  <div className="card p-3">
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <h6 className="mb-0">{t.nombreTorneo}</h6>
                        <small className="text-muted">{t.fechaInicio} — {t.fechaTermino}</small>
                      </div>
                      <button
                        className="btn btn-sm btn-primary"
                        onClick={() => {
                          // aquí llamaremos la inscripción (más adelante)
                          // navigate(`/archer/inscribir/${t.idTorneo}`);
                        }}
                      >
                        Inscribirme
                      </button>
                    </div>
                  </div>
                </div>
              ))}
          </div>
        )}
      </section>


    </div>






  );
}

export default ArcherDashboard;
// comandos para interactuar con BD

// para meterse a la BD
// docker exec -i LAB1_BDA psql -U postgres -d postgres

// para limpiar todo
// TRUNCATE TABLE flecha, ronda, participacion, torneo, logs, usuario, categoria RESTART IDENTITY CASCADE;


// cargar datos:
// docker exec -i LAB1_BDA psql -U postgres -d postgres < /home/martin/Descargas/BDA/Laboratorios/Lab1/Codigo-fuente/BDA-Laboratorio_1/backend/demo/src/main/resources/data.sql


// [para borrar todo];
// DROP SCHEMA public CASCADE;
// CREATE SCHEMA public;