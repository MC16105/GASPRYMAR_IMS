import { useEffect, useState } from "react";
import { Box, Typography, Paper, Grid, CircularProgress, Alert } from "@mui/material";
import api from "../api/axiosConfig";
import Inventory2Icon from "@mui/icons-material/Inventory2";
import BusinessIcon from "@mui/icons-material/Business";
import PeopleIcon from "@mui/icons-material/People";
import WaterIcon from "@mui/icons-material/Water";
import DirectionsCarIcon from "@mui/icons-material/DirectionsCar";
import PrecisionManufacturingIcon from "@mui/icons-material/PrecisionManufacturing";

const logoGreen = "#1E5631";
export default function Dashboard() {
    const [datos, setDatos] = useState({
        insumos: 0,
        proveedores: 0,
        clientes: 0,
        estanques: 0,
        vehiculos: 0,
        maquinarias: 0
    });
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");
    useEffect(() => {
        cargarDashboard();
    }, []);
    const cargarDashboard = async () => {
        try {
            setCargando(true);
            setError("");
            const [
                insumos,
                proveedores,
                clientes,
                estanques,
                vehiculos,
                maquinarias
            ] = await Promise.all([
                api.get("/insumos"),
                api.get("/proveedores"),
                api.get("/clientes"),
                api.get("/estanques"),
                api.get("/vehiculos"),
                api.get("/maquinarias")
            ]);

            setDatos({
                insumos: insumos.data.length,
                proveedores: proveedores.data.length,
                clientes: clientes.data.length,
                estanques: estanques.data.length,
                vehiculos: vehiculos.data.length,
                maquinarias: maquinarias.data.length
            });
        } catch (err) {
            console.error("Error cargando Dashboard:", err);
            setError(
                "No se pudieron cargar los datos del Dashboard."
            );
        } finally {
            setCargando(false);
        }
    };

    const tarjetas = [
        {
            titulo: "Insumos",
            cantidad: datos.insumos,
            icon: <Inventory2Icon />
        },
        {
            titulo: "Proveedores",
            cantidad: datos.proveedores,
            icon: <BusinessIcon />
        },
        {
            titulo: "Clientes",
            cantidad: datos.clientes,
            icon: <PeopleIcon />
        },
        {
            titulo: "Estanques",
            cantidad: datos.estanques,
            icon: <WaterIcon />
        },
        {
            titulo: "Vehículos",
            cantidad: datos.vehiculos,
            icon: <DirectionsCarIcon />
        },
        {
            titulo: "Maquinaria",
            cantidad: datos.maquinarias,
            icon: <PrecisionManufacturingIcon />
        }
    ];

    return (
        <Box sx={{ p: 1 }}>
            {/* Encabezado */}
            <Box
                sx={{
                    mb: 4,
                    p: { xs: 2.5, md: 3.5 },
                    borderRadius: 4,
                    background:
                        "linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)",
                    border: "1px solid #e2e8f0",
                    boxShadow:
                        "0 4px 18px rgba(15, 23, 42, 0.04)"
                }}
            >
                <Typography
                    variant="h4"
                    sx={{
                        fontWeight: 800,
                        color: "#1e293b",
                        letterSpacing: "-0.8px"
                    }}
                >
                    Panel General
                </Typography>

                <Typography
                    variant="body1"
                    sx={{
                        color: "#64748b",
                        mt: 0.7
                    }}
                >
                    Bienvenido a <strong>GASPRYMAR IMS</strong> — Sistema
                    Integral de Inventarios
                </Typography>
            </Box>

            {/* Error */}
            {error && (
                <Alert
                    severity="error"
                    sx={{
                        mb: 3,
                        borderRadius: 3,
                        border: "1px solid",
                        borderColor: "error.light"
                    }}
                >
                    {error}
                </Alert>
            )}

            {/* Cargando */}
            {cargando ? (
                <Box
                    sx={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        minHeight: 300
                    }}
                >
                    <CircularProgress
                        size={42}
                        sx={{
                            color: logoGreen
                        }}
                    />
                </Box>
            ) : (
                <Grid
                    container
                    spacing={3}
                >
                    {tarjetas.map((tarjeta) => (
                        <Grid
                            item
                            xs={12}
                            sm={6}
                            md={4}
                            key={tarjeta.titulo}
                        >
                            <Paper
                                elevation={0}
                                sx={{
                                    position: "relative",
                                    overflow: "hidden",
                                    minHeight: 165,
                                    p: 3,
                                    borderRadius: 4,
                                    backgroundColor: "#ffffff",
                                    border: "1px solid #e2e8f0",
                                    boxShadow:
                                        "0 4px 18px rgba(15, 23, 42, 0.04)",
                                    transition:
                                        "transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease",
                                    "&:hover": {
                                        transform: "translateY(-5px)",
                                        boxShadow:
                                            "0 12px 30px rgba(30, 86, 49, 0.10)",
                                        borderColor:
                                            "rgba(30, 86, 49, 0.30)"
                                    },
                                    "&::before": {
                                        content: '""',
                                        position: "absolute",
                                        left: 0,
                                        top: 0,
                                        bottom: 0,
                                        width: "4px",
                                        backgroundColor: logoGreen
                                    }
                                }}
                            >
                                <Box
                                    sx={{
                                        display: "flex",
                                        justifyContent: "space-between",
                                        alignItems: "flex-start"
                                    }}
                                >
                                    {/* Información */}
                                    <Box>
                                        <Typography
                                            variant="body2"
                                            sx={{
                                                fontWeight: 700,
                                                color: "#64748b",
                                                textTransform: "uppercase",
                                                letterSpacing: "0.06em",
                                                fontSize: "0.72rem"
                                            }}
                                        >
                                            {tarjeta.titulo}
                                        </Typography>
                                        <Typography
                                            variant="h2"
                                            sx={{
                                                mt: 1,
                                                fontWeight: 800,
                                                color: "#1e293b",
                                                letterSpacing: "-1.5px",
                                                lineHeight: 1
                                            }}
                                        >
                                            {tarjeta.cantidad}
                                        </Typography>
                                        <Typography
                                            variant="caption"
                                            sx={{
                                                display: "block",
                                                mt: 1.5,
                                                color: "#94a3b8"
                                            }}
                                        >
                                            Registros actuales
                                        </Typography>
                                    </Box>
                                    {/* Icono */}
                                    <Box
                                        sx={{
                                            width: 54,
                                            height: 54,
                                            borderRadius: "16px",
                                            backgroundColor:
                                                "rgba(30, 86, 49, 0.08)",
                                            color: logoGreen,
                                            display: "flex",
                                            alignItems: "center",
                                            justifyContent: "center",
                                            "& svg": {
                                                fontSize: 28
                                            }
                                        }}
                                    >
                                        {tarjeta.icon}
                                    </Box>
                                </Box>
                            </Paper>
                        </Grid>
                    ))}
                </Grid>
            )}
        </Box>
   );
}