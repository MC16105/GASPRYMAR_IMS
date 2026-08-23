import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
        "Content-Type": "application/json",
    },
});

// REQUEST
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// RESPONSE
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        const status = error.response?.status;
        // Token inválido o expirado
        if (status === 401) {
            localStorage.removeItem("token");
            localStorage.removeItem("rol");
            localStorage.removeItem("username");
            localStorage.removeItem("nombreCompleto");
            window.location.href = "/";
        }
        // Usuario autenticado, pero sin permiso
        if (status === 403) {
            console.warn(
                "Acceso denegado: no tienes permisos para realizar esta acción."
            );

        }
        return Promise.reject(error);
    }
);

export default api;