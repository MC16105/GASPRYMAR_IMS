import { Routes, Route } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Clientes from "../pages/Clientes";
import Proveedores from "../pages/Proveedores";
import Insumos from "../pages/Insumos";

export default function AppRouter() {
    return (
        <Routes>

            <Route path="/" element={<Login />} />

            <Route path="/dashboard" element={<MainLayout />}>

                <Route index element={<Dashboard />} />

                <Route path="clientes" element={<Clientes />} />

                <Route path="proveedores" element={<Proveedores />} />

                <Route path="insumos" element={<Insumos />} />

            </Route>
        </Routes>
    );
}