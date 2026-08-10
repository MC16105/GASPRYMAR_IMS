import { useEffect, useState } from "react";
import {
    Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddAlt1Icon from "@mui/icons-material/PersonAddAlt1";
import { listarVehiculo, crearVehiculo, actualizarVehiculo, eliminarVehiculo } from "../services/vehiculoService";

const logoGreen = "#1E5631";
const initialState = {
    placa: "",
    marca: "",
    modelo:"",
    clase: "",
    tipo: "",
    anio: "",
    estado: "",
    observaciones: ""
};

export default function Vehiculos() {
    const [vehiculos, setVehiculos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const [openModal, setOpenModal] = useState(false);
    const [vehiculoActual, setVehiculoActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        cargarVehiculos();
    }, []);

    const cargarVehiculos = async () => {
        try {
            setCargando(true);
            setError("");
            const respuesta = await listarVehiculo();
            setVehiculos(respuesta.data);
        } catch (err) {
            console.error(err);
            setError("No se pudieron cargar los vehiculos.");
        } finally {
            setCargando(false);
        }
    };

    const handleAbrirCrear = () => {
        setVehiculoActual(initialState);
        setIsEditing(false);
        setOpenModal(true);
    };

    const handleAbrirEditar = (vehiculo) => {
        setVehiculoActual(vehiculo);
        setIsEditing(true);
        setOpenModal(true);
    };

    const handleCerrarModal = () => {
        setOpenModal(false);
        setVehiculoActual(initialState);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await actualizarVehiculo(vehiculoActual.id, vehiculoActual);
            } else {
                await crearVehiculo(vehiculoActual);
            }
            await cargarVehiculos();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError("No se pudo guardar el vehiculo.");
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm("¿Deseas eliminar este Vehiculo?")) {
            return;
        }
        try {
            await eliminarVehiculo(id);
            await cargarVehiculos();
        } catch (err) {
            console.error(err);
            setError("No se pudo eliminar el vehiculo.");
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
                        Directorio de Vehiculos
                    </Typography>
                    <Typography variant="body2" sx={{ color: "#64748b", mt: 0.5 }}>
                        Gestión y control de cartera de Vehiculos de GRASPYMAR
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
                    Nuevo Vehiculo
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
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Placa</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Marca</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Modelo</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Clase</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Tipo</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Año</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Estado</TableCell>
                                    <TableCell sx={{ fontWeight: 700, color: "#475569" }}>Observaciones</TableCell>
                                    <TableCell align="center" sx={{ fontWeight: 700, color: "#475569" }}>Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {vehiculos.map((vehiculo, index) => (
                                    <TableRow
                                        key={vehiculo.id}
                                        sx={{
                                            backgroundColor: index % 2 === 0 ? "#ffffff" : "#fcfdff",
                                            "&:hover": { backgroundColor: "#f1f5f9" },
                                            transition: "background-color 0.2s"
                                        }}
                                    >
                                        <TableCell sx={{ color: "#64748b", fontWeight: 500 }}>#{vehiculo.id}</TableCell>
                                        <TableCell sx={{ color: "#1e293b", fontWeight: 600 }}>{vehiculo.placa}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.marca}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.modelo}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.clase}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.tipo}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.anio}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.estado || "N/D"}</TableCell>
                                        <TableCell sx={{ color: "#334155" }}>{vehiculo.observaciones || "N/D"}</TableCell>
                                        <TableCell align="center">
                                            <Tooltip title="Editar">
                                                <IconButton
                                                    size="small"
                                                    onClick={() => handleAbrirEditar(vehiculo)}
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
                                                    onClick={() => handleEliminar(vehiculo.id)}
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
                                {vehiculos.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={10} align="center" sx={{ py: 6, color: "#94a3b8" }}>
                                            No hay vehiculos registrados en este momento.
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
                            {isEditing ? "Editar Información del Vehiculo" : "Registrar Nuevo Vehiculo"}
                        </Typography>
                    </DialogTitle>
                    <DialogContent sx={{ px: 3, py: 2 }}>
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5, pt: 1 }}>
                            <TextField
                                label="Placa"
                                fullWidth
                                required
                                value={vehiculoActual.placa}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, placa: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Marca"
                                fullWidth
                                required
                                value={vehiculoActual.marca}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, marca: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Modelo"
                                fullWidth
                                required
                                value={vehiculoActual.modelo}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, modelo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Clase"
                                fullWidth
                                required
                                value={vehiculoActual.clase}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, clase: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Tipo"
                                fullWidth
                                value={vehiculoActual.tipo}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, tipo: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Año"
                                fullWidth
                                value={vehiculoActual.anio}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, anio: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Estado"
                                fullWidth
                                value={vehiculoActual.estado}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, estado: e.target.value })}
                                sx={{ "& .MuiOutlinedInput-root": { borderRadius: "10px" } }}
                            />
                            <TextField
                                label="Observaciones"
                                fullWidth
                                value={vehiculoActual.observaciones}
                                onChange={(e) => setVehiculoActual({ ...vehiculoActual, observaciones: e.target.value })}
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