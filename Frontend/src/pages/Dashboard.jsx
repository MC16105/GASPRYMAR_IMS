import { useEffect, useState } from "react";
import { Box, Typography, Paper, Grid, CircularProgress, Alert } from "@mui/material";
import api from "../api/axiosConfig";
export default function Dashboard() {
    const [datos, setDatos] = useState({
        insumos: 0,
        proveedores: 0,
        clientes: 0,
        estanques: 0,
        vehiculos: 0
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
                vehiculos
            ] = await Promise.all([
                api.get("/insumos"),
                api.get("/proveedores"),
                api.get("/clientes"),
                api.get("/estanques"),
                api.get("/vehiculos")
            ]);
            setDatos({
                insumos: insumos.data.length,
                proveedores: proveedores.data.length,
                clientes: clientes.data.length,
                estanques: estanques.data.length,
                vehiculos: vehiculos.data.length
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
            cantidad: datos.insumos
        },
        {
            titulo: "Proveedores",
            cantidad: datos.proveedores
        },
        {
            titulo: "Clientes",
            cantidad: datos.clientes
        },
        {
            titulo: "Estanques",
            cantidad: datos.estanques
        },
        {
            titulo: "Vehículos",
            cantidad: datos.vehiculos
        }
    ];

    return (
        <Box>
            <Typography
                variant="h4"
                fontWeight="bold"
                gutterBottom
            >
                Dashboard
            </Typography>
            <Typography
                color="text.secondary"
                sx={{ mb: 3 }}
            >
                Bienvenido a GASPRYMAR IMS
            </Typography>
            {error && (
                <Alert
                    severity="error"
                    sx={{ mb: 3 }}
                >
                    {error}
                </Alert>
            )}
            {cargando ? (
                <Box
                    sx={{
                        display: "flex",
                        justifyContent: "center",
                        mt: 5
                    }}
                >
                    <CircularProgress />
                </Box>
            ) : (
                <Grid container spacing={3}>
                    {tarjetas.map((tarjeta) => (
                        <Grid
                            item
                            xs={12}
                            sm={6}
                            md={4}
                            key={tarjeta.titulo}
                        >
                            <Paper
                                elevation={2}
                                sx={{
                                    p: 3,
                                    borderRadius: 3
                                }}
                            >
                                <Typography
                                    color="text.secondary"
                                    variant="body2"
                                >
                                    {tarjeta.titulo}
                                </Typography>
                                <Typography
                                    variant="h3"
                                    fontWeight="bold"
                                    sx={{ mt: 1 }}
                                >
                                    {tarjeta.cantidad}
                                </Typography>
                            </Paper>
                        </Grid>
                    ))}
                </Grid>
            )}
        </Box>
    );
}