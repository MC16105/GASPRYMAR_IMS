import { useState } from "react";
import { login } from "../services/authService";
import { useNavigate } from "react-router-dom";
import { Box, Button, Container, Paper, TextField, Typography, Alert } from "@mui/material";
import Logo from "../logos/GCF.png";

// Color verde corporativo
const logoGreen = "#1E5631";

export default function Login() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(false);
        try {
            const respuesta = await login({ username, password });
            localStorage.setItem("token", respuesta.token);
            navigate("/dashboard");
        } catch (err) {
            setError(true);
        }
    };

    return (
        <Box
            sx={{
                height: "100vh",
                width: "100vw",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1920&auto=format&fit=crop)`,
                backgroundSize: "cover",
                backgroundPosition: "center",
                position: "fixed",
                top: 0,
                left: 0,
            }}
        >
            <Container component="main" maxWidth="xs">
                <Paper
                    elevation={6}
                    sx={{
                        padding: 4,
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        width: "100%",
                        borderRadius: 3,
                    }}
                >
                    {/* Avatar con tu color verde */}
                    <Box
                        component="img"
                        src={Logo}
                        alt="Logo"
                        sx={{
                            height: 300,
                            width: "auto",
                            objectFit: "contain"
                        }}
                    />

                    {error && (
                        <Alert severity="error" sx={{ width: "100%", mb: 2 }}>
                            Usuario o contraseña incorrectos
                        </Alert>
                    )}

                    <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1, width: "100%" }}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="username"
                            label="Usuario"
                            name="username"
                            autoComplete="username"
                            autoFocus
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            sx={{
                                "& label.Mui-focused": { color: logoGreen },
                                "& .MuiOutlinedInput-root": { "&.Mui-focused fieldset": { borderColor: logoGreen } }
                            }}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Contraseña"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            sx={{
                                "& label.Mui-focused": { color: logoGreen },
                                "& .MuiOutlinedInput-root": { "&.Mui-focused fieldset": { borderColor: logoGreen } }
                            }}
                        />

                        {/* Botón de Login */}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{
                                mt: 3,
                                mb: 2,
                                py: 1.5,
                                px: 0.1,
                                fontWeight: "bold",
                                bgcolor: logoGreen,
                                borderRadius: "50px",
                                "&:hover": { bgcolor: "#164526" }
                            }}
                        >
                            Iniciar Sesión
                        </Button>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
}