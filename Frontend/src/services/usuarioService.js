import api from "../api/axiosConfig";

export const listarUsuarios = () => {
    return api.get("/usuarios");
};

export const crearUsuario = (usuario) => {
    return api.post("/usuarios", usuario);
};

export const actualizarUsuario = (id, usuario) => {
    return api.put(`/usuarios/${id}`, usuario);
};

export const eliminarUsuario = (id) => {
    return api.delete(`/usuarios/${id}`);
};