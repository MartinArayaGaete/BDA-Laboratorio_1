import api from "./api";

const participacionService = {
  obtenerTodas: async () => {
    const response = await api.get("/participaciones");
    return response.data;
  },

  obtenerInscritosPorTorneo: async (idTorneo) => {
    try {
      const response = await api.get(`/participaciones/torneo/${idTorneo}`);
      return response.data || [];
    } catch (error) {
      return [];
    }
  },

  inscribirArquero: async (idTorneo, idUsuario) => {
    // Usamos params para enviar los RequestParam definidos en Spring Boot
    const response = await api.post("/participaciones/inscribir", null, {
      params: { idTorneo, idUsuario },
    });
    return response.data;
  },
};

export default participacionService;
