import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";

const logoGreen = "#1E5631";
const obtenerSaludo = () => {
    const hora = new Date().getHours();

    if (hora >= 6 && hora < 12) {
        return "BUENOS DÍAS";
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
                {/* Logo y título */}
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 2
                    }}
                >
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{
                            fontWeight: 700
                        }}
                    >
                        GASPRYMAR IMS
                    </Typography>
                </Box>
                {/* Saludo */}
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center"
                    }}
                >
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{
                            fontWeight: 700
                        }}
                    >
                        {saludo}
                    </Typography>
                </Box>
                {/* Logout */}
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
                            px: 4,

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