import { useEffect, useState } from "react";
import {
    Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddAlt1Icon from "@mui/icons-material/PersonAddAlt1";
import { listarMaquinaria, crearMaquinaria, actualizarMaquinaria, eliminarMaquinaria } from "../services/maquinariaService";

const logoGreen = "#1E5631";
const initialState = {
    nombre: "",
    codigo: "",
    tipo:"",
    marca: "",
    modelo: "",
    anio: "",
    estado: "",
    observaciones: ""
};

export default function Maquinarias() {
    const [maquinarias, setMaquinarias] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const [openModal, setOpenModal] = useState(false);
    const [maquinariaActual, setMaquinariaActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        cargarMaquinarias();
    }, []);

    const cargarMaquinarias = async () => {
        try {
            setCargando(true);
            setError("");
            const respuesta = await listarMaquinaria();
            setMaquinarias(respuesta.data);
        } catch (err) {
            console.error(err);
            setError("No se pudieron cargar la Maquinaria.");
        } finally {
            setCargando(false);
        }
    };

    const handleAbrirCrear = () => {
        setMaquinariaActual(initialState);
        setIsEditing(false);
        setOpenModal(true);
    };

    const handleAbrirEditar = (maquinaria) => {
        setMaquinariaActual(maquinaria);
        setIsEditing(true);
        setOpenModal(true);
    };

    const handleCerrarModal = () => {
        setOpenModal(false);
        setMaquinariaActual(initialState);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await actualizarMaquinaria(maquinariaActual.id, maquinariaActual);
            } else {
                await crearMaquinaria(maquinariaActual);
            }
            await cargarMaquinarias();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError("No se pudo guardar la maquinaria.");
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm("¿Deseas eliminar este Maquinaria?")) {
            return;
        }
        try {
            await eliminarMaquinaria(id);
            await cargarMaquinarias();
        } catch (err) {
            console.error(err);
            setError("No se pudo eliminar la maquinaria.");
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
                        Directorio de Maquinaria
                    </Typography>
                    <Typography variant="body2" sx={{ color: "#64748b", mt: 0.5 }}>
                        Gestión y control de cartera de Maquinaria de GASPRYMAR
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
                    Nueva Maquina
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
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Tipo</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Marca</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Modelo</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Año</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Estado</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Observaciones</TableCell>
                                    <TableCell align="center" sx={{ fontWeight: 700, color: "#475569" }}>Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {maquinarias.map((maquinaria, index) => (
                                    <TableRow
                                        key={maquinaria.id}
                                        sx={{
                                            backgroundColor: index % 2 === 0 ? "#ffffff" : "#fcfdff",
                                            "&:hover": { backgroundColor: "#f1f5f9" },
                                            transition: "background-color 0.2s"
                                        }}
                                    >
                                        <TableCell sx={{ color: "#64748b", fontWeight: 500 }}>#{maquinaria.id}</TableCell>
                                        <TableCell sx={{ color: "#1e293b", fontWeight: 600 }}>{maquinaria.nombre}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.codigo}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.tipo}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.marca}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.modelo}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.anio}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.estado || "N/D"}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{maquinaria.observaciones || "N/D"}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Editar">
                                                <IconButton
                                                    size="small"
                                                    onClick={() => handleAbrirEditar(maquinaria)}
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
                                                    onClick={() => handleEliminar(maquinaria.id)}
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
                                {maquinarias.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={10} align="center" sx={{ py: 6, color: "#94a3b8" }}>
                                            No hay maquinarias registradas en este momento.
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
                            {isEditing ? "Editar Información de la Maquinaria" : "Registrar Nueva Maquinaria"}
                        </Typography>
                    </DialogTitle>
                    <DialogContent sx={{ px: 3, py: 2 }}>
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5, pt: 1 }}>
                            <TextField
                                label="Nombre"
                                fullWidth
                                required
                                value={maquinariaActual.nombre}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, nombre: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Codigo"
                                fullWidth
                                required
                                value={maquinariaActual.codigo}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, codigo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Tipo"
                                fullWidth
                                value={maquinariaActual.tipo}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, tipo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Marca"
                                fullWidth
                                required
                                value={maquinariaActual.marca}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, marca: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Modelo"
                                fullWidth
                                required
                                value={maquinariaActual.modelo}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, modelo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Año"
                                fullWidth
                                value={maquinariaActual.anio}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, anio: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Estado"
                                fullWidth
                                value={maquinariaActual.estado}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, estado: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Observaciones"
                                fullWidth
                                value={maquinariaActual.observaciones}
                                onChange={(e) => setMaquinariaActual({ ...maquinariaActual, observaciones: e.target.value })}
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