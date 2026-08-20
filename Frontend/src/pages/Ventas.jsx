import { useEffect, useState } from "react";
import {Box, Typography, Paper, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton,
    CircularProgress, Alert, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Tooltip, MenuItem, Divider
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import VisibilityIcon from "@mui/icons-material/Visibility";
import PointOfSaleIcon from "@mui/icons-material/PointOfSale";
import {listarVentas, crearVenta, eliminarVenta} from "../services/ventaService";
import api from "../api/axiosConfig";
const logoGreen = "#1E5631";
const initialState = {
    clienteId: "",
    fecha: "",
    documentoFiscal: "",
    observaciones: "",
    detalles: []
};
const detalleVacio = {
    productoId: "",
    cantidad: "",
    precioUnitario: ""
};
export default function Ventas() {
    const [ventas, setVentas] = useState([]);
    const [clientes, setClientes] = useState([]);
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");
    const [openModal, setOpenModal] = useState(false);
    const [ventaActual, setVentaActual] = useState(initialState);
    const [openDetalle, setOpenDetalle] = useState(false);
    const [ventaSeleccionada, setVentaSeleccionada] = useState(null);
    useEffect(() => {
        cargarDatos();
    }, []);
    const cargarDatos = async () => {
        try {
            setCargando(true);
            setError("");
            const [
                ventasResponse,
                clientesResponse,
                productosResponse
            ] = await Promise.all([
                listarVentas(),
                api.get("/clientes"),
                api.get("/productos")
            ]);
            setVentas(ventasResponse.data);
            setClientes(clientesResponse.data);
            setProductos(productosResponse.data);
        } catch (err) {
            console.error(err);
            setError(
                "No se pudieron cargar los datos de Ventas."
            );
        } finally {
            setCargando(false);
        }
    };
    // NUEVA VENTA
    const handleAbrirCrear = () => {
        setVentaActual({
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
        setVentaActual(initialState);
    };
   // DETALLES
   const agregarDetalle = () => {
        setVentaActual({
            ...ventaActual,
            detalles: [
                ...ventaActual.detalles,
                { ...detalleVacio }
            ]
        });
    };
    const actualizarDetalle = (
        index, campo, valor
    ) => {
        const nuevosDetalles =
            [...ventaActual.detalles];
        nuevosDetalles[index] = {
            ...nuevosDetalles[index],
            [campo]: valor
        };
        setVentaActual({
            ...ventaActual,
            detalles: nuevosDetalles
        });
    };
    const eliminarDetalle = (index) => {
        const nuevosDetalles =
            ventaActual.detalles.filter(
                (_, i) => i !== index
            );
        setVentaActual({
            ...ventaActual,
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
        return ventaActual.detalles.reduce(
            (total, detalle) => {
                return total + calcularSubtotal(detalle);
            },
            0
        );
    };
    // GUARDAR VENTA
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        if (
            ventaActual.detalles.length === 0
        ) {
            setError(
                "La venta debe tener al menos un detalle."
            );
            return;
        }
        try {
            const payload = {
                clienteId: Number(ventaActual.clienteId),
                fecha: ventaActual.fecha,
                documentoFiscal: ventaActual.documentoFiscal,
                observaciones: ventaActual.observaciones,
                detalles: ventaActual.detalles.map(
                        (detalle) => ({
                            productoId:Number(detalle.productoId),
                            cantidad:Number(detalle.cantidad),
                            precioUnitario:Number(detalle.precioUnitario)
                        })
                    )
            };
            await crearVenta(payload);
            await cargarDatos();
            handleCerrarModal();
        } catch (err) {
            console.error(err);
            const mensajeBackend = err.response?.data?.message;
            setError(
                mensajeBackend ||
                "No se pudo guardar la venta."
            );
        }
    };
    // ELIMINAR
   const handleEliminar = async (id) => {
        if (
            !window.confirm(
                "¿Deseas eliminar esta venta?"
            )
        ) {
            return;
        }
        try {
            await eliminarVenta(id);
            await cargarDatos();
        } catch (err) {
            console.error(err);
            setError(
                err.response?.data?.message ||
                "No se pudo eliminar la venta."
            );
        }
    };
   // VER DETALLE
   const handleVerDetalle = (venta) => {
        setVentaSeleccionada(venta);
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
                    boxShadow:
                        "0px 2px 4px rgba(0,0,0,0.02)",
                    border:
                        "1px solid #f1f5f9"
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
                        Registro de Ventas
                    </Typography>
                    <Typography
                        variant="body2"
                        sx={{
                            color: "#64748b",
                            mt: 0.5
                        }}
                    >
                        Gestión de ventas y salida
                        de productos de GASPRYMAR
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
                    Nueva Venta
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
            {/* TABLA VENTAS */}
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
                                    backgroundColor:
                                        "#f8fafc"
                                }}
                            >
                                <TableRow>
                                    <TableCell sx={{ fontWeight: 700 }}>ID</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Cliente</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Fecha</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Documento</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Total</TableCell>
                                    <TableCell sx={{ fontWeight: 700 }}>Observaciones</TableCell>
                                    <TableCell
                                        align="center"
                                        sx={{ fontWeight: 700 }}
                                    >
                                        Acciones
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {ventas.map(
                                    (venta, index) => (
                                        <TableRow
                                            key={venta.id}
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
                                                #{venta.id}
                                            </TableCell>
                                            <TableCell
                                                sx={{
                                                    fontWeight: 600
                                                }}
                                            >
                                                {venta.cliente}
                                            </TableCell>
                                            <TableCell>
                                                {venta.fecha}
                                            </TableCell>
                                            <TableCell>
                                                {venta.documentoFiscal ||
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
                                                    venta.montoTotal
                                                ).toFixed(2)}
                                            </TableCell>
                                            <TableCell>
                                                {venta.observaciones ||
                                                    "N/D"}
                                            </TableCell>
                                            <TableCell align="center">
                                                <Tooltip title="Ver detalles">
                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleVerDetalle(
                                                                venta
                                                            )
                                                        }
                                                        sx={{
                                                            color:
                                                            logoGreen,
                                                            backgroundColor:
                                                                "rgba(30,86,49,0.08)",
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
                                                                venta.id
                                                            )
                                                        }
                                                        sx={{
                                                            color:
                                                                "#ef4444",
                                                            backgroundColor:
                                                                "rgba(239,68,68,0.08)"
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
                                {ventas.length === 0 && (
                                    <TableRow>
                                        <TableCell
                                            colSpan={7}
                                            align="center"
                                            sx={{
                                                py: 6,
                                                color:
                                                    "#94a3b8"
                                            }}
                                        >
                                            No hay ventas registradas.
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Paper>
            {/* MODAL NUEVA VENTA */}
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
                        <PointOfSaleIcon
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
                            Registrar Nueva Venta
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
                                label="Cliente"
                                required
                                fullWidth
                                value={
                                    ventaActual.clienteId
                                }
                                onChange={(e) =>
                                    setVentaActual({
                                        ...ventaActual,
                                        clienteId:
                                        e.target.value
                                    })
                                }
                            >
                                {clientes.map(
                                    (cliente) => (
                                        <MenuItem
                                            key={cliente.id}
                                            value={cliente.id}
                                        >
                                            {
                                                cliente.nombreRazonSocial
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
                                    ventaActual.fecha
                                }
                                onChange={(e) =>
                                    setVentaActual({
                                        ...ventaActual,
                                        fecha:
                                        e.target.value
                                    })
                                }
                            />
                            <TextField
                                label="Documento Fiscal"
                                fullWidth
                                value={
                                    ventaActual.documentoFiscal
                                }
                                onChange={(e) =>
                                    setVentaActual({
                                        ...ventaActual,
                                        documentoFiscal:
                                        e.target.value
                                    })
                                }
                            />
                            <TextField
                                label="Observaciones"
                                fullWidth
                                value={
                                    ventaActual.observaciones
                                }
                                onChange={(e) =>
                                    setVentaActual({
                                        ...ventaActual,
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
                                justifyContent:
                                    "space-between",
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
                                Detalle de Venta
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
                                Agregar Producto
                            </Button>
                        </Box>
                        {ventaActual.detalles.map(
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
                                            alignItems: "center"
                                        }}
                                    >
                                        {/* PRODUCTO */}
                                        <TextField
                                            select
                                            label="Producto"
                                            required
                                            value={
                                                detalle.productoId
                                            }
                                            onChange={(
                                                e
                                            ) =>
                                                actualizarDetalle(
                                                    index,
                                                    "productoId",
                                                    e.target
                                                        .value
                                                )
                                            }
                                        >
                                            {productos.map(
                                                (
                                                    producto
                                                ) => (

                                                    <MenuItem
                                                        key={
                                                            producto.id
                                                        }
                                                        value={
                                                            producto.id
                                                        }
                                                    >
                                                        {
                                                            producto.nombre
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
                                    Total Venta
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
                                px: 3, fontWeight: 600,
                                textTransform: "none"
                            }}
                        >
                            Guardar Venta
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
                <DialogTitle>
                    Detalle de Venta
                </DialogTitle>
                <DialogContent>
                    {ventaSeleccionada && (
                        <>
                            <Typography
                                sx={{
                                    mb: 2
                                }}
                            >
                                <strong>
                                    Cliente:
                                </strong>{" "}
                                {
                                    ventaSeleccionada.cliente
                                }
                            </Typography>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Producto</TableCell>
                                            <TableCell>Cantidad </TableCell>
                                            <TableCell>Precio</TableCell>
                                            <TableCell>Subtotal</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {ventaSeleccionada.detalles?.map(
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
                                                            detalle.nombreProducto
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