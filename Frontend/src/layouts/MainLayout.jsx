import { Box } from "@mui/material";
import { Outlet } from "react-router-dom";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";

export default function MainLayout() {
    return (
        <Box
            sx={{
                minHeight: "100vh",
                backgroundColor: "#f8fafc"
            }}
        >
            <Navbar />
            <Sidebar />
            <Box
                component="main"
                sx={{
                    marginLeft: "220px",
                    paddingTop: "90px",
                    paddingLeft: 3,
                    paddingRight: 3,
                    paddingBottom: 3,
                    minHeight: "100vh"
                }}
            >
                <Outlet />
            </Box>
        </Box>
    );
}