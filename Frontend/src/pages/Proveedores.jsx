import { useEffect, useState } from "react";
import {
    Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddAlt1Icon from "@mui/icons-material/PersonAddAlt1";
import { listarProveedor, crearProveedor, actualizarProveedor, eliminarProveedor } from "../services/proveedorService";

const logoGreen = "#1E5631";
const initialState = {
    nombreRazonSocial: "",
    duiNit: "",
    nrc:"",
    direccion: "",
    telefono: "",
    correo: ""
};

export default function Proveedores() {
    const [proveedores, setProveedores] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const [openModal, setOpenModal] = useState(false);
    const [proveedorActual, setProveedorActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        cargarProveedores();
    }, []);

    const cargarProveedores = async () => {
        try {
            setCargando(true);
            setError("");
            const respuesta = await listarProveedor();
            setProveedores(respuesta.data);
        } catch (err) {
            console.error(err);
            setError("No se pudieron cargar los proveedores.");
        } finally {
            setCargando(false);
        }
    };

    const handleAbrirCrear = () => {
        setProveedorActual(initialState);
        setIsEditing(false);
        setOpenModal(true);
    };

    const handleAbrirEditar = (proveedor) => {
        setProveedorActual(proveedor);
        setIsEditing(true);
        setOpenModal(true);
    };

    const handleCerrarModal = () => {
        setOpenModal(false);
        setProveedorActual(initialState);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await actualizarProveedor(proveedorActual.id, proveedorActual);
            } else {
                await crearProveedor(proveedorActual);
            }
            await cargarProveedores();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError("No se pudo guardar el proveedor.");
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm("¿Deseas eliminar este Proveedor?")) {
            return;
        }
        try {
            await eliminarProveedor(id);
            await cargarProveedores();
        } catch (err) {
            console.error(err);
            setError("No se pudo eliminar el proveedor.");
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
                        Directorio de Proveedores
                    </Typography>
                    <Typography variant="body2" sx={{ color: "#64748b", mt: 0.5 }}>
                        Gestión y control de cartera de proveedores de GRASPYMAR
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
                    Nuevo Proveedor
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
                                {proveedores.map((proveedor, index) => (
                                    <TableRow
                                        key={proveedor.id}
                                        sx={{
                                            backgroundColor: index % 2 === 0 ? "#ffffff" : "#fcfdff",
                                            "&:hover": { backgroundColor: "#f1f5f9" },
                                            transition: "background-color 0.2s"
                                        }}
                                    >
                                        <TableCell sx={{ color: "#64748b", fontWeight: 500 }}>#{proveedor.id}</TableCell>
                                        <TableCell sx={{ color: "#1e293b", fontWeight: 600 }}>{proveedor.nombreRazonSocial}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{proveedor.duiNit}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{proveedor.nrc}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{proveedor.direccion}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{proveedor.telefono || "N/D"}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{proveedor.correo || "N/D"}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Editar">
                                                <IconButton
                                                    size="small"
                                                    onClick={() => handleAbrirEditar(proveedor)}
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
                                                    onClick={() => handleEliminar(proveedor.id)}
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
                                {proveedores.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={8} align="center" sx={{ py: 6, color: "#94a3b8" }}>
                                            No hay proveedores registrados en este momento.
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
                            {isEditing ? "Editar Información del Proveedor" : "Registrar Nuevo Proveedor"}
                        </Typography>
                    </DialogTitle>
                    <DialogContent sx={{ px: 3, py: 2 }}>
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5, pt: 1 }}>
                            <TextField
                                label="Nombre / Razón Social"
                                fullWidth
                                required
                                value={proveedorActual.nombreRazonSocial}
                                onChange={(e) => setProveedorActual({ ...proveedorActual, nombreRazonSocial: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="DUI / NIT"
                                fullWidth
                                required
                                value={proveedorActual.duiNit}
                                onChange={(e) => setProveedorActual({ ...proveedorActual, duiNit: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="NRC"
                                fullWidth
                                required
                                value={proveedorActual.nrc}
                                onChange={(e) => setProveedorActual({ ...proveedorActual, nrc: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Direccion"
                                fullWidth
                                required
                                value={proveedorActual.direccion}
                                onChange={(e) => setProveedorActual({ ...proveedorActual, direccion: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Teléfono"
                                fullWidth
                                value={proveedorActual.telefono}
                                onChange={(e) => setProveedorActual({ ...proveedorActual, telefono: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Correo Electrónico"
                                type="email"
                                fullWidth
                                value={proveedorActual.correo}
                                onChange={(e) => setProveedorActual({ ...proveedorActual, correo: e.target.value })}
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
                            {isEditing ? "Actualizar" : "Guardar"}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
}