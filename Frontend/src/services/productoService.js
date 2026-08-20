import api from "../api/axiosConfig";

export const listarProducto = () => {
    return api.get("/productos");
};

export const crearProducto  = (producto) => {
    return api.post("/productos", producto);
};

export const actualizarProducto  = (id, producto) => {
    return api.put(`/productos/${id}`, producto);
};

export const eliminarProducto  = (id) => {
    return api.delete(`/productos/${id}`);
};