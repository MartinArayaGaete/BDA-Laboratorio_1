import api from "./api";

const arquerosService = {
  obtenerHistorial: async (idUsuario, page = 0, size = 5) => {
    const response = await api.get(`/arqueros/${idUsuario}/historial`, {
      params: { page, size },
    });
    return response.data;
  },
};

export default arquerosService;
