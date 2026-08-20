import api from "../api/axiosConfig";

export const listarMaquinaria = () => {
    return api.get("/maquinarias");
};

export const crearMaquinaria  = (maquinaria) => {
    return api.post("/maquinarias", maquinaria);
};

export const actualizarMaquinaria  = (id, maquinaria) => {
    return api.put(`/maquinarias/${id}`, maquinaria);
};

export const eliminarMaquinaria  = (id) => {
    return api.delete(`/maquinarias/${id}`);
};