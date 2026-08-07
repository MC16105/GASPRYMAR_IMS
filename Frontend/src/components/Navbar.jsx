import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";
import Logo from "../logos/GCF.png";

// Tu color verde corporativo
const logoGreen = "#1E5631";

// Función auxiliar para obtener el saludo según la hora actual
const obtenerSaludo = () => {
    const hora = new Date().getHours();
    if (hora >= 6 && hora < 12) {
        return "BUENOS DIAS ";
    } else if (hora >= 12 && hora < 19) {
        return "BUENAS TARDES";
    } else {
        return "BUENAS NOCHES";
    }
};

export default function Navbar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("nombre_completo");
        navigate("/");
    };

    const saludo = obtenerSaludo();

    return (
        <AppBar
            position="fixed"
            sx={{
                zIndex: (theme) => theme.zIndex.drawer + 1,
                backgroundColor: "#ffffff",
                color: logoGreen,
                boxShadow: "0px 1px 3px rgba(0, 0, 0, 0.1)",
                borderBottom: `5px solid ${logoGreen}`,
            }}
        >
            <Toolbar sx={{ justifyContent: "space-between" }}>
                {/* Contenedor del Logo y Título */}
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                    <Box
                        component="img"
                        src={Logo}
                        alt="Logo"
                        sx={{
                            height: 80,
                            width: "auto",
                            objectFit: "contain"
                        }}
                    />
                    <Typography variant="h6" noWrap component="div" sx={{ fontWeight: 700 }}>
                        GRASPYMAR IMS
                    </Typography>
                </Box>

                {/* Saludo dinámico y nombre */}
                <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Typography variant="h6" noWrap component="div" sx={{ fontWeight: 700 }}>
                        {saludo}
                    </Typography>
                </Box>

                {/* Botón de Salir */}
                <Box>
                    <Button
                        variant="outlined"
                        onClick={handleLogout}
                        startIcon={<LogoutIcon />}
                        sx={{
                            fontWeight: 600,
                            color: logoGreen,
                            borderColor: logoGreen,
                            backgroundColor: "#ffffff",
                            borderRadius: "50px",
                            py: 1,
                            px: 5,
                            "&:hover": {
                                backgroundColor: "rgba(30, 86, 49, 0.08)",
                                borderColor: logoGreen,
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