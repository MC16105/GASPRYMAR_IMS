import api from "../api/axiosConfig";

export const listarProducciones = () => {
    return api.get("/producciones");
};

export const crearProduccion = (produccion) => {
    return api.post("/producciones", produccion);
};

export const actualizarProduccion = (id, produccion) => {
    return api.put(`/producciones/${id}`, produccion);
};

export const eliminarProduccion = (id) => {
    return api.delete(`/producciones/${id}`);
};