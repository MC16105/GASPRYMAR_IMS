import api from "../api/axiosConfig";

export const listarCompras = () => {
    return api.get("/compras");
};

export const buscarCompraPorId = (id) => {
    return api.get(`/compras/${id}`);
};

export const crearCompra = (compra) => {
    return api.post("/compras", compra);
};

export const actualizarCompra = (id, compra) => {
    return api.put(`/compras/${id}`, compra);
};

export const eliminarCompra = (id) => {
    return api.delete(`/compras/${id}`);
};