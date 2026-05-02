import api from "./api";

const participacionService = {
  obtenerInscritosPorTorneo: async (idTorneo) => {
    try {
      const response = await api.get(`/participaciones/torneo/${idTorneo}`);
      return response.data || [];
    } catch (error) {
      return [];
    }
  },
};

export default participacionService;
