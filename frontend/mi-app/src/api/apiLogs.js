import api from "./api";

const logsService = {
  obtenerAuditoria: async (page = 0, size = 10) => {
    const response = await api.get(`/logs?page=${page}&size=${size}`);
    return response.data;
  },
};

export default logsService;
