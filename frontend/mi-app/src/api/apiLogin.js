import api from "./api";

const loginService = {
  login: async (rut, password) => {
    const response = await api.post("/auth/login", { rut, password });
    return response.data;
  },

  refreshToken: async () => {
    const response = await api.post("/auth/refresh-token");
    return response.data;
  },
};

export default loginService;
