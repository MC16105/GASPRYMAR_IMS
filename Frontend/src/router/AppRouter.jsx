import { Routes, Route } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";

// 1. Importa todas tus páginas de catálogos y operaciones
//import Producto from "../pages/Productos";
//import Categorias from "../pages/Categorias";
//import Proveedores from "../pages/Proveedores";
//import Inventario from "../pages/Inventario";
//import Compras from "../pages/Compras";
//import Ventas from "../pages/Ventas";
//import Usuarios from "../pages/Usuarios";
// import Perfil from "../pages/Perfil"; // Si ya la tienes creada

export default function AppRouter() {
    return (
        <Routes>
            {/* Ruta pública */}
            <Route path="/" element={<Login />} />

            {/* Rutas protegidas dentro del MainLayout */}
            <Route path="/dashboard" element={<MainLayout />}>
                {/* Ruta por defecto cuando entras a /dashboard */}
                <Route index element={<Dashboard />} />

                {/* Vista de Perfil (si la usas) *
                {/* <Route path="perfil" element={<Perfil />} /> */}

                {/* Catálogos *
                <Route path="productos" element={<Producto />} />
                <Route path="categorias" element={<Categorias />} />
                <Route path="proveedores" element={<Proveedores />} />

                {/* Operaciones *
                <Route path="inventario" element={<Inventario />} />
                <Route path="compras" element={<Compras />} />
                <Route path="ventas" element={<Ventas />} />

                {/* Seguridad *
                <Route path="usuarios" element={<Usuarios />} /> */}
            </Route>
        </Routes>
    );
}