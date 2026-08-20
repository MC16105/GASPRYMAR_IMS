import api from "../api/axiosConfig";

export const listarVehiculo = () => {
    return api.get("/vehiculos");
};

export const crearVehiculo  = (vehiculo) => {
    return api.post("/vehiculos", vehiculo);
};

export const actualizarVehiculo  = (id, vehiculo) => {
    return api.put(`/vehiculos/${id}`, vehiculo);
};

export const eliminarVehiculo  = (id) => {
    return api.delete(`/vehiculos/${id}`);
};