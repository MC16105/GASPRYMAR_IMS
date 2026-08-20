import api from "../api/axiosConfig";

export const listarProveedor = () => {
    return api.get("/proveedores");
};

export const crearProveedor  = (proveedor) => {
    return api.post("/proveedores", proveedor);
};

export const actualizarProveedor  = (id, proveedor) => {
    return api.put(`/proveedores/${id}`, proveedor);
};

export const eliminarProveedor  = (id) => {
    return api.delete(`/proveedores/${id}`);
};