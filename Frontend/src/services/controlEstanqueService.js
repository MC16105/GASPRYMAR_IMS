import api from "../api/axiosConfig";

export const listarControlesEstanque = () => {
    return api.get("/control_estanque");
};

export const crearControlEstanque = (control) => {
    return api.post("/control_estanque", control);
};

export const buscarControlEstanquePorId = (id) => {
    return api.get(`/control_estanque/${id}`);
};

export const eliminarControlEstanque = (id) => {
    return api.delete(`/control_estanque/${id}`);
};