import { useState } from "react";
import { login } from "../services/authService";
import { useNavigate } from "react-router-dom";
import { Box, Button, Container, Paper, TextField, Typography, Alert, CircularProgress, Link, InputAdornment } from "@mui/material";
import Logo from "../logos/GCF.png";
import PersonIcon from "@mui/icons-material/Person";
import LockIcon from "@mui/icons-material/Lock";

const logoGreen = "#1E5631";

export default function Login() {
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(false);
        setLoading(true);

        try {
            const respuesta = await login({ username, password });

            localStorage.setItem("token", respuesta.token);
            if (respuesta.nombre_completo) {
                localStorage.setItem("nombre_completo", respuesta.nombre_completo);
            } else if (respuesta.user?.nombre_completo) {
                localStorage.setItem("nombre_completo", respuesta.user.nombre_completo);
            }

            navigate("/dashboard");
        } catch (err) {
            setError(true);
        } finally {
            setLoading(false);
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
                // Fondo con tonos acordes verde corporativo y una imagen sutil
                backgroundImage: `linear-gradient(rgba(15, 23, 42, 0.75), rgba(30, 86, 49, 0.85)), url(https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1920&auto=format&fit=crop)`,
                backgroundSize: "cover",
                backgroundPosition: "center",
                position: "fixed",
                top: 0,
                left: 0,
            }}
        >
            <Container component="main" maxWidth="xs">
                <Paper
                    elevation={24}
                    sx={{
                        padding: { xs: 3, sm: 4.5 },
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        width: "100%",
                        borderRadius: "28px",
                        backgroundColor: "rgba(255, 255, 255, 0.12)",
                        backdropFilter: "blur(16px)",
                        WebkitBackdropFilter: "blur(16px)",
                        border: "1px solid rgba(255, 255, 255, 0.25)",
                        boxShadow: "0px 25px 50px rgba(0, 0, 0, 0.4)",
                        color: "#ffffff"
                    }}
                >
                    {Logo ? (
                        <Box
                            component="img"
                            src={Logo}
                            alt="Logo GASPRYMAR"
                            sx={{
                                height: 75,
                                width: "auto",
                                objectFit: "contain",
                                mb: 1.5,
                                filter: "drop-shadow(0px 4px 8px rgba(0,0,0,0.3))"
                            }}
                        />
                    ) : null}

                    <Typography
                        component="h1"
                        variant="h4"
                        sx={{
                            fontWeight: 700,
                            mb: 3,
                            letterSpacing: "0.5px",
                            color: "#ffffff",
                            textAlign: "center"
                        }}
                    >
                        GASPRYMAR IMS
                    </Typography>

                    {error && (
                        <Alert
                            severity="error"
                            sx={{
                                width: "100%",
                                mb: 2,
                                borderRadius: "12px",
                                fontSize: "0.85rem",
                                backgroundColor: "rgba(211, 47, 47, 0.9)",
                                color: "#fff"
                            }}
                        >
                            Usuario o contraseña incorrectos
                        </Alert>
                    )}

                    <Box component="form" onSubmit={handleSubmit} sx={{ width: "100%" }}>
                        {/* Campo Usuario */}
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="username"
                            placeholder="Username"
                            name="username"
                            autoComplete="username"
                            autoFocus
                            disabled={loading}
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            InputProps={{
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <PersonIcon sx={{ color: "rgba(255, 255, 255, 0.7)" }} />
                                    </InputAdornment>
                                ),
                            }}
                            sx={{
                                "& .MuiOutlinedInput-root": {
                                    borderRadius: "50px",
                                    backgroundColor: "rgba(255, 255, 255, 0.08)",
                                    color: "#ffffff",
                                    "& fieldset": { borderColor: "rgba(255, 255, 255, 0.3)" },
                                    "&:hover fieldset": { borderColor: "rgba(255, 255, 255, 0.6)" },
                                    "&.Mui-focused fieldset": { borderColor: "#ffffff", borderWidth: "1.5px" }
                                },
                                "& .MuiInputBase-input::placeholder": {
                                    color: "rgba(255, 255, 255, 0.6)",
                                    opacity: 1
                                }
                            }}
                        />

                        {/* Campo Contraseña */}
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            placeholder="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            disabled={loading}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            InputProps={{
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <LockIcon sx={{ color: "rgba(255, 255, 255, 0.7)" }} />
                                    </InputAdornment>
                                ),
                            }}
                            sx={{
                                "& .MuiOutlinedInput-root": {
                                    borderRadius: "50px",
                                    backgroundColor: "rgba(255, 255, 255, 0.08)",
                                    color: "#ffffff",
                                    "& fieldset": { borderColor: "rgba(255, 255, 255, 0.3)" },
                                    "&:hover fieldset": { borderColor: "rgba(255, 255, 255, 0.6)" },
                                    "&.Mui-focused fieldset": { borderColor: "#ffffff", borderWidth: "1.5px" }
                                },
                                "& .MuiInputBase-input::placeholder": {
                                    color: "rgba(255, 255, 255, 0.6)",
                                    opacity: 1
                                }
                            }}
                        />

                        {/* Enlace de recuperar contraseña alineado a la derecha */}
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                                mt: 1.5,
                                mb: 2,
                                px: 1,
                                fontSize: "1rem",
                                letterSpacing: "0.5px",
                                fontWeight: 700,

                            }}
                        >
                            --------------------- ACCEDER --------------------

                        </Box>

                        {/* Botón Principal Estilo Píldora */}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            disabled={loading}
                            sx={{
                                mt: 1,
                                mb: 3,
                                py: 2,
                                fontWeight: 700,
                                fontSize: "0.95rem",
                                letterSpacing: "0.5px",
                                bgcolor: "#066024",
                                color: "#ffffff",
                                borderRadius: "50px",
                                boxShadow: "0px 8px 20px rgba(0, 0, 0, 0.2)",
                                transition: "all 0.2s ease-in-out",
                                "&:hover": {
                                    bgcolor: "rgba(255, 255, 255, 0.9)",
                                    boxShadow: "0px 10px 25px rgba(0, 0, 0, 0.3)",
                                    transform: "translateY(-1px)"
                                }
                            }}
                        >
                            {loading ? (
                                <CircularProgress size={24} sx={{ color: "#1e1e1e" }} />
                            ) : (
                                "INICIAR SESION"
                            )}
                        </Button>

                        </Box>
                </Paper>
            </Container>
        </Box>
    );
}