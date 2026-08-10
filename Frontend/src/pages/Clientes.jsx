import { useEffect, useState } from "react";
import {
    Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddAlt1Icon from "@mui/icons-material/PersonAddAlt1";
import { listarClientes, crearCliente, actualizarCliente, eliminarCliente } from "../services/clienteService";

const logoGreen = "#1E5631";
const initialState = {
    nombreRazonSocial: "",
    duiNit: "",
    nrc:"",
    direccion: "",
    telefono: "",
    correo: ""
};

export default function Clientes() {
    const [clientes, setClientes] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const [openModal, setOpenModal] = useState(false);
    const [clienteActual, setClienteActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        cargarClientes();
    }, []);

    const cargarClientes = async () => {
        try {
            setCargando(true);
            setError("");
            const respuesta = await listarClientes();
            setClientes(respuesta.data);
        } catch (err) {
            console.error(err);
            setError("No se pudieron cargar los clientes.");
        } finally {
            setCargando(false);
        }
    };

    const handleAbrirCrear = () => {
        setClienteActual(initialState);
        setIsEditing(false);
        setOpenModal(true);
    };

    const handleAbrirEditar = (cliente) => {
        setClienteActual(cliente);
        setIsEditing(true);
        setOpenModal(true);
    };

    const handleCerrarModal = () => {
        setOpenModal(false);
        setClienteActual(initialState);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await actualizarCliente(clienteActual.id, clienteActual);
            } else {
                await crearCliente(clienteActual);
            }
            await cargarClientes();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError("No se pudo guardar el cliente.");
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm("¿Deseas eliminar este cliente?")) {
            return;
        }
        try {
            await eliminarCliente(id);
            await cargarClientes();
        } catch (err) {
            console.error(err);
            setError("No se pudo eliminar el cliente.");
        }
    };

    return (
        <Box sx={{ p: 1 }}>
            {/* Cabecera estilizada */}
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 4,
                    backgroundColor: "#ffffff",
                    p: 3,
                    borderRadius: 4,
                    boxShadow: "0px 2px 4px rgba(0,0,0,0.02)",
                    border: "1px solid #f1f5f9"
                }}
            >
                <Box>
                    <Typography variant="h5" sx={{ fontWeight: 800, color: "#1e293b", letterSpacing: "-0.5px" }}>
                        Directorio de Clientes
                    </Typography>
                    <Typography variant="body2" sx={{ color: "#64748b", mt: 0.5 }}>
                        Gestión y control de cartera de clientes de GRASPYMAR
                    </Typography>
                </Box>
                <Button
                    variant="outlined"
                    startIcon={<AddIcon />}
                    onClick={handleAbrirCrear}
                    sx={{
                        fontWeight: 600,
                        color: logoGreen,
                        borderColor: logoGreen,
                        backgroundColor: "#ffffff",
                        borderRadius: "10px",
                        px: 3,
                        py: 1,
                        textTransform: "none",
                        boxShadow: "none",
                        "&:hover": {
                            backgroundColor: "rgba(30, 86, 49, 0.08)",
                            borderColor: logoGreen,
                        }
                    }}
                >
                    Nuevo Cliente
                </Button>
            </Box>

            {error && (
                <Alert severity="error" sx={{ mb: 3, borderRadius: 2 }}>
                    {error}
                </Alert>
            )}

            {/* Tabla con diseño moderno */}
            <Paper
                elevation={0}
                sx={{
                    borderRadius: 4,
                    overflow: "hidden",
                    border: "1px solid #e2e8f0",
                    boxShadow: "0px 4px 20px rgba(0, 0, 0, 0.03)"
                }}
            >
                {cargando ? (
                    <Box sx={{ display: "flex", justifyContent: "center", p: 6 }}>
                        <CircularProgress sx={{ color: logoGreen }} />
                    </Box>
                ) : (
                    <TableContainer>
                        <Table sx={{ minWidth: 650 }}>
                            <TableHead sx={{ backgroundColor: "#f8fafc" }}>
                                <TableRow>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569", py: 2 }}>ID</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Nombre / Razón Social</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>DUI / NIT</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>NRC</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Direccion</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Teléfono</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Correo</TableCell>
                                    <TableCell align="center" sx={{ fontWeight: 700, color: "#475569" }}>Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {clientes.map((cliente, index) => (
                                    <TableRow
                                        key={cliente.id}
                                        sx={{
                                            backgroundColor: index % 2 === 0 ? "#ffffff" : "#fcfdff",
                                            "&:hover": { backgroundColor: "#f1f5f9" },
                                            transition: "background-color 0.2s"
                                        }}
                                    >
                                        <TableCell sx={{ color: "#64748b", fontWeight: 500 }}>#{cliente.id}</TableCell>
                                        <TableCell sx={{ color: "#1e293b", fontWeight: 600 }}>{cliente.nombreRazonSocial}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{cliente.duiNit}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{cliente.nrc}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{cliente.direccion}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{cliente.telefono || "N/D"}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{cliente.correo || "N/D"}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Editar">
                                                <IconButton
                                                    size="small"
                                                    onClick={() => handleAbrirEditar(cliente)}
                                                    sx={{
                                                        color: "#3b82f6",
                                                        backgroundColor: "rgba(59, 130, 246, 0.08)",
                                                        mr: 1,
                                                        "&:hover": { backgroundColor: "rgba(59, 130, 246, 0.15)" }
                                                    }}
                                                >
                                                    <EditIcon fontSize="small" />
                                                </IconButton>
                                            </Tooltip>
                                            <Tooltip title="Eliminar">
                                                <IconButton
                                                    size="small"
                                                    onClick={() => handleEliminar(cliente.id)}
                                                    sx={{
                                                        color: "#ef4444",
                                                        backgroundColor: "rgba(239, 68, 68, 0.08)",
                                                        "&:hover": { backgroundColor: "rgba(239, 68, 68, 0.15)" }
                                                    }}
                                                >
                                                    <DeleteIcon fontSize="small" />
                                                </IconButton>
                                            </Tooltip>
                                        </TableCell>
                                    </TableRow>
                                ))}
                                {clientes.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={6} align="center" sx={{ py: 6, color: "#94a3b8" }}>
                                            No hay clientes registrados en este momento.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>

            {/* Modal Estilizado */}
            <Dialog
                open={openModal}
                onClose={handleCerrarModal}
                fullWidth
                maxWidth="sm"
                PaperProps={{
                    sx: { borderRadius: 4, p: 1 }
                }}
            >
                <form onSubmit={handleSubmit}>
                    <DialogTitle sx={{ display: "flex", alignItems: "center", gap: 1.5, pb: 1, pt: 2, px: 3 }}>
                        <PersonAddAlt1Icon sx={{ color: logoGreen }} />
                        <Typography variant="h6" sx={{ fontWeight: 700, color: "#1e293b" }}>
                            {isEditing ? "Editar Información del Cliente" : "Registrar Nuevo Cliente"}
                        </Typography>
                    </DialogTitle>
                    <DialogContent sx={{ px: 3, py: 2 }}>
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5, pt: 1 }}>
                            <TextField
                                label="Nombre / Razón Social"
                                fullWidth
                                required
                                value={clienteActual.nombreRazonSocial}
                                onChange={(e) => setClienteActual({ ...clienteActual, nombreRazonSocial: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="DUI / NIT"
                                fullWidth
                                required
                                value={clienteActual.duiNit}
                                onChange={(e) => setClienteActual({ ...clienteActual, duiNit: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="NRC"
                                fullWidth
                                required
                                value={clienteActual.nrc}
                                onChange={(e) => setClienteActual({ ...clienteActual, nrc: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Direccion"
                                fullWidth
                                required
                                value={clienteActual.direccion}
                                onChange={(e) => setClienteActual({ ...clienteActual, direccion: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Teléfono"
                                fullWidth
                                value={clienteActual.telefono}
                                onChange={(e) => setClienteActual({ ...clienteActual, telefono: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Correo Electrónico"
                                type="email"
                                fullWidth
                                value={clienteActual.correo}
                                onChange={(e) => setClienteActual({ ...clienteActual, correo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                        </Box>
                    </DialogContent>
                    <DialogActions sx={{ px: 3, pb: 3, pt: 1 }}>
                        <Button
                            onClick={handleCerrarModal}
                            sx={{ color: "#64748b", textTransform: "none", fontWeight: 600 }}
                        >
                            Cancelar
                        </Button>
                        <Button
                            type="submit"
                            variant="outlined"
                            sx={{
                                fontWeight: 600,
                                color: logoGreen,
                                borderColor: logoGreen,
                                backgroundColor: "#ffffff",
                                borderRadius: "10px",
                                px: 3,
                                textTransform: "none",
                                "&:hover": {
                                    backgroundColor: "rgba(30, 86, 49, 0.08)",
                                    borderColor: logoGreen,
                                }
                            }}
                        >
                            {isEditing ? "Actualizar Cambios" : "Guardar Cliente"}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
}