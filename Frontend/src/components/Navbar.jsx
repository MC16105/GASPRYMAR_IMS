import { AppBar, Toolbar, Typography, Button, Box, Avatar } from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import WbSunnyRoundedIcon from "@mui/icons-material/WbSunnyRounded";
import WbTwilightRoundedIcon from "@mui/icons-material/WbTwilightRounded";
import NightsStayRoundedIcon from "@mui/icons-material/NightsStayRounded";
import { useNavigate } from "react-router-dom";

const logoGreen = "#1E5631";

const obtenerSaludoYIcono = () => {
    const hora = new Date().getHours();

    if (hora >= 6 && hora < 12) {
        return { texto: "BUENOS DÍAS", icono: <WbSunnyRoundedIcon sx={{ fontSize: 20, color: "#f39c12" }} /> };
    } else if (hora >= 12 && hora < 19) {
        return { texto: "BUENAS TARDES", icono: <WbTwilightRoundedIcon sx={{ fontSize: 20, color: "#e67e22" }} /> };
    } else {
        return { texto: "BUENAS NOCHES", icono: <NightsStayRoundedIcon sx={{ fontSize: 20, color: "#34495e" }} /> };
    }
};

export default function Navbar() {
    const navigate = useNavigate();

    // Obtenemos el nombre del localStorage (si existe)
    const nombreCompleto = localStorage.getItem("nombre_completo") || "Usuario";
    const primerNombre = nombreCompleto.split(" ")[0]; // Extrae el primer nombre para estilizar mejor

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("nombre_completo");
        navigate("/");
    };

    const { texto: saludo, icono: iconoSaludo } = obtenerSaludoYIcono();

    return (
        <AppBar
            position="fixed"
            elevation={0}
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                backgroundColor: "#ffffff",
                color: logoGreen,
                boxShadow: "0px 4px 20px rgba(0, 0, 0, 0.05)",
                borderBottom: `3px solid ${logoGreen}`,
            }}
        >
            <Toolbar sx={{ justifyContent: "space-between", px: { xs: 2, md: 4 }, minHeight: "70px" }}>

                {/* Logo y título */}
                <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                    <Box
                        sx={{
                            width: 10,
                            height: 28,
                            backgroundColor: logoGreen,
                            borderRadius: "4px"
                        }}
                    />
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{
                            fontWeight: 800,
                            letterSpacing: "-0.5px",
                            fontSize: { xs: "1rem", md: "1.25rem" }
                        }}
                    >
                        GASPRYMAR <Box component="span" sx={{ fontWeight: 400, opacity: 0.8 }}>IMS</Box>
                    </Typography>
                </Box>

                {/* Saludo dinámico y Usuario */}
                <Box
                    sx={{
                        display: { xs: "none", sm: "flex" },
                        alignItems: "center",
                        gap: 1.5,
                        backgroundColor: "rgba(30, 86, 49, 0.04)",
                        py: 0.75,
                        px: 2,
                        borderRadius: "50px",
                        border: "1px solid rgba(30, 86, 49, 0.08)"
                    }}
                >
                    {iconoSaludo}
                    <Box sx={{ display: "flex", flexDirection: "column" }}>
                        <Typography
                            variant="caption"
                            sx={{
                                fontWeight: 700,
                                color: logoGreen,
                                lineHeight: 1.1,
                                letterSpacing: "0.5px"
                            }}
                        >
                            {saludo}, {primerNombre.toUpperCase()}
                        </Typography>
                    </Box>
                </Box>

                {/* Botón de Logout */}
                <Box>
                    <Button
                        variant="outlined"
                        onClick={handleLogout}
                        startIcon={<LogoutIcon />}
                        sx={{
                            fontWeight: 600,
                            color: logoGreen,
                            borderColor: "rgba(30, 86, 49, 0.3)",
                            backgroundColor: "#ffffff",
                            borderRadius: "50px",
                            py: 0.8,
                            px: 3,
                            fontSize: "0.85rem",
                            transition: "all 0.2s ease-in-out",
                            "&:hover": {
                                backgroundColor: "rgba(30, 86, 49, 0.08)",
                                borderColor: logoGreen,
                                boxShadow: "0px 2px 8px rgba(30, 86, 49, 0.15)",
                                transform: "translateY(-1px)"
                            }
                        }}
                    >
                        SALIR
                    </Button>
                </Box>
            </Toolbar>
        </AppBar>
    );
}