import { Routes, Route } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Clientes from "../pages/Clientes";
import Proveedores from "../pages/Proveedores";
import Insumos from "../pages/Insumos";
import Vehiculos from "../pages/Vehiculos";
import Estanques from "../pages/Estanques";
import Maquinarias from "../pages/Maquinarias";
import Productos from "../pages/Productos";
import Compras from "../pages/Compras";
import Ventas from "../pages/Ventas";
import CargaCombustible from "../pages/CargaCombustible.jsx";

export default function AppRouter() {
    return (
        <Routes>

            <Route path="/" element={<Login />} />

            <Route path="/dashboard" element={<MainLayout />}>

                <Route index element={<Dashboard />} />

                <Route path="clientes" element={<Clientes />} />

                <Route path="proveedores" element={<Proveedores />} />

                <Route path="insumos" element={<Insumos />} />

                <Route path="vehiculos" element={<Vehiculos />} />

                <Route path="estanques" element={<Estanques />} />

                <Route path="maquinarias" element={<Maquinarias />} />

                <Route path="productos" element={<Productos />} />

                <Route path="compras" element={<Compras />} />

                <Route path="ventas" element={<Ventas />} />

                <Route path="carga_combustibles" element={<CargaCombustible />} />

            </Route>
        </Routes>
    );
}