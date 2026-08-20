import api from "../api/axiosConfig";

export const listarVentas = () => {
    return api.get("/ventas");
};

export const buscarVentaPorId = (id) => {
    return api.get(`/ventas/${id}`);
};

export const crearVenta = (venta) => {
    return api.post("/ventas", venta);
};

export const actualizarVenta = (id, venta) => {
    return api.put(`/ventas/${id}`, venta);
};

export const eliminarVenta = (id) => {
    return api.delete(`/ventas/${id}`);
};