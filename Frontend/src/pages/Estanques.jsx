import { useEffect, useState } from "react";
import {
    Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddAlt1Icon from "@mui/icons-material/PersonAddAlt1";
import { listarEstanque, crearEstanque, actualizarEstanque, eliminarEstanque } from "../services/estanqueService";

const logoGreen = "#1E5631";
const initialState = {
    nombre: "",
    codigo: "",
    area:"",
    capacidad: "",
    ubicacion: "",
    estado: ""
};

export default function Estanques() {
    const [estanques, setEstanques] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const [openModal, setOpenModal] = useState(false);
    const [estanqueActual, setEstanqueActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        cargarEstanques();
    }, []);

    const cargarEstanques = async () => {
        try {
            setCargando(true);
            setError("");
            const respuesta = await listarEstanque();
            setEstanques(respuesta.data);
        } catch (err) {
            console.error(err);
            setError("No se pudieron cargar los Estanques.");
        } finally {
            setCargando(false);
        }
    };

    const handleAbrirCrear = () => {
        setEstanqueActual(initialState);
        setIsEditing(false);
        setOpenModal(true);
    };

    const handleAbrirEditar = (estanque) => {
        setEstanqueActual(estanque);
        setIsEditing(true);
        setOpenModal(true);
    };

    const handleCerrarModal = () => {
        setOpenModal(false);
        setEstanqueActual(initialState);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await actualizarEstanque(estanqueActual.id, estanqueActual);
            } else {
                await crearEstanque(estanqueActual);
            }
            await cargarEstanques();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError("No se pudo guardar el Estanque.");
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm("¿Deseas eliminar este Estanque?")) {
            return;
        }
        try {
            await eliminarEstanque(id);
            await cargarEstanques();
        } catch (err) {
            console.error(err);
            setError("No se pudo eliminar el Estanque.");
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
                        Directorio de Estanques
                    </Typography>
                    <Typography variant="body2" sx={{ color: "#64748b", mt: 0.5 }}>
                        Gestión y control de cartera de Estanques de GASPRYMAR
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
                    Nuevo Estanque
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
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Nombre</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Codigo</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Area (MT2)</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Capacidad (LTS)</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Ubicacion</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Estado</TableCell>
                                    <TableCell align="center" sx={{ fontWeight: 700, color: "#475569" }}>Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {estanques.map((estanque, index) => (
                                    <TableRow
                                        key={estanque.id}
                                        sx={{
                                            backgroundColor: index % 2 === 0 ? "#ffffff" : "#fcfdff",
                                            "&:hover": { backgroundColor: "#f1f5f9" },
                                            transition: "background-color 0.2s"
                                        }}
                                    >
                                        <TableCell sx={{ color: "#64748b", fontWeight: 500 }}>#{estanque.id}</TableCell>
                                        <TableCell sx={{ color: "#1e293b", fontWeight: 600 }}>{estanque.nombre}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{estanque.codigo}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{estanque.area}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{estanque.capacidad}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{estanque.ubicacion}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{estanque.estado || "N/D"}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Editar">
                                                <IconButton
                                                    size="small"
                                                    onClick={() => handleAbrirEditar(estanque)}
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
                                                    onClick={() => handleEliminar(estanque.id)}
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
                                {estanques.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={8} align="center" sx={{ py: 6, color: "#94a3b8" }}>
                                            No hay estanques registrados en este momento.
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
                            {isEditing ? "Editar Información del Estanque" : "Registrar Nuevo Estanque"}
                        </Typography>
                    </DialogTitle>
                    <DialogContent sx={{ px: 3, py: 2 }}>
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5, pt: 1 }}>
                            <TextField
                                label="Nombre"
                                fullWidth
                                required
                                value={estanqueActual.nombre}
                                onChange={(e) => setEstanqueActual({ ...estanqueActual, nombre: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Codigo"
                                fullWidth
                                required
                                value={estanqueActual.codigo}
                                onChange={(e) => setEstanqueActual({ ...estanqueActual, codigo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Area"
                                fullWidth
                                required
                                value={estanqueActual.area}
                                onChange={(e) => setEstanqueActual({ ...estanqueActual, area: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Capacidad"
                                fullWidth
                                required
                                value={estanqueActual.capacidad}
                                onChange={(e) => setEstanqueActual({ ...estanqueActual, capacidad: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Ubicacion"
                                fullWidth
                                value={estanqueActual.ubicacion}
                                onChange={(e) => setEstanqueActual({ ...estanqueActual, ubicacion: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Estado"
                                fullWidth
                                value={estanqueActual.estado}
                                onChange={(e) => setEstanqueActual({ ...estanqueActual, estado: e.target.value })}
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