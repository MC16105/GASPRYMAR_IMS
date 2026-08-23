import api from "../api/axiosConfig";

export const listarControlesMaquinaria = () => {
    return api.get("/control_maquinaria");
};

export const crearControlMaquinaria = (control) => {
    return api.post("/control_maquinaria", control);
};

export const buscarControlMaquinariaPorId = (id) => {
    return api.get(`/control_maquinaria/${id}`);
};

export const eliminarControlMaquinaria = (id) => {
    return api.delete(`/control_maquinaria/${id}`);
};