import api from "./api";

const torneoService = {
  // Obtener todos los torneos
  obtenerTodos: async () => {
    const response = await api.get("/torneos");
    return response.data;
  },

  // Crear un nuevo torneo (Requerimiento 1 y 2)
  crearTorneo: async (datosTorneo) => {
    const response = await api.post("/torneos", datosTorneo);
    return response.data;
  },

  // Registrar ronda completa - Llama al Procedimiento Almacenado 1
  registrarPuntajes: async (idTorneo, idUsuario, numeroRonda, flechas) => {
    const response = await api.post(
      `/torneos/${idTorneo}/arqueros/${idUsuario}/rondas/${numeroRonda}/flechas`,
      { flechas },
    );
    return response.data;
  },

  // Finalizar torneo y calcular ranking - Llama al Procedimiento Almacenado 2
  finalizarTorneo: async (idTorneo) => {
    const response = await api.post(`/torneos/${idTorneo}/finalizar`);
    return response.data;
  },

  // Obtener el podio (Requerimiento 10)
  obtenerPodio: async (idTorneo) => {
    const response = await api.get(`/torneos/${idTorneo}/podio`);
    return response.data;
  },
};

export default torneoService;
