import api from "../api/axiosConfig";

export const listarInsumo = () => {
    return api.get("/insumos");
};

export const crearInsumo  = (cliente) => {
    return api.post("/insumos", insumo);
};

export const actualizarInsumo  = (id, insumo) => {
    return api.put(`/insumos/${id}`, insumo);
};

export const eliminarInsumo  = (id) => {
    return api.delete(`/insumos/${id}`);
};