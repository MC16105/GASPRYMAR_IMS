import api from "../api/axiosConfig";

export const listarEstanque = () => {
    return api.get("/estanques");
};

export const crearEstanque  = (estanque) => {
    return api.post("/estanques", estanque);
};

export const actualizarEstanque  = (id, estanque) => {
    return api.put(`/estanques/${id}`, estanque);
};

export const eliminarEstanque  = (id) => {
    return api.delete(`/estanques/${id}`);
};