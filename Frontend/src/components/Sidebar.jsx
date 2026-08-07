import { Box, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Toolbar, Typography } from "@mui/material";
import { useNavigate, useLocation } from "react-router-dom";
import HomeIcon from "@mui/icons-material/Home";
import PersonIcon from "@mui/icons-material/Person";
import Inventory2Icon from "@mui/icons-material/Inventory2";
import LocalShippingIcon from "@mui/icons-material/LocalShipping";
import CategoryIcon from "@mui/icons-material/Category";
import FolderIcon from "@mui/icons-material/Folder";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import ReceiptIcon from "@mui/icons-material/Receipt";
import SecurityIcon from "@mui/icons-material/Security";

const logoGreen = "#1E5631";

const menuSections = [
    {
        title: "Principal",
        items: [
            { text: "Inicio", icon: <HomeIcon />, path: "/dashboard" },
            { text: "Perfil", icon: <PersonIcon />, path: "/dashboard/perfil" },
        ]
    },
    {
        title: "Catálogos",
        items: [
            { text: "Productos", icon: <Inventory2Icon />, path: "/dashboard/productos" },
            { text: "Categorías", icon: <CategoryIcon />, path: "/dashboard/categorias" },
            { text: "Proveedores", icon: <LocalShippingIcon />, path: "/dashboard/proveedores" },
        ]
    },
    {
        title: "Operaciones",
        items: [
            { text: "Inventario", icon: <FolderIcon />, path: "/dashboard/inventario" },
            { text: "Compras", icon: <ShoppingCartIcon />, path: "/dashboard/compras" },
            { text: "Ventas", icon: <ReceiptIcon />, path: "/dashboard/ventas" },
        ]
    },
    {
        title: "Seguridad",
        items: [
            { text: "Usuarios", icon: <SecurityIcon />, path: "/dashboard/usuarios" },
        ]
    }
];

export default function Sidebar() {
    const navigate = useNavigate();
    const location = useLocation();

    return (
        <Box
            component="nav"
            sx={{
                width: 190, // Ancho optimizado para que los textos no se apretujen
                backgroundColor: "#0f172a", // Fondo oscuro elegante
                color: "#ffffff",
                height: "100vh",
                position: "fixed",
                left: 0,
                top: "20px",
                display: "flex",
                flexDirection: "column",
                borderRight: "1px solid #1e293b",
                overflowY: "auto",
                flexShrink: 0,
                zIndex: (theme) => theme.zIndex.drawer,
            }}
        >
            <Toolbar /> {/* Espaciador automático para alinearse debajo de la Navbar */}

            {menuSections.map((section, index) => (
                <Box key={index} sx={{ px: 2, py: 1 }}>
                    <Typography
                        variant="caption"
                        sx={{
                            color: "#64748b",
                            fontWeight: 700,
                            letterSpacing: "0.05em",
                            px: 1,
                            textTransform: "uppercase",
                            fontSize: "0.7rem"
                        }}
                    >
                        {section.title}
                    </Typography>
                    <List sx={{ pt: 1, pb: 0 }}>
                        {section.items.map((item) => {
                            const isActive = location.pathname === item.path;

                            return (
                                <ListItem key={item.text} disablePadding sx={{ mb: 0.5 }}>
                                    <ListItemButton
                                        onClick={() => navigate(item.path)}
                                        sx={{
                                            display: "flex",
                                            alignItems: "center",
                                            gap: 1.5,
                                            py: 1,
                                            px: 2,
                                            borderRadius: 25,
                                            backgroundColor: isActive ? logoGreen : "transparent",
                                            "&:hover": {
                                                backgroundColor: isActive ? logoGreen : "rgba(255, 255, 255, 0.05)",
                                            },
                                        }}
                                    >
                                        <ListItemIcon
                                            sx={{
                                                minWidth: "auto",
                                                color: isActive ? "#ffffff" : "#94a3b8",
                                            }}
                                        >
                                            {item.icon}
                                        </ListItemIcon>
                                        <ListItemText
                                            primary={item.text}
                                            primaryTypographyProps={{
                                                fontSize: "0.85rem",
                                                fontWeight: isActive ? 600 : 500,
                                                color: isActive ? "#ffffff" : "#cbd5e1",
                                            }}
                                        />
                                    </ListItemButton>
                                </ListItem>
                            );
                        })}
                    </List>
                </Box>
            ))}
        </Box>
    );
}