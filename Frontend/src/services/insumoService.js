import api from "../api/axiosConfig";

export const listarInsumo = () => {
    return api.get("/insumos");
};

export const crearInsumo  = (cliente) => {
    return api.post("/insumos", cliente);
};

export const actualizarInsumo  = (id, cliente) => {
    return api.put(`/insumos/${id}`, cliente);
};

export const eliminarInsumo  = (id) => {
    return api.delete(`/insumos/${id}`);
};