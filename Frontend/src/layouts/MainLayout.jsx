import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import { Outlet } from "react-router-dom";
import { Box } from "@mui/material";
const drawerWidth = 260;
export default function MainLayout() {
    return (
        <Box sx={{ display: "flex", height: "50vh", overflow: "hidden" }}>
            {/* Sidebar lateral fijo */}
            <Box
                component="nav"
                sx={{ width: { sm: drawerWidth },
                    flexShrink: { sm: 0 }  }}
            >
                <Sidebar />
            </Box>

            {/* Contenedor principal */}
            <Box

            >
                {/* Navbar superior */}
                <Navbar />

                {/* Área de contenido dinámico (Dashboard, etc.) */}
                <Box
                    component="main"
                    sx={{
                        flexGrow: 1,
                        p: 3,
                        overflowY: "auto",
                        mt: "64px" // Altura estimada del Navbar para que no se traslape
                    }}
                >
                    <Outlet />
                </Box>
            </Box>
        </Box>
    );
}