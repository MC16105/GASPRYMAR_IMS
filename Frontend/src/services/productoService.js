import api from "../api/axiosConfig";

export const listarProducto = () => {
    return api.get("/productos");
};

export const crearProducto  = (cliente) => {
    return api.post("/productos", cliente);
};

export const actualizarProducto  = (id, cliente) => {
    return api.put(`/productos/${id}`, cliente);
};

export const eliminarProducto  = (id) => {
    return api.delete(`/productos/${id}`);
};