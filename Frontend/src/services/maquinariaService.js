import api from "../api/axiosConfig";

export const listarMaquinaria = () => {
    return api.get("/maquinarias");
};

export const crearMaquinaria  = (cliente) => {
    return api.post("/maquinarias", cliente);
};

export const actualizarMaquinaria  = (id, cliente) => {
    return api.put(`/maquinarias/${id}`, cliente);
};

export const eliminarMaquinaria  = (id) => {
    return api.delete(`/maquinarias/${id}`);
};