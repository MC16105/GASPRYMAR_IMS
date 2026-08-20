import { useEffect, useState } from "react";
import { Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton,
    CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip, MenuItem, Divider
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import VisibilityIcon from "@mui/icons-material/Visibility";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import {listarCompras, crearCompra, eliminarCompra} from "../services/compraService";
import api from "../api/axiosConfig";

const logoGreen = "#1E5631";
const initialState = {
    proveedorId: "",
    fecha: "",
    documentoFiscal: "",
    observaciones: "",
    detalles: []
};
const detalleVacio = {
    insumoId: "",
    cantidad: "",
    precioUnitario: ""
};
export default function Compras() {
    const [compras, setCompras] = useState([]);
    const [proveedores, setProveedores] = useState([]);
    const [insumos, setInsumos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");
    const [openModal, setOpenModal] = useState(false);
    const [compraActual, setCompraActual] = useState(initialState);
    const [openDetalle, setOpenDetalle] = useState(false);
    const [compraSeleccionada, setCompraSeleccionada] = useState(null);
    useEffect(() => {
        cargarDatos();
    }, []);

    const cargarDatos = async () => {
        try {
            setCargando(true);
            setError("");
            const [
                comprasResponse,
                proveedoresResponse,
                insumosResponse
            ] = await Promise.all([
                listarCompras(),
                api.get("/proveedores"),
                api.get("/insumos")
            ]);
            setCompras(comprasResponse.data);
            setProveedores(proveedoresResponse.data);
            setInsumos(insumosResponse.data);
        } catch (err) {
            console.error(err);
            setError(
                "No se pudieron cargar los datos de Compras."
            );
        } finally {
            setCargando(false);
        }
    };
    // ABRIR MODAL NUEVA COMPRA
    const handleAbrirCrear = () => {
        setCompraActual({
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
        setCompraActual(initialState);
    };
    // DETALLES
    const agregarDetalle = () => {
        setCompraActual({
            ...compraActual,
            detalles: [
                ...compraActual.detalles,
                { ...detalleVacio }
            ]
        });
    };
    const actualizarDetalle = (
        index, campo, valor
    ) => {
        const nuevosDetalles =
            [...compraActual.detalles];
        nuevosDetalles[index] = {
            ...nuevosDetalles[index],
            [campo]: valor
        };
        setCompraActual({
            ...compraActual,
            detalles: nuevosDetalles
        });
    };
    const eliminarDetalle = (index) => {
        const nuevosDetalles = compraActual.detalles.filter(
                (_, i) => i !== index
            );
        setCompraActual({
            ...compraActual,
            detalles: nuevosDetalles
        });
    };
    // CÁLCULOS VISUALES
    const calcularSubtotal = (detalle) => {
        const cantidad = Number(detalle.cantidad) || 0;
        const precio = Number(detalle.precioUnitario) || 0;
        return cantidad * precio;
    };
    const calcularTotal = () => {
        return compraActual.detalles.reduce(
            (total, detalle) => {
                return total + calcularSubtotal(detalle);
            },
            0
        );
    };
   // GUARDAR COMPRA
   const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        if (compraActual.detalles.length === 0) {
            setError(
                "La compra debe tener al menos un detalle."
            );
            return;
        }
        try {
            /*
             * El backend espera:
             *
             * proveedorId
             * fecha
             * documentoFiscal
             * observaciones
             * detalles[]
             *
             * NO montoTotal
             * NO subtotal
             */
            const payload = {
                proveedorId: Number(compraActual.proveedorId),
                fecha: compraActual.fecha,
                documentoFiscal: compraActual.documentoFiscal,
                observaciones: compraActual.observaciones,
                detalles: compraActual.detalles.map(
                        (detalle) => ({
                            insumoId: Number(detalle.insumoId),
                            cantidad: Number(detalle.cantidad),
                            precioUnitario: Number(detalle.precioUnitario)
                        })
                    )
            };
            await crearCompra(payload);
            await cargarDatos();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            const mensajeBackend = err.response?.data?.message;
            setError(
                mensajeBackend ||
                "No se pudo guardar la compra."
            );
        }
    };
    // ELIMINAR
    const handleEliminar = async (id) => {
        if (
            !window.confirm("¿Deseas eliminar esta compra?")
        ) {
            return;
        }
        try {
            await eliminarCompra(id);
            await cargarDatos();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "No se pudo eliminar la compra."
            );
        }
    };
    // VER DETALLE
    const handleVerDetalle = (compra) => {
        setCompraSeleccionada(compra);
        setOpenDetalle(true);
    };
    return (
        <Box sx={{ p: 1 }}>
            {/* ==================================
                CABECERA
            ================================== */}
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
                        Registro de Compras
                    </Typography>
                    <Typography
                        variant="body2"
                        sx={{
                            color: "#64748b",
                            mt: 0.5
                        }}
                    >
                        Gestión de compras e ingreso
                        de insumos a GASPRYMAR
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
                            backgroundColor:
                                "rgba(30, 86, 49, 0.08)",
                            borderColor: logoGreen
                        }
                    }}
                >
                    Nueva Compra
                </Button>
            </Box>
            {/* ==================================
                ERROR
            ================================== */}
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
            {/* ==================================
                TABLA COMPRAS
            ================================== */}
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
                                minWidth: 900
                            }}
                        >
                            <TableHead
                                sx={{
                                    backgroundColor: "#f8fafc"
                                }}
                            >
                                <TableRow>
                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        ID
                                    </TableCell>
                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Proveedor
                                    </TableCell>
                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Fecha
                                    </TableCell>
                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Documento
                                    </TableCell>
                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Total
                                    </TableCell>
                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Observaciones
                                    </TableCell>
                                    <TableCell
                                        align="center"
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Acciones
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {compras.map(
                                    (compra, index) => (
                                        <TableRow
                                            key={compra.id}
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
                                            <TableCell>
                                                #{compra.id}
                                            </TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 600
                                                }}
                                            >
                                                {compra.proveedor}
                                            </TableCell>
                                            <TableCell>
                                                {compra.fecha}
                                            </TableCell>
                                            <TableCell>
                                                {compra.documentoFiscal ||
                                                    "N/D"}
                                            </TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 700,
                                                    color: logoGreen
                                                }}
                                            >
                                                $
                                                {Number(
                                                    compra.montoTotal
                                                ).toFixed(2)}
                                            </TableCell>
                                            <TableCell>
                                                {compra.observaciones ||
                                                    "N/D"}
                                            </TableCell>
                                            <TableCell align="center">
                                                <Tooltip
                                                    title="Ver detalles"
                                                >
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleVerDetalle(
                                                                compra
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
                                                <Tooltip
                                                    title="Eliminar"
                                                >
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleEliminar(
                                                                compra.id
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
                                {compras.length === 0 && (
                                    <TableRow>
                                        <TableCell
                                            colSpan={7}
                                            align="center"
                                            sx={{
                                                py: 6,
                                                color: "#94a3b8"
                                            }}
                                        >
                                            No hay compras registradas.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>
            {/* ==================================
                MODAL NUEVA COMPRA
            ================================== */}
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
                        <ShoppingCartIcon
                            sx={{
                                color: logoGreen
                            }}
                        />
                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 700
                            }}
                        >
                            Registrar Nueva Compra
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
                                label="Proveedor"
                                required
                                fullWidth
                                value={
                                    compraActual.proveedorId
                                }
                                onChange={(e) =>
                                    setCompraActual({
                                        ...compraActual,
                                        proveedorId:
                                        e.target.value
                                    })
                                }
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
                            <TextField
                                label="Fecha"
                                type="date"
                                required
                                fullWidth
                                InputLabelProps={{
                                    shrink: true
                                }}
                                value={
                                    compraActual.fecha
                                }
                                onChange={(e) =>
                                    setCompraActual({
                                        ...compraActual,
                                        fecha:
                                        e.target.value
                                    })
                                }
                            />
                            <TextField
                                label="Documento Fiscal"
                                fullWidth
                                value={
                                    compraActual.documentoFiscal
                                }
                                onChange={(e) =>
                                    setCompraActual({
                                        ...compraActual,
                                        documentoFiscal:
                                        e.target.value
                                    })
                                }
                            />
                            <TextField
                                label="Observaciones"
                                fullWidth
                                value={
                                    compraActual.observaciones
                                }
                                onChange={(e) =>
                                    setCompraActual({
                                        ...compraActual,
                                        observaciones:
                                        e.target.value
                                    })
                                }
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
                                Detalle de Compra
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
                        {compraActual.detalles.map(
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
                                                md: "2fr 1fr 1fr 1fr auto"
                                            },
                                            gap: 2,
                                            alignItems:
                                                "center"
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
                                            onChange={(
                                                e
                                            ) =>
                                                actualizarDetalle(
                                                    index,
                                                    "insumoId",
                                                    e.target
                                                        .value
                                                )
                                            }
                                        >
                                            {insumos.map(
                                                (
                                                    insumo
                                                ) => (

                                                    <MenuItem
                                                        key={
                                                            insumo.id
                                                        }
                                                        value={
                                                            insumo.id
                                                        }
                                                    >
                                                        {
                                                            insumo.nombre
                                                        }
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
                                                min: 1
                                            }}
                                            value={
                                                detalle.cantidad
                                            }
                                            onChange={(
                                                e
                                            ) =>
                                                actualizarDetalle(
                                                    index,
                                                    "cantidad",
                                                    e.target
                                                        .value
                                                )
                                            }
                                        />
                                        {/* PRECIO */}
                                        <TextField
                                            label="Precio Unit."
                                            type="number"
                                            required
                                            inputProps={{
                                                min: 0,
                                                step: "0.01"
                                            }}
                                            value={
                                                detalle.precioUnitario
                                            }
                                            onChange={(
                                                e
                                            ) =>
                                                actualizarDetalle(
                                                    index,
                                                    "precioUnitario",
                                                    e.target
                                                        .value
                                                )
                                            }
                                        />
                                        {/* SUBTOTAL */}
                                        <TextField
                                            label="Subtotal"
                                            value={
                                                `$${calcularSubtotal(
                                                    detalle
                                                ).toFixed(
                                                    2
                                                )}`
                                            }
                                            InputProps={{
                                                readOnly:
                                                    true
                                            }}
                                        />
                                        {/* ELIMINAR DETALLE */}
                                        <Tooltip
                                            title="Eliminar detalle"
                                        >
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
                        {/* TOTAL */}
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent:
                                    "flex-end",
                                mt: 3
                            }}
                        >
                            <Paper
                                elevation={0}
                                sx={{
                                    px: 3,
                                    py: 2,
                                    borderRadius: 3,
                                    backgroundColor: "rgba(30,86,49,0.08)",
                                    border: "1px solid rgba(30,86,49,0.20)"
                                }}
                            >
                                <Typography
                                    variant="body2"
                                    color="text.secondary"
                                >
                                    Total Compra
                                </Typography>
                                <Typography
                                    variant="h4"
                                    sx={{
                                        fontWeight: 800,
                                        color:
                                        logoGreen
                                    }}
                                >
                                    $
                                    {calcularTotal().toFixed(
                                        2
                                    )}
                                </Typography>
                            </Paper>
                        </Box>
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
                            Guardar Compra
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
            {/* ==================================
                MODAL VER DETALLES
            ================================== */}
            <Dialog
                open={openDetalle}
                onClose={() =>
                    setOpenDetalle(false)
                }
                fullWidth
                maxWidth="md"
            >
                <DialogTitle>
                    Detalle de Compra
                </DialogTitle>
                <DialogContent>
                    {compraSeleccionada && (
                        <>
                            <Typography
                                sx={{
                                    mb: 2
                                }}
                            >
                                <strong>
                                    Proveedor:
                                </strong>{" "}
                                {
                                    compraSeleccionada.proveedor
                                }
                            </Typography>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Insumo</TableCell>
                                            <TableCell>Cantidad</TableCell>
                                            <TableCell>Precio</TableCell>
                                            <TableCell>Subtotal</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {compraSeleccionada.detalles?.map(
                                            (
                                                detalle
                                            ) => (
                                                <TableRow
                                                    key={
                                                        detalle.id
                                                    }
                                                >
                                                    <TableCell>
                                                        {
                                                            detalle.nombreInsumo
                                                        }
                                                    </TableCell>
                                                    <TableCell>
                                                        {
                                                            detalle.cantidad
                                                        }
                                                    </TableCell>
                                                    <TableCell>
                                                        $
                                                        {Number(
                                                            detalle.precioUnitario
                                                        ).toFixed(
                                                            2
                                                        )}
                                                    </TableCell>
                                                    <TableCell>
                                                        $
                                                        {Number(
                                                            detalle.subtotal
                                                        ).toFixed(
                                                            2
                                                        )}
                                                    </TableCell>
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