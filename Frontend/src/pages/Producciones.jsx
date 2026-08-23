import { useEffect, useState } from "react";
import {Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton,
    CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip, MenuItem
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PrecisionManufacturingIcon from "@mui/icons-material/PrecisionManufacturing";
import {listarProducciones, crearProduccion, actualizarProduccion, eliminarProduccion} from "../services/produccionService";
import api from "../api/axiosConfig";

const logoGreen = "#1E5631";
const initialState = {
    productoId: "",
    tipoMovimiento: "",
    fecha: "",
    cantidad: "",
    unidadMedida: "",
    observaciones: ""
};
export default function Produccion() {
    const [producciones, setProducciones] = useState([]);
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");
    const [openModal, setOpenModal] = useState(false);
    const [produccionActual, setProduccionActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);
    useEffect(() => {
        cargarDatos();
    }, []);
    const cargarDatos = async () => {
        try {
            setCargando(true);
            setError("");
            const [
                produccionesResponse,
                productosResponse
            ] = await Promise.all([
                listarProducciones(),
                api.get("/productos")
            ]);
            setProducciones(produccionesResponse.data);
            setProductos(productosResponse.data);
        } catch (err) {
            console.error(err);
            setError(
                "No se pudieron cargar los datos de producción."
            );
        } finally {
            setCargando(false);
        }
    };
    const handleAbrirCrear = () => {
        setProduccionActual({
            ...initialState,
            fecha: new Date()
                .toISOString()
                .split("T")[0]
        });
        setIsEditing(false);
        setOpenModal(true);
    };
    const handleAbrirEditar = (produccion) => {
        setProduccionActual({
            id: produccion.id,
            productoId: produccion.productoId ?? "",
            tipoMovimiento: produccion.tipoMovimiento ?? "",
            fecha: produccion.fecha ?? "",
            cantidad: produccion.cantidad ?? "",
            unidadMedida: produccion.unidadMedida ?? "",
            observaciones: produccion.observaciones ?? ""
        });
        setIsEditing(true);
        setOpenModal(true);
    };
    const handleCerrarModal = () => {
        setOpenModal(false);
        setProduccionActual(initialState);
        setIsEditing(false);
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setError("");
            const payload = {
                productoId: Number(produccionActual.productoId),
                tipoMovimiento: produccionActual.tipoMovimiento,
                fecha: produccionActual.fecha,
                cantidad: Number(produccionActual.cantidad),
                unidadMedida: produccionActual.unidadMedida,
                observaciones: produccionActual.observaciones
            };
            if (isEditing) {
                await actualizarProduccion(
                    produccionActual.id,
                    payload
                );
            } else {
                await crearProduccion(payload);

            }
            await cargarDatos();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "No se pudo guardar el registro de producción."
            );
        }
    };
    const handleEliminar = async (id) => {
        if (
            !window.confirm(
                "¿Deseas eliminar este registro de producción?"
            )
        ) {
            return;
        }
        try {
            await eliminarProduccion(id);
            await cargarDatos();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "No se pudo eliminar el registro."
            );
        }
    };
    return (
        <Box sx={{ p: 1 }}>
            {/* CABECERA */}
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
                    <Typography
                        variant="h5"
                        sx={{
                            fontWeight: 800,
                            color: "#1e293b",
                            letterSpacing: "-0.5px"
                        }}
                    >
                        Control de Producción
                    </Typography>
                    <Typography
                        variant="body2"
                        sx={{
                            color: "#64748b",
                            mt: 0.5
                        }}
                    >
                        Registro de movimientos de producción de GASPRYMAR
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
                        "&:hover": {
                            backgroundColor: "rgba(30, 86, 49, 0.08)",
                            borderColor: logoGreen
                        }
                    }}
                >
                    Nueva Producción
                </Button>
            </Box>
            {error && (
                <Alert
                    severity="error"
                    sx={{
                        mb: 3,
                        borderRadius: 2
                    }}
                >
                    {error}
                </Alert>
            )}
            {/* TABLA */}
            <Paper
                elevation={0}
                sx={{
                    borderRadius: 4,
                    overflow: "hidden",
                    border:
                        "1px solid #e2e8f0",
                    boxShadow:
                        "0px 4px 20px rgba(0,0,0,0.03)"
                }}
            >
                {cargando ? (
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                            p: 6
                        }}
                    >
                        <CircularProgress
                            sx={{ color: logoGreen }}
                        />
                    </Box>
                ) : (
                    <TableContainer>
                        <Table sx={{ minWidth: 900 }}>
                            <TableHead
                                sx={{
                                    backgroundColor: "#f8fafc"
                                }}
                            >
                                <TableRow>
                                    <TableCell sx={{ fontWeight: 700 }}>ID</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Producto</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Movimiento</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Fecha</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Cantidad</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Unidad</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Observaciones</TableCell>
                                    <TableCell
                                        align="center"
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {producciones.map(
                                    (produccion, index) => (
                                        <TableRow
                                            key={produccion.id}
                                            sx={{
                                                backgroundColor:
                                                    index % 2 === 0
                                                        ? "#ffffff"
                                                        : "#fcfdff",
                                                "&:hover": {
                                                    backgroundColor:
                                                        "#f1f5f9"
                                                }
                                            }}
                                        >
                                            <TableCell>
                                                #{produccion.id}
                                            </TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 600
                                                }}
                                            >
                                                {produccion.producto}
                                            </TableCell>
                                            <TableCell>{produccion.tipoMovimiento}</TableCell>
                                            <TableCell>{produccion.fecha}</TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 700,
                                                    color: logoGreen
                                                }}
                                            >
                                                {produccion.cantidad}</TableCell>
                                            <TableCell> {produccion.unidadMedida}</TableCell>
                                            <TableCell>{produccion.observaciones || "N/D"}</TableCell>
                                            <TableCell align="center">
                                                <Tooltip title="Editar">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleAbrirEditar(
                                                                produccion
                                                            )
                                                        }
                                                        sx={{
                                                            color: "#3b82f6",
                                                            backgroundColor: "rgba(59,130,246,0.08)",
                                                            mr: 1
                                                        }}
                                                    >
                                                        <EditIcon
                                                            fontSize="small"
                                                        />
                                                    </IconButton>
                                                </Tooltip>
                                                <Tooltip title="Eliminar">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleEliminar(
                                                                produccion.id
                                                            )
                                                        }
                                                        sx={{
                                                            color: "#ef4444",
                                                            backgroundColor: "rgba(239,68,68,0.08)"
                                                        }}
                                                    >
                                                        <DeleteIcon
                                                            fontSize="small"
                                                        />
                                                    </IconButton>
                                                </Tooltip>
                                            </TableCell>
                                        </TableRow>
                                    )
                                )}
                                {producciones.length === 0 && (
                                    <TableRow>
                                        <TableCell
                                            colSpan={8}
                                            align="center"
                                            sx={{
                                                py: 6,
                                                color: "#94a3b8"
                                            }}
                                        >
                                            No hay registros de producción.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>
            {/* MODAL */}
            <Dialog
                open={openModal}
                onClose={handleCerrarModal}
                fullWidth
                maxWidth="sm"
                PaperProps={{
                    sx: {
                        borderRadius: 4,
                        p: 1
                    }
                }}
            >
                <form onSubmit={handleSubmit}>
                    <DialogTitle
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            gap: 1.5,
                            pb: 1,
                            pt: 2,
                            px: 3
                        }}
                    >
                        <PrecisionManufacturingIcon
                            sx={{ color: logoGreen }}
                        />
                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 700,
                                color: "#1e293b"
                            }}
                        >
                            {isEditing
                                ? "Editar Producción"
                                : "Registrar Producción"}
                        </Typography>
                    </DialogTitle>
                    <DialogContent
                        sx={{
                            px: 3,
                            py: 2
                        }}
                    >
                        <Box
                            sx={{
                                display: "flex",
                                flexDirection: "column",
                                gap: 2.5,
                                pt: 1
                            }}
                        >
                            {/* PRODUCTO */}
                            <TextField
                                select
                                label="Producto"
                                fullWidth
                                required
                                value={
                                    produccionActual.productoId
                                }
                                onChange={(e) =>
                                    setProduccionActual({
                                        ...produccionActual,
                                        productoId:
                                        e.target.value
                                    })
                                }
                            >
                                {productos.map(
                                    (producto) => (
                                        <MenuItem
                                            key={producto.id}
                                            value={producto.id}
                                        >
                                            {producto.nombre}
                                        </MenuItem>
                                    )
                                )}
                            </TextField>
                            {/* TIPO MOVIMIENTO */}
                            <TextField
                                select
                                label="Tipo de Movimiento"
                                fullWidth
                                required
                                value={
                                    produccionActual.tipoMovimiento
                                }
                                onChange={(e) =>
                                    setProduccionActual({
                                        ...produccionActual,
                                        tipoMovimiento:
                                        e.target.value
                                    })
                                }
                            >
                                <MenuItem value="SIEMBRA">SIEMBRA</MenuItem>
                                <MenuItem value="COSECHA">COSECHA</MenuItem>
                                <MenuItem value="AJUSTE">AJUSTE</MenuItem>
                            </TextField>
                            {/* FECHA */}
                            <TextField
                                label="Fecha"
                                type="date"
                                fullWidth
                                required
                                InputLabelProps={{
                                    shrink: true
                                }}
                                value={
                                    produccionActual.fecha
                                }
                                onChange={(e) =>
                                    setProduccionActual({
                                        ...produccionActual,
                                        fecha:
                                        e.target.value
                                    })
                                }
                            />
                            {/* CANTIDAD */}
                            <TextField
                                label="Cantidad"
                                type="number"
                                fullWidth
                                required
                                inputProps={{
                                    min: 0.01,
                                    step: "0.01"
                                }}
                                value={
                                    produccionActual.cantidad
                                }
                                onChange={(e) =>
                                    setProduccionActual({
                                        ...produccionActual,
                                        cantidad:
                                        e.target.value
                                    })
                                }
                            />
                            {/* UNIDAD */}
                            <TextField
                                label="Unidad de Medida"
                                fullWidth
                                required
                                value={
                                    produccionActual.unidadMedida
                                }
                                onChange={(e) =>
                                    setProduccionActual({
                                        ...produccionActual,
                                        unidadMedida:
                                        e.target.value
                                    })
                                }
                            />
                            {/* OBSERVACIONES */}
                            <TextField
                                label="Observaciones"
                                fullWidth
                                multiline
                                rows={3}
                                value={
                                    produccionActual.observaciones
                                }
                                onChange={(e) =>
                                    setProduccionActual({
                                        ...produccionActual,
                                        observaciones:
                                        e.target.value
                                    })
                                }
                            />
                        </Box>
                    </DialogContent>
                    <DialogActions
                        sx={{
                            px: 3,
                            pb: 3,
                            pt: 1
                        }}
                    >
                        <Button
                            onClick={handleCerrarModal}
                            sx={{
                                color: "#64748b",
                                textTransform: "none",
                                fontWeight: 600
                            }}
                        >
                            Cancelar
                        </Button>
                        <Button
                            type="submit"
                            variant="outlined"
                            sx={{
                                color: logoGreen,
                                borderColor: logoGreen,
                                borderRadius: "10px",
                                px: 3,
                                fontWeight: 600,
                                textTransform: "none"
                            }}
                        >
                            {isEditing
                                ? "Actualizar"
                                : "Guardar"}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
}