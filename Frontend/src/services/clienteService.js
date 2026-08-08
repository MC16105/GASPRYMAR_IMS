import api from "../api/axiosConfig";

export const listarClientes = () => {
    return api.get("/clientes");
};

export const crearCliente = (cliente) => {
    return api.post("/clientes", cliente);
};

export const actualizarCliente = (id, cliente) => {
    return api.put(`/clientes/${id}`, cliente);
};

export const eliminarCliente = (id) => {
    return api.delete(`/clientes/${id}`);
};