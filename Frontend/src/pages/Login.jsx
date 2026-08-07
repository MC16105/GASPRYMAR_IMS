import { useState } from "react";
import { login } from "../services/authService";
import api from "../api/axiosConfig.js";
function Login() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {

            const respuesta = await login({
                username,
                password
            });

            console.log(respuesta);

            localStorage.setItem("token", respuesta.token);

            alert("Login Correcto");

            // Prueba
            const clientes = await api.get("clientes");

            console.log(clientes.data);

        } catch (error) {

            alert("Usuario o contraseña incorrectos");
            console.error(error);

        }

    };

    return (
        <div>

            <h1>GRASPYMAR IMS</h1>

            <form onSubmit={handleSubmit}>

                <input
                    type="text"
                    placeholder="Usuario"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <br /><br />

                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <br /><br />

                <button type="submit">
                    Iniciar Sesión
                </button>

            </form>

        </div>
    );
}

export default Login;