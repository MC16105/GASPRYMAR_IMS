import { useEffect, useState } from "react";
import {Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton,
    CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip, MenuItem, Divider
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import VisibilityIcon from "@mui/icons-material/Visibility";
import WaterIcon from "@mui/icons-material/Water";
import {listarControlesEstanque, crearControlEstanque, eliminarControlEstanque} from "../services/controlEstanqueService";
import api from "../api/axiosConfig";
const logoGreen = "#1E5631";
const initialState = {
    estanqueId: "",
    fecha: "",
    observaciones: "",
    detalles: []
};
const detalleVacio = {
    insumoId: "",
    cantidad: ""
};
export default function ControlEstanque() {
    const [controles, setControles] = useState([]);
    const [estanques, setEstanques] = useState([]);
    const [insumos, setInsumos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");
    const [openModal, setOpenModal] = useState(false);
    const [controlActual, setControlActual] = useState(initialState);
    const [openDetalle, setOpenDetalle] = useState(false);
    const [controlSeleccionado, setControlSeleccionado] = useState(null);
    // CARGAR DATOS
    useEffect(() => {
        cargarDatos();
    }, []);
    const cargarDatos = async () => {
        try {
            setCargando(true);
            setError("");
            const [
                controlesResponse,
                estanquesResponse,
                insumosResponse
            ] = await Promise.all([
                listarControlesEstanque(),
                api.get("/estanques"),
                api.get("/insumos")
            ]);
            setControles(controlesResponse.data);
            setEstanques(estanquesResponse.data);
            setInsumos(insumosResponse.data);
        } catch (err) {
            console.error(err);
            setError(
                "No se pudieron cargar los datos de Control de Estanques."
            );
        } finally {
            setCargando(false);
        }
    };
   // NUEVO CONTROL
    const handleAbrirCrear = () => {
        setControlActual({
            ...initialState,
            fecha: new Date()
                .toISOString()
                .split("T")[0],
            detalles: [
                { ...detalleVacio }
            ]
        });
        setOpenModal(true);
    };
    const handleCerrarModal = () => {
        setOpenModal(false);
        setControlActual(initialState);
    };
    // DETALLES
    const agregarDetalle = () => {
        setControlActual({
            ...controlActual,
            detalles: [
                ...controlActual.detalles,
                { ...detalleVacio }
            ]
        });
    };
    const actualizarDetalle = (
        index, campo, valor
    ) => {
        const nuevosDetalles =
            [...controlActual.detalles];
        nuevosDetalles[index] = {
            ...nuevosDetalles[index],
            [campo]: valor
        };
        setControlActual({
            ...controlActual,
            detalles: nuevosDetalles
        });
    };
    const eliminarDetalle = (index) => {
        const nuevosDetalles =
            controlActual.detalles.filter(
                (_, i) => i !== index
            );
        setControlActual({
            ...controlActual,
            detalles: nuevosDetalles
        });
    };
    // GUARDAR
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        if (controlActual.detalles.length === 0) {
            setError(
                "El control debe tener al menos un insumo."
            );
            return;
        }
        try {
            const payload = {
                estanqueId: Number(controlActual.estanqueId),
                fecha: controlActual.fecha,
                observaciones: controlActual.observaciones,
                detalles: controlActual.detalles.map(
                        (detalle) => ({
                            insumoId: Number(detalle.insumoId),
                            cantidad: Number(detalle.cantidad)
                        })
                    )
            };
            await crearControlEstanque(payload);
            await cargarDatos();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "No se pudo guardar el control del estanque."
            );
        }
    };
   // ELIMINAR
    const handleEliminar = async (id) => {
        if (
            !window.confirm(
                "¿Deseas eliminar este control de estanque?"
            )
        ) {
            return;
        }
        try {
            await eliminarControlEstanque(id);
            await cargarDatos();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "No se pudo eliminar el control."
            );
        }
    };
   // VER DETALLE
    const handleVerDetalle = (control) => {
        setControlSeleccionado(control);
        setOpenDetalle(true);
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
                        Control de Estanques
                    </Typography>
                    <Typography
                        variant="body2"
                        sx={{
                            color: "#64748b",
                            mt: 0.5
                        }}
                    >
                        Registro del consumo de insumos por estanque de GASPRYMAR
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
                    Nuevo Control
                </Button>
            </Box>
            {/* ERROR */}
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
                    border: "1px solid #e2e8f0",
                    boxShadow: "0px 4px 20px rgba(0,0,0,0.03)"
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
                        <Table sx={{ minWidth: 800 }}>
                            <TableHead
                                sx={{
                                    backgroundColor: "#f8fafc"
                                }}
                            >
                                <TableRow>
                                    <TableCell sx={{ fontWeight: 700 }}>ID</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Estanque</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Fecha</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Observaciones</TableCell>
                                    <TableCell
                                        align="center"
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {controles.map(
                                    (control, index) => (
                                        <TableRow
                                            key={control.id}
                                            sx={{
                                                backgroundColor:
                                                    index % 2 === 0
                                                        ? "#ffffff"
                                                        : "#fcfdff",
                                                "&:hover": {
                                                    backgroundColor: "#f1f5f9"
                                                }
                                            }}
                                        >
                                            <TableCell>#{control.id} </TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 600
                                                }}
                                            >
                                                {control.estanque}</TableCell>
                                            <TableCell>{control.fecha}</TableCell>
                                            <TableCell>{control.observaciones || "N/D"}</TableCell>
                                            <TableCell align="center">
                                                <Tooltip title="Ver detalles">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleVerDetalle(
                                                                control
                                                            )
                                                        }
                                                        sx={{
                                                            color: logoGreen,
                                                            backgroundColor: "rgba(30,86,49,0.08)",
                                                            mr: 1
                                                        }}
                                                    >
                                                        <VisibilityIcon
                                                            fontSize="small"
                                                        />
                                                    </IconButton>
                                                </Tooltip>
                                                <Tooltip title="Eliminar">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleEliminar(
                                                                control.id
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
                                {controles.length === 0 && (
                                    <TableRow>
                                        <TableCell
                                            colSpan={5}
                                            align="center"
                                            sx={{
                                                py: 6,
                                                color: "#94a3b8"
                                            }}
                                        >
                                            No hay controles de estanque registrados.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>
            {/* MODAL NUEVO CONTROL */}
            <Dialog
                open={openModal}
                onClose={handleCerrarModal}
                fullWidth
                maxWidth="md"
                PaperProps={{
                    sx: {
                        borderRadius: 4
                    }
                }}
            >
                <form onSubmit={handleSubmit}>
                    <DialogTitle
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            gap: 1.5,
                            px: 3,
                            pt: 3
                        }}
                    >
                        <WaterIcon
                            sx={{ color: logoGreen }}
                        />
                        <Typography
                            variant="h6"
                            sx={{ fontWeight: 700 }}
                        >
                            Registrar Control de Estanque
                        </Typography>
                    </DialogTitle>
                    <DialogContent
                        sx={{
                            px: 3,
                            py: 2
                        }}
                    >
                        {/* DATOS GENERALES */}
                        <Box
                            sx={{
                                display: "grid",
                                gridTemplateColumns: {
                                    xs: "1fr",
                                    md: "1fr 1fr"
                                },
                                gap: 2.5,
                                pt: 1
                            }}
                        >
                            <TextField
                                select
                                label="Estanque"
                                required
                                fullWidth
                                value={
                                    controlActual.estanqueId
                                }
                                onChange={(e) =>
                                    setControlActual({
                                        ...controlActual,
                                        estanqueId:
                                        e.target.value
                                    })
                                }
                            >
                                {estanques.map(
                                    (estanque) => (
                                        <MenuItem
                                            key={estanque.id}
                                            value={estanque.id}
                                        >
                                            {estanque.nombre}
                                        </MenuItem>

                                    )
                                )}
                            </TextField>
                            <TextField
                                label="Fecha"
                                type="date"
                                required
                                fullWidth
                                InputLabelProps={{
                                    shrink: true
                                }}
                                value={
                                    controlActual.fecha
                                }
                                onChange={(e) =>
                                    setControlActual({
                                        ...controlActual,
                                        fecha:
                                        e.target.value
                                    })
                                }
                            />
                            <TextField
                                label="Observaciones"
                                fullWidth
                                multiline
                                rows={2}
                                value={
                                    controlActual.observaciones
                                }
                                onChange={(e) =>
                                    setControlActual({
                                        ...controlActual,
                                        observaciones:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    gridColumn: {
                                        xs: "auto",
                                        md: "1 / -1"
                                    }
                                }}
                            />
                        </Box>
                        <Divider sx={{ my: 3 }} />
                        {/* DETALLES */}
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                mb: 2
                            }}
                        >
                            <Typography
                                variant="h6"
                                sx={{
                                    fontWeight: 700
                                }}
                            >
                                Insumos Utilizados
                            </Typography>
                            <Button
                                startIcon={<AddIcon />}
                                onClick={agregarDetalle}
                                sx={{
                                    color: logoGreen,
                                    textTransform: "none",
                                    fontWeight: 600
                                }}
                            >
                                Agregar Insumo
                            </Button>
                        </Box>
                        {controlActual.detalles.map(
                            (detalle, index) => (
                                <Paper
                                    key={index}
                                    elevation={0}
                                    sx={{
                                        p: 2,
                                        mb: 2,
                                        borderRadius: 3,
                                        border: "1px solid #e2e8f0",
                                        backgroundColor: "#f8fafc"
                                    }}
                                >
                                    <Box
                                        sx={{
                                            display: "grid",
                                            gridTemplateColumns: {
                                                xs: "1fr",
                                                md: "2fr 1fr auto"
                                            },
                                            gap: 2,
                                            alignItems: "center"
                                        }}
                                    >
                                        {/* INSUMO */}
                                        <TextField
                                            select
                                            label="Insumo"
                                            required
                                            value={
                                                detalle.insumoId
                                            }
                                            onChange={(e) =>
                                                actualizarDetalle(
                                                    index,
                                                    "insumoId",
                                                    e.target.value
                                                )
                                            }
                                        >
                                            {insumos.map(
                                                (insumo) => (
                                                    <MenuItem
                                                        key={insumo.id}
                                                        value={insumo.id}
                                                    >
                                                        {insumo.nombre}
                                                        {" — Stock: "}
                                                        {insumo.stockActual}
                                                    </MenuItem>
                                                )
                                            )}
                                        </TextField>
                                        {/* CANTIDAD */}
                                        <TextField
                                            label="Cantidad"
                                            type="number"
                                            required
                                            inputProps={{
                                                min: 1,
                                                step: 1
                                            }}
                                            value={
                                                detalle.cantidad
                                            }
                                            onChange={(e) =>
                                                actualizarDetalle(
                                                    index,
                                                    "cantidad",
                                                    e.target.value
                                                )
                                            }
                                        />
                                        {/* ELIMINAR */}
                                        <Tooltip title="Eliminar detalle">
                                            <IconButton
                                                color="error"
                                                onClick={() =>
                                                    eliminarDetalle(
                                                        index
                                                    )
                                                }
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </Tooltip>
                                    </Box>
                                </Paper>
                            )
                        )}
                    </DialogContent>
                    <DialogActions
                        sx={{
                            px: 3,
                            pb: 3
                        }}
                    >
                        <Button
                            onClick={
                                handleCerrarModal
                            }
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
                            Guardar Control
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
            {/* MODAL VER DETALLES */}
            <Dialog
                open={openDetalle}
                onClose={() =>
                    setOpenDetalle(false)
                }
                fullWidth
                maxWidth="md"
            >
                <DialogTitle>Detalle del Control de Estanque</DialogTitle>
                <DialogContent>
                    {controlSeleccionado && (
                        <>
                            <Typography sx={{ mb: 1 }}>
                                <strong>Estanque:</strong>{" "}
                                {controlSeleccionado.estanque}
                            </Typography>
                            <Typography sx={{ mb: 2 }}>
                                <strong>Fecha:</strong>{" "}
                                {controlSeleccionado.fecha}
                            </Typography>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Insumo</TableCell>
                                            <TableCell>Cantidad</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {controlSeleccionado.detalles?.map(
                                            (detalle) => (
                                                <TableRow
                                                    key={detalle.id}
                                                >
                                                    <TableCell>{detalle.nombreInsumo}</TableCell>
                                                    <TableCell>{detalle.cantidad}</TableCell>
                                                </TableRow>
                                            )
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() =>
                            setOpenDetalle(false)
                        }
                    >
                        Cerrar
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
}