import api from "../api/axiosConfig";

export const listarProveedor = () => {
    return api.get("/proveedores");
};

export const crearProveedor  = (cliente) => {
    return api.post("/proveedores", cliente);
};

export const actualizarProveedor  = (id, cliente) => {
    return api.put(`/proveedores/${id}`, cliente);
};

export const eliminarProveedor  = (id) => {
    return api.delete(`/proveedores/${id}`);
};