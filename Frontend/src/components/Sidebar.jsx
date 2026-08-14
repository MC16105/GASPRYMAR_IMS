import {Box, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Toolbar, Typography
} from "@mui/material";

import {useNavigate, useLocation } from "react-router-dom";

import HomeIcon from "@mui/icons-material/Home";
import PersonIcon from "@mui/icons-material/Person";
import Inventory2Icon from "@mui/icons-material/Inventory2";
import LocalShippingIcon from "@mui/icons-material/LocalShipping";
import CategoryIcon from "@mui/icons-material/Category";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import ReceiptIcon from "@mui/icons-material/Receipt";
import SecurityIcon from "@mui/icons-material/Security";
import PrecisionManufacturingIcon from "@mui/icons-material/PrecisionManufacturing";
import WaterIcon from "@mui/icons-material/Water";
import LocalGasStationIcon from "@mui/icons-material/LocalGasStation";

const logoGreen = "#1E5631";
const menuSections = [
    {
        title: "Principal",
        items: [
            {
                text: "Inicio",
                icon: <HomeIcon />,
                path: "/dashboard"
            }
        ]
    },
    {
        title: "Catálogos",
        items: [
            {
                text: "Clientes",
                icon: <PersonIcon />,
                path: "/dashboard/clientes"
            },
            {
                text: "Proveedores",
                icon: <LocalShippingIcon />,
                path: "/dashboard/proveedores"
            },
            {
                text: "Insumos",
                icon: <Inventory2Icon />,
                path: "/dashboard/insumos"
            },
            {
                text: "Productos",
                icon: <CategoryIcon />,
                path: "/dashboard/productos"
            },
            {
                text: "Vehículos",
                icon: <LocalShippingIcon />,
                path: "/dashboard/vehiculos"
            },
            {
                text: "Estanques",
                icon: <WaterIcon />,
                path: "/dashboard/estanques"
            }
        ]
    },
    {
        title: "Operaciones",
        items: [
            {
                text: "Compras",
                icon: <ShoppingCartIcon />,
                path: "/dashboard/compras"
            },
            {
                text: "Ventas",
                icon: <ReceiptIcon />,
                path: "/dashboard/ventas"
            },
            {
                text: "Producción",
                icon: <Inventory2Icon />,
                path: "/dashboard/produccion"
            },
            {
                text: "Control de Estanques",
                icon: <WaterIcon />,
                path: "/dashboard/control-estanques"
            },
            {
                text: "Carga de Combustible",
                icon: <LocalGasStationIcon />,
                path: "/dashboard/carga-combustible"
            },
            {
                text: "Maquinarias",
                icon: <PrecisionManufacturingIcon />,
                path: "/dashboard/maquinarias"
            }
        ]
    },
    {
        title: "Seguridad",
        items: [
            {
                text: "Usuarios",
                icon: <SecurityIcon />,
                path: "/dashboard/usuarios"
            }
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
                width: 220,
                backgroundColor: "#0f172a",
                color: "#ffffff",
                height: "100vh",
                position: "fixed",
                left: 0,
                top: 0,
                display: "flex",
                flexDirection: "column",
                borderRight: "1px solid #1e293b",
                overflowY: "auto",
                flexShrink: 0,
                zIndex: (theme) => theme.zIndex.drawer,
            }}
        >
            {/* Espacio para Navbar */}
            <Toolbar />
            {menuSections.map((section) => (
                <Box
                    key={section.title}
                    sx={{
                        px: 2,
                        py: 1
                    }}
                >
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
                    <List
                        sx={{
                            pt: 1,
                            pb: 0
                        }}
                    >
                        {section.items.map((item) => {
                            const isActive =
                                location.pathname === item.path;
                            return (
                                <ListItem
                                    key={item.text}
                                    disablePadding
                                    sx={{
                                        mb: 0.5
                                    }}
                                >
                                    <ListItemButton
                                        onClick={() => navigate(item.path)}
                                        sx={{
                                            display: "flex",
                                            alignItems: "center",
                                            gap: 1.5,
                                            py: 1,
                                            px: 2,
                                            borderRadius: 25,

                                            backgroundColor:
                                                isActive
                                                    ? logoGreen
                                                    : "transparent",

                                            "&:hover": {
                                                backgroundColor:
                                                    isActive
                                                        ? logoGreen
                                                        : "rgba(255, 255, 255, 0.05)"
                                            }
                                        }}
                                    >
                                        <ListItemIcon
                                            sx={{
                                                minWidth: "auto",

                                                color:
                                                    isActive
                                                        ? "#ffffff"
                                                        : "#94a3b8"
                                            }}
                                        >
                                            {item.icon}
                                        </ListItemIcon>

                                        <ListItemText
                                            primary={item.text}
                                            primaryTypographyProps={{
                                                fontSize: "0.85rem",
                                                fontWeight:
                                                    isActive
                                                        ? 600
                                                        : 500,
                                                color:
                                                    isActive
                                                        ? "#ffffff"
                                                        : "#cbd5e1"
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