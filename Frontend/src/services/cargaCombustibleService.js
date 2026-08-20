import api from "../api/axiosConfig";

export const listarCargasCombustible = () => {
    return api.get("/carga_combustibles");
};

export const crearCargaCombustible = (cargaCombustible) => {
    return api.post("/carga_combustibles", cargaCombustible);
};

export const actualizarCargaCombustible = (id, cargaCombustible) => {
    return api.put(`/carga_combustibles/${id}`, cargaCombustible);
};

export const eliminarCargaCombustible = (id) => {
    return api.delete(`/carga_combustibles/${id}`);
};