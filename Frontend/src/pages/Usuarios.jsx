import { useEffect, useState } from "react";

import {
    Box,
    Typography,
    Paper,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    CircularProgress,
    Alert,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Tooltip,
    MenuItem,
    InputAdornment
} from "@mui/material";

import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";

import {
    listarUsuarios,
    crearUsuario,
    actualizarUsuario,
    eliminarUsuario
} from "../services/usuarioService";

import api from "../api/axiosConfig";


const logoGreen = "#1E5631";


const initialState = {
    username: "",
    password: "",
    nombreCompleto: "",
    correo: "",
    rolId: ""
};


export default function Usuarios() {

    const [usuarios, setUsuarios] = useState([]);
    const [roles, setRoles] = useState([]);

    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const [openModal, setOpenModal] = useState(false);

    const [usuarioActual, setUsuarioActual] =
        useState(initialState);

    const [isEditing, setIsEditing] =
        useState(false);

    const [mostrarPassword, setMostrarPassword] =
        useState(false);


    useEffect(() => {
        cargarDatos();
    }, []);


    const cargarDatos = async () => {

        try {

            setCargando(true);
            setError("");

            const [
                usuariosResponse,
                rolesResponse
            ] = await Promise.all([

                listarUsuarios(),

                api.get("/roles")

            ]);

            setUsuarios(usuariosResponse.data);

            setRoles(rolesResponse.data);

        } catch (err) {

            console.error(err);

            setError(
                "No se pudieron cargar los usuarios."
            );

        } finally {

            setCargando(false);

        }
    };


    const handleAbrirCrear = () => {

        setUsuarioActual(initialState);

        setIsEditing(false);

        setMostrarPassword(false);

        setOpenModal(true);
    };


    const handleAbrirEditar = (usuario) => {

        /*
         * UsuarioResponse NO devuelve password.
         *
         * También necesitamos encontrar el rolId
         * usando el nombre del rol recibido.
         */

        const rolEncontrado =
            roles.find(
                (rol) =>
                    rol.nombre === usuario.rol
            );

        setUsuarioActual({

            id: usuario.id,

            username:
                usuario.username ?? "",

            password: "",

            nombreCompleto:
                usuario.nombreCompleto ?? "",

            correo:
                usuario.correo ?? "",

            rolId:
                rolEncontrado?.id ?? ""

        });

        setIsEditing(true);

        setMostrarPassword(false);

        setOpenModal(true);
    };


    const handleCerrarModal = () => {

        setOpenModal(false);

        setUsuarioActual(initialState);

        setIsEditing(false);

        setMostrarPassword(false);
    };


    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            setError("");

            const payload = {

                username:
                usuarioActual.username,

                password:
                usuarioActual.password,

                nombreCompleto:
                usuarioActual.nombreCompleto,

                correo:
                usuarioActual.correo,

                rolId:
                    Number(usuarioActual.rolId)

            };


            if (isEditing) {

                await actualizarUsuario(
                    usuarioActual.id,
                    payload
                );

            } else {

                await crearUsuario(payload);

            }


            await cargarDatos();

            handleCerrarModal();

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data?.message ||
                "No se pudo guardar el usuario."
            );
        }
    };


    const handleEliminar = async (id) => {

        if (
            !window.confirm(
                "¿Deseas eliminar este usuario?"
            )
        ) {
            return;
        }

        try {

            await eliminarUsuario(id);

            await cargarDatos();

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data?.message ||
                "No se pudo eliminar el usuario."
            );

        }
    };


    return (

        <Box sx={{ p: 1 }}>


            {/* CABECERA */}

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 4,
                    backgroundColor: "#ffffff",
                    p: 3,
                    borderRadius: 4,
                    boxShadow:
                        "0px 2px 4px rgba(0,0,0,0.02)",
                    border:
                        "1px solid #f1f5f9"
                }}
            >

                <Box>

                    <Typography
                        variant="h5"
                        sx={{
                            fontWeight: 800,
                            color: "#1e293b",
                            letterSpacing: "-0.5px"
                        }}
                    >
                        Gestión de Usuarios
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            color: "#64748b",
                            mt: 0.5
                        }}
                    >
                        Administración de cuentas
                        y accesos de GASPRYMAR
                    </Typography>

                </Box>


                <Button
                    variant="outlined"
                    startIcon={<AddIcon />}
                    onClick={handleAbrirCrear}
                    sx={{
                        fontWeight: 600,
                        color: logoGreen,
                        borderColor: logoGreen,
                        backgroundColor: "#ffffff",
                        borderRadius: "10px",
                        px: 3,
                        py: 1,
                        textTransform: "none",

                        "&:hover": {
                            backgroundColor:
                                "rgba(30, 86, 49, 0.08)",
                            borderColor:
                            logoGreen
                        }
                    }}
                >
                    Nuevo Usuario
                </Button>

            </Box>


            {/* ERROR */}

            {error && (

                <Alert
                    severity="error"
                    sx={{
                        mb: 3,
                        borderRadius: 2
                    }}
                >
                    {error}
                </Alert>

            )}


            {/* TABLA */}

            <Paper
                elevation={0}
                sx={{
                    borderRadius: 4,
                    overflow: "hidden",
                    border:
                        "1px solid #e2e8f0",
                    boxShadow:
                        "0px 4px 20px rgba(0,0,0,0.03)"
                }}
            >

                {cargando ? (

                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                            p: 6
                        }}
                    >

                        <CircularProgress
                            sx={{
                                color: logoGreen
                            }}
                        />

                    </Box>

                ) : (

                    <TableContainer>

                        <Table sx={{ minWidth: 800 }}>

                            <TableHead
                                sx={{
                                    backgroundColor:
                                        "#f8fafc"
                                }}
                            >

                                <TableRow>

                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        ID
                                    </TableCell>

                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Usuario
                                    </TableCell>

                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Nombre Completo
                                    </TableCell>

                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Correo
                                    </TableCell>

                                    <TableCell
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Rol
                                    </TableCell>

                                    <TableCell
                                        align="center"
                                        sx={{
                                            fontWeight: 700
                                        }}
                                    >
                                        Acciones
                                    </TableCell>

                                </TableRow>

                            </TableHead>


                            <TableBody>

                                {usuarios.map(
                                    (usuario, index) => (

                                        <TableRow
                                            key={usuario.id}
                                            sx={{
                                                backgroundColor:
                                                    index % 2 === 0
                                                        ? "#ffffff"
                                                        : "#fcfdff",

                                                "&:hover": {
                                                    backgroundColor:
                                                        "#f1f5f9"
                                                }
                                            }}
                                        >

                                            <TableCell>
                                                #{usuario.id}
                                            </TableCell>


                                            <TableCell
                                                sx={{
                                                    fontWeight: 700,
                                                    color: "#1e293b"
                                                }}
                                            >
                                                {usuario.username}
                                            </TableCell>


                                            <TableCell>
                                                {usuario.nombreCompleto}
                                            </TableCell>


                                            <TableCell>
                                                {usuario.correo}
                                            </TableCell>


                                            <TableCell>

                                                <Box
                                                    component="span"
                                                    sx={{
                                                        px: 1.5,
                                                        py: 0.5,
                                                        borderRadius: 10,
                                                        backgroundColor:
                                                            "rgba(30,86,49,0.08)",
                                                        color:
                                                        logoGreen,
                                                        fontWeight: 700,
                                                        fontSize:
                                                            "0.8rem"
                                                    }}
                                                >
                                                    {usuario.rol}
                                                </Box>

                                            </TableCell>


                                            <TableCell align="center">

                                                <Tooltip title="Editar">

                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleAbrirEditar(
                                                                usuario
                                                            )
                                                        }
                                                        sx={{
                                                            color:
                                                                "#3b82f6",
                                                            backgroundColor:
                                                                "rgba(59,130,246,0.08)",
                                                            mr: 1
                                                        }}
                                                    >

                                                        <EditIcon
                                                            fontSize="small"
                                                        />

                                                    </IconButton>

                                                </Tooltip>


                                                <Tooltip title="Eliminar">

                                                    <IconButton
                                                        size="small"
                                                        onClick={() =>
                                                            handleEliminar(
                                                                usuario.id
                                                            )
                                                        }
                                                        sx={{
                                                            color:
                                                                "#ef4444",
                                                            backgroundColor:
                                                                "rgba(239,68,68,0.08)"
                                                        }}
                                                    >

                                                        <DeleteIcon
                                                            fontSize="small"
                                                        />

                                                    </IconButton>

                                                </Tooltip>

                                            </TableCell>

                                        </TableRow>

                                    )
                                )}


                                {usuarios.length === 0 && (

                                    <TableRow>

                                        <TableCell
                                            colSpan={6}
                                            align="center"
                                            sx={{
                                                py: 6,
                                                color:
                                                    "#94a3b8"
                                            }}
                                        >
                                            No hay usuarios registrados.
                                        </TableCell>

                                    </TableRow>

                                )}

                            </TableBody>

                        </Table>

                    </TableContainer>

                )}

            </Paper>


            {/* MODAL */}

            <Dialog
                open={openModal}
                onClose={handleCerrarModal}
                fullWidth
                maxWidth="sm"
                PaperProps={{
                    sx: {
                        borderRadius: 4,
                        p: 1
                    }
                }}
            >

                <form onSubmit={handleSubmit}>

                    <DialogTitle
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            gap: 1.5,
                            pb: 1,
                            pt: 2,
                            px: 3
                        }}
                    >

                        <PersonAddIcon
                            sx={{
                                color: logoGreen
                            }}
                        />

                        <Typography
                            variant="h6"
                            sx={{
                                fontWeight: 700,
                                color: "#1e293b"
                            }}
                        >
                            {isEditing
                                ? "Editar Usuario"
                                : "Registrar Nuevo Usuario"}
                        </Typography>

                    </DialogTitle>


                    <DialogContent
                        sx={{
                            px: 3,
                            py: 2
                        }}
                    >

                        <Box
                            sx={{
                                display: "flex",
                                flexDirection: "column",
                                gap: 2.5,
                                pt: 1
                            }}
                        >

                            {/* USERNAME */}

                            <TextField
                                label="Usuario"
                                fullWidth
                                required
                                value={
                                    usuarioActual.username
                                }
                                onChange={(e) =>
                                    setUsuarioActual({
                                        ...usuarioActual,
                                        username:
                                        e.target.value
                                    })
                                }
                            />


                            {/* NOMBRE */}

                            <TextField
                                label="Nombre Completo"
                                fullWidth
                                required
                                value={
                                    usuarioActual.nombreCompleto
                                }
                                onChange={(e) =>
                                    setUsuarioActual({
                                        ...usuarioActual,
                                        nombreCompleto:
                                        e.target.value
                                    })
                                }
                            />


                            {/* CORREO */}

                            <TextField
                                label="Correo Electrónico"
                                type="email"
                                fullWidth
                                required
                                value={
                                    usuarioActual.correo
                                }
                                onChange={(e) =>
                                    setUsuarioActual({
                                        ...usuarioActual,
                                        correo:
                                        e.target.value
                                    })
                                }
                            />


                            {/* ROL */}

                            <TextField
                                select
                                label="Rol"
                                fullWidth
                                required
                                value={
                                    usuarioActual.rolId
                                }
                                onChange={(e) =>
                                    setUsuarioActual({
                                        ...usuarioActual,
                                        rolId:
                                        e.target.value
                                    })
                                }
                            >

                                {roles.map(
                                    (rol) => (

                                        <MenuItem
                                            key={rol.id}
                                            value={rol.id}
                                        >
                                            {rol.nombre}
                                        </MenuItem>

                                    )
                                )}

                            </TextField>


                            {/* PASSWORD */}

                            <TextField
                                label={
                                    isEditing
                                        ? "Nueva Contraseña"
                                        : "Contraseña"
                                }
                                type={
                                    mostrarPassword
                                        ? "text"
                                        : "password"
                                }
                                fullWidth
                                required={!isEditing}
                                value={
                                    usuarioActual.password
                                }
                                onChange={(e) =>
                                    setUsuarioActual({
                                        ...usuarioActual,
                                        password:
                                        e.target.value
                                    })
                                }
                                helperText={
                                    isEditing
                                        ? "Déjala vacía si no deseas cambiarla."
                                        : ""
                                }
                                InputProps={{
                                    endAdornment: (

                                        <InputAdornment position="end">

                                            <IconButton
                                                onClick={() =>
                                                    setMostrarPassword(
                                                        !mostrarPassword
                                                    )
                                                }
                                                edge="end"
                                            >

                                                {mostrarPassword
                                                    ? <VisibilityOffIcon />
                                                    : <VisibilityIcon />
                                                }

                                            </IconButton>

                                        </InputAdornment>

                                    )
                                }}
                            />

                        </Box>

                    </DialogContent>


                    <DialogActions
                        sx={{
                            px: 3,
                            pb: 3,
                            pt: 1
                        }}
                    >

                        <Button
                            onClick={
                                handleCerrarModal
                            }
                            sx={{
                                color: "#64748b",
                                textTransform: "none",
                                fontWeight: 600
                            }}
                        >
                            Cancelar
                        </Button>


                        <Button
                            type="submit"
                            variant="outlined"
                            sx={{
                                fontWeight: 600,
                                color: logoGreen,
                                borderColor:
                                logoGreen,
                                borderRadius:
                                    "10px",
                                px: 3,
                                textTransform:
                                    "none",

                                "&:hover": {
                                    backgroundColor:
                                        "rgba(30,86,49,0.08)",
                                    borderColor:
                                    logoGreen
                                }
                            }}
                        >
                            {isEditing
                                ? "Actualizar"
                                : "Guardar Usuario"}
                        </Button>

                    </DialogActions>

                </form>

            </Dialog>

        </Box>
    );
}