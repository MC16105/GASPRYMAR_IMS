import api from "../api/axiosConfig";

export const listarEstanque = () => {
    return api.get("/estanques");
};

export const crearEstanque  = (cliente) => {
    return api.post("/estanques", cliente);
};

export const actualizarEstanque  = (id, cliente) => {
    return api.put(`/estanques/${id}`, cliente);
};

export const eliminarEstanque  = (id) => {
    return api.delete(`/estanques/${id}`);
};