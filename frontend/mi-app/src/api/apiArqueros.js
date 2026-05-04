import api from "./api";

const arquerosService = {
  obtenerHistorial: async (idUsuario, page = 0, size = 5) => {
    const response = await api.get(`/arqueros/${idUsuario}/historial`, {
      params: { page, size },
    });
    return response.data;
  },

  // Método para el Requisito 9
  obtenerMejoresMes: async () => {
    const response = await api.get("/arqueros/rendimiento/ultimo-mes");
    return response.data;
  },
};

export default arquerosService;
