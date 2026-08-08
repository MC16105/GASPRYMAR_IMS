import { Routes, Route } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Clientes from "../pages/Clientes.jsx";

export default function AppRouter() {
    return (
        <Routes>

            <Route path="/" element={<Login />} />

            <Route path="/dashboard" element={<MainLayout />}>

                <Route index element={<Dashboard />} />

                <Route path="clientes" element={<Clientes />} />

            </Route>
        </Routes>
    );
}