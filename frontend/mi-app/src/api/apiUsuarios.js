import api from "./api";

const usuarioService = {
  obtenerTodos: async () => {
    const response = await api.get("/usuarios");
    return response.data;
  },

  crearUsuario: async (nuevoUsuario) => {
    const response = await api.post("/usuarios", nuevoUsuario);
    return response.data;
  },
};

export default usuarioService;
