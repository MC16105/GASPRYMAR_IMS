import api from "../api/axiosConfig";

export const listarVehiculo = () => {
    return api.get("/vehiculos");
};

export const crearVehiculo  = (cliente) => {
    return api.post("/vehiculos", cliente);
};

export const actualizarVehiculo  = (id, cliente) => {
    return api.put(`/vehiculos/${id}`, cliente);
};

export const eliminarVehiculo  = (id) => {
    return api.delete(`/vehiculos/${id}`);
};