import { useEffect, useState } from "react";
import { Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton,
    CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip, MenuItem
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import LocalGasStationIcon from "@mui/icons-material/LocalGasStation";
import {listarCargasCombustible, crearCargaCombustible, actualizarCargaCombustible, eliminarCargaCombustible} from "../services/cargaCombustibleService";
import api from "../api/axiosConfig";
const logoGreen = "#1E5631";
const initialState = {
    vehiculoId: "",
    proveedorId: "",
    fecha: "",
    galones: "",
    precioGalon: "",
    kilometraje: "",
    tipoCombustible: "",
    numeroFactura: "",
    observaciones: ""
};
export default function CargaCombustible() {
    const [cargas, setCargas] = useState([]);
    const [vehiculos, setVehiculos] = useState([]);
    const [proveedores, setProveedores] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");
    const [openModal, setOpenModal] = useState(false);
    const [cargaActual, setCargaActual] = useState(initialState);
    const [isEditing, setIsEditing] = useState(false);
    // CARGAR DATOS
    useEffect(() => {
        cargarDatos();
    }, []);
    const cargarDatos = async () => {
        try {
            setCargando(true);
            setError("");
            const [
                cargasResponse,
                vehiculosResponse,
                proveedoresResponse
            ] = await Promise.all([
                listarCargasCombustible(),
                api.get("/vehiculos"),
                api.get("/proveedores")
            ]);
            setCargas(cargasResponse.data);
            setVehiculos(vehiculosResponse.data);
            setProveedores(proveedoresResponse.data);
        } catch (err) {
            console.error(
                "Error cargando datos:",
                err
            );
            setError(
                "No se pudieron cargar los datos de combustible."
            );
        } finally {
            setCargando(false);
        }
    };
    // ABRIR CREAR
    const handleAbrirCrear = () => {
        setCargaActual({
            ...initialState,
            // Coloca automáticamente la fecha actual
            fecha: new Date()
                .toISOString()
                .split("T")[0]
        });
        setIsEditing(false);
        setOpenModal(true);
    };
    // ABRIR EDITAR
    const handleAbrirEditar = (carga) => {
        setCargaActual({
            id: carga.id,
            vehiculoId: carga.vehiculoId ?? "",
            proveedorId: carga.proveedorId ?? "",
            fecha: carga.fecha ?? "",
            galones: carga.galones ?? "",
            precioGalon: carga.precioGalon ?? "",
            kilometraje: carga.kilometraje ?? "",
            tipoCombustible: carga.tipoCombustible ?? "",
            numeroFactura: carga.numeroFactura ?? "",
            observaciones: carga.observaciones ?? ""
        });
        setIsEditing(true);
        setOpenModal(true);
    };
    // CERRAR MODAL
    const handleCerrarModal = () => {
        setOpenModal(false);
        setCargaActual(initialState);
        setIsEditing(false);
    };
    // CALCULAR TOTAL VISUAL
    const calcularTotal = () => {
        const galones = Number(cargaActual.galones) || 0;
        const precio = Number(cargaActual.precioGalon) || 0;
        return galones * precio;
    };
    // GUARDAR / ACTUALIZAR
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setError("");
            const payload = {
                vehiculoId: Number(cargaActual.vehiculoId),
                proveedorId: Number(cargaActual.proveedorId),
                fecha: cargaActual.fecha,
                galones: Number(cargaActual.galones),
                precioGalon: Number(cargaActual.precioGalon),
                kilometraje: Number(cargaActual.kilometraje),
                tipoCombustible: cargaActual.tipoCombustible,
                numeroFactura: cargaActual.numeroFactura,
                observaciones: cargaActual.observaciones
            };
            if (isEditing) {
                await actualizarCargaCombustible(
                    cargaActual.id,
                    payload
                );
            } else {
                await crearCargaCombustible(
                    payload
                );
            }
            await cargarDatos();
            handleCerrarModal();
        } catch (err) {
            console.error(
                "Error guardando carga:",
                err
            );
            setError(
                err.response?.data?.message ||
                "No se pudo guardar la carga de combustible."
            );
        }
    };
    // ELIMINAR
    const handleEliminar = async (id) => {
        const confirmar =
            window.confirm(
                "¿Deseas eliminar esta carga de combustible?"
            );
        if (!confirmar) {
            return;
        }
        try {
            setError("");
            await eliminarCargaCombustible(id);
            await cargarDatos();
        } catch (err) {
            console.error(
                "Error eliminando carga:",
                err
            );
            setError(
                err.response?.data?.message ||
                "No se pudo eliminar la carga de combustible."
            );
        }
    };
    // RENDER
    return (
        <Box sx={{ p: 1 }}>
            {/* ===================== */}
            {/* CABECERA */}
            {/* ===================== */}
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
                        Control de Combustible
                    </Typography>
                    <Typography
                        variant="body2"
                        sx={{
                            color: "#64748b",
                            mt: 0.5
                        }}
                    >
                        Registro y control de cargas
                        de combustible de GASPRYMAR
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
                            backgroundColor:
                                "rgba(30, 86, 49, 0.08)",
                            borderColor:
                            logoGreen
                        }
                    }}
                >
                    Nueva Carga
                </Button>

            </Box>
            {/* ===================== */}
            {/* ERROR */}
            {/* ===================== */}
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
            {/* ===================== */}
            {/* TABLA */}
            {/* ===================== */}
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
                            sx={{
                                color: logoGreen
                            }}
                        />
                    </Box>
                ) : (
                    <TableContainer>
                        <Table
                            sx={{
                                minWidth: 1200
                            }}
                        >
                            {/* ENCABEZADOS */}
                            <TableHead
                                sx={{
                                    backgroundColor: "#f8fafc"
                                }}
                            >
                                <TableRow>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        ID
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Vehículo
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Proveedor
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Fecha
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Galones
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Precio/Galón
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Total
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Kilometraje
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Combustible
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Factura
                                    </TableCell>
                                    <TableCell
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Observaciones
                                    </TableCell>
                                    <TableCell
                                        align="center"
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Acciones
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            {/* CUERPO */}
                            <TableBody>
                                {cargas.map(
                                    (carga, index) => (
                                        <TableRow
                                            key={carga.id}
                                            sx={{
                                                backgroundColor:
                                                    index % 2 === 0
                                                        ? "#ffffff"
                                                        : "#fcfdff",
                                                "&:hover": {
                                                    backgroundColor: "#f1f5f9"
                                                },
                                                transition: "background-color 0.2s"
                                            }}
                                        >
                                            <TableCell
                                                sx={{
                                                    color: "#64748b",
                                                    fontWeight: 500
                                                }}
                                            >
                                                #{carga.id}
                                            </TableCell>
                                            <TableCell
                                                sx={{
                                                    color: "#1e293b",
                                                    fontWeight: 600
                                                }}
                                            >
                                                {carga.vehiculo}
                                            </TableCell>
                                            <TableCell> {carga.proveedor}</TableCell>
                                            <TableCell>{carga.fecha}</TableCell>
                                            <TableCell>{carga.galones}</TableCell>
                                            <TableCell>
                                                $
                                                {Number(
                                                    carga.precioGalon
                                                ).toFixed(2)}
                                            </TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 700,
                                                    color: logoGreen
                                                }}
                                            >
                                                $
                                                {Number(
                                                    carga.total
                                                ).toFixed(2)}
                                            </TableCell>
                                            <TableCell>{carga.kilometraje}</TableCell>
                                            <TableCell>{carga.tipoCombustible}</TableCell>
                                            <TableCell>{carga.numeroFactura}</TableCell>
                                            <TableCell>{carga.observaciones}</TableCell>
                                            <TableCell align="center">
                                                {/* EDITAR */}
                                                <Tooltip title="Editar">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleAbrirEditar(
                                                                carga
                                                            )
                                                        }
                                                        sx={{
                                                            color: "#3b82f6",
                                                            backgroundColor: "rgba(59,130,246,0.08)",
                                                            mr: 1,
                                                            "&:hover": {
                                                                backgroundColor:
                                                                    "rgba(59,130,246,0.15)"
                                                            }
                                                        }}
                                                    >
                                                        <EditIcon
                                                            fontSize="small"
                                                        />
                                                    </IconButton>
                                                </Tooltip>
                                                {/* ELIMINAR */}
                                                <Tooltip title="Eliminar">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleEliminar(
                                                                carga.id
                                                            )
                                                        }
                                                        sx={{
                                                            color: "#ef4444",
                                                            backgroundColor: "rgba(239,68,68,0.08)",
                                                            "&:hover": {
                                                                backgroundColor:
                                                                    "rgba(239,68,68,0.15)"
                                                            }
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
                                {/* SIN DATOS */}
                                {cargas.length === 0 && (
                                    <TableRow>
                                        <TableCell
                                            colSpan={12}
                                            align="center"
                                            sx={{
                                                py: 6,
                                                color:
                                                    "#94a3b8"
                                            }}
                                        >
                                            No hay cargas de combustible registradas.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>
            {/* ===================== */}
            {/* MODAL */}
            {/* ===================== */}
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
                    {/* TITULO */}

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
                        <LocalGasStationIcon
                            sx={{
                                color: logoGreen
                            }}
                        />
                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 700,
                                color: "#1e293b"
                            }}
                        >
                            {isEditing
                                ? "Editar Carga de Combustible"
                                : "Registrar Nueva Carga"}
                        </Typography>
                    </DialogTitle>
                    {/* CONTENIDO */}
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
                            {/* ===================== */}
                            {/* SELECT VEHÍCULO */}
                            {/* ===================== */}
                            <TextField
                                select
                                label="Vehículo"
                                fullWidth
                                required
                                value={
                                    cargaActual.vehiculoId
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        vehiculoId:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius: "10px"
                                    }
                                }}
                            >
                                {vehiculos.map(
                                    (vehiculo) => (
                                        <MenuItem
                                            key={vehiculo.id}
                                            value={vehiculo.id}
                                        >
                                            {vehiculo.placa}
                                            {" - "}
                                            {vehiculo.marca}
                                            {" "}
                                            {vehiculo.modelo}
                                        </MenuItem>
                                    )
                                )}
                            </TextField>
                            {/* ===================== */}
                            {/* SELECT PROVEEDOR */}
                            {/* ===================== */}
                            <TextField
                                select
                                label="Proveedor"
                                fullWidth
                                required
                                value={
                                    cargaActual.proveedorId
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        proveedorId:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius:
                                            "10px"
                                    }
                                }}
                            >
                                {proveedores.map(
                                    (proveedor) => (
                                        <MenuItem
                                            key={proveedor.id}
                                            value={proveedor.id}
                                        >
                                            {
                                                proveedor.nombreRazonSocial
                                            }
                                        </MenuItem>
                                    )
                                )}
                            </TextField>
                            {/* ===================== */}
                            {/* FECHA */}
                            {/* ===================== */}
                            <TextField
                                label="Fecha"
                                type="date"
                                fullWidth
                                required
                                InputLabelProps={{
                                    shrink: true
                                }}
                                value={
                                    cargaActual.fecha
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        fecha:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius:
                                            "10px"
                                    }
                                }}
                            />
                            {/* ===================== */}
                            {/* GALONES */}
                            {/* ===================== */}
                            <TextField
                                label="Galones"
                                type="number"
                                fullWidth
                                required
                                value={
                                    cargaActual.galones
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        galones:
                                        e.target.value
                                    })
                                }
                                inputProps={{
                                    min: 0.01,
                                    step: "0.01"
                                }}
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius:
                                            "10px"
                                    }
                                }}
                            />
                            {/* ===================== */}
                            {/* PRECIO GALÓN */}
                            {/* ===================== */}
                            <TextField
                                label="Precio por Galón ($)"
                                type="number"
                                fullWidth
                                required
                                value={
                                    cargaActual.precioGalon
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        precioGalon:
                                        e.target.value
                                    })
                                }
                                inputProps={{
                                    min: 0.01,
                                    step: "0.01"
                                }}
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius:
                                            "10px"
                                    }
                                }}
                            />
                            {/* ===================== */}
                            {/* TOTAL AUTOMÁTICO */}
                            {/* ===================== */}
                            <TextField
                                label="Total Calculado ($)"
                                fullWidth
                                value={
                                    calcularTotal()
                                        .toFixed(2)
                                }
                                InputProps={{
                                    readOnly: true
                                }}
                                helperText={
                                    "Galones × Precio por galón"
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius: "10px",
                                        backgroundColor: "#f8fafc"
                                    }
                                }}
                            />
                            {/* ===================== */}
                            {/* KILOMETRAJE */}
                            {/* ===================== */}
                            <TextField
                                label="Kilometraje"
                                type="number"
                                fullWidth
                                required
                                value={
                                    cargaActual.kilometraje
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        kilometraje:
                                        e.target.value
                                    })
                                }
                                inputProps={{
                                    min: 0,
                                    step: 1
                                }}
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius: "10px"
                                    }
                                }}
                            />
                            {/* ===================== */}
                            {/* TIPO COMBUSTIBLE */}
                            {/* ===================== */}
                            <TextField
                                select
                                label="Tipo de Combustible"
                                fullWidth
                                required
                                value={
                                    cargaActual.tipoCombustible
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        tipoCombustible:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius: "10px"
                                    }
                                }}
                            >
                                <MenuItem value="DIESEL">
                                    Diésel
                                </MenuItem>
                                <MenuItem value="REGULAR">
                                    Regular
                                </MenuItem>
                                <MenuItem value="SUPER">
                                    Super
                                </MenuItem>
                            </TextField>
                            {/* ===================== */}
                            {/* NUMERO FACTURA */}
                            {/* ===================== */}
                            <TextField
                                select
                                label="Tipo de Documento"
                                fullWidth
                                required
                                value={
                                    cargaActual.numeroFactura
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        numeroFactura:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius: "10px"
                                    }
                                }}
                            >
                                <MenuItem value="CCF">
                                    Comprobante Credito Fiscal
                                </MenuItem>
                                <MenuItem value="FCF">
                                    Factura Consumidor Final
                                </MenuItem>
                                <MenuItem value="DTE">
                                    Documento Tributario Electronico
                                </MenuItem>
                            </TextField>
                            {/* ===================== */}
                            {/* OBSERVACIONES */}
                            {/* ===================== */}
                            <TextField
                                label="Observaciones"
                                fullWidth
                                required
                                multiline
                                rows={3}
                                value={
                                    cargaActual.observaciones
                                }
                                onChange={(e) =>
                                    setCargaActual({
                                        ...cargaActual,
                                        observaciones:
                                        e.target.value
                                    })
                                }
                                sx={{
                                    "& .MuiOutlinedInput-root": {
                                        borderRadius:
                                            "10px"
                                    }
                                }}
                            />
                        </Box>
                    </DialogContent>
                    {/* ===================== */}
                    {/* BOTONES */}
                    {/* ===================== */}
                    <DialogActions
                        sx={{
                            px: 3,
                            pb: 3,
                            pt: 1
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
                                fontWeight: 600,
                                color: logoGreen,
                                borderColor: logoGreen,
                                backgroundColor: "#ffffff",
                                borderRadius: "10px",
                                px: 3,
                                textTransform: "none",
                                "&:hover": {
                                    backgroundColor: "rgba(30, 86, 49, 0.08)",
                                    borderColor: logoGreen
                                }
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