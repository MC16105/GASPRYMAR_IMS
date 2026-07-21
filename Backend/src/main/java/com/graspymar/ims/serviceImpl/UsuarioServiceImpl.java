package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.UsuarioRequest;
import com.graspymar.ims.dto.UsuarioResponse;
import com.graspymar.ims.entity.Rol;
import com.graspymar.ims.entity.Usuario;
import com.graspymar.ims.repository.RolRepository;
import com.graspymar.ims.repository.UsuarioRepository;
import com.graspymar.ims.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya existe"); }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya existe"); }

        Rol rol = rolRepository.findById(request.getRolId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        Usuario usuario = new Usuario();

        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword())); // Después será BCrypt
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setCorreo(request.getCorreo());
        usuario.setRol(rol);

        usuario = usuarioRepository.save(usuario);

        UsuarioResponse response = new UsuarioResponse();

        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol().getNombre().name());

        return response;
    } 

    @Override
    public List<UsuarioResponse> listarUsuarios() {

    return usuarioRepository.findAll().stream().map(usuario -> {
                UsuarioResponse response = new UsuarioResponse();
                response.setId(usuario.getId());
                response.setUsername(usuario.getUsername());
                response.setNombreCompleto(usuario.getNombreCompleto());
                response.setCorreo(usuario.getCorreo());
                response.setRol(usuario.getRol().getNombre().name());
                return response;
            }).toList();
    }

    @Override
    public UsuarioResponse obtenerUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    UsuarioResponse response = new UsuarioResponse();
    response.setId(usuario.getId());
    response.setUsername(usuario.getUsername());
    response.setNombreCompleto(usuario.getNombreCompleto());
    response.setCorreo(usuario.getCorreo());
    response.setRol(usuario.getRol().getNombre().name());
    return response;
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    if (!usuario.getUsername().equals(request.getUsername())
            && usuarioRepository.existsByUsername(request.getUsername())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El username ya existe");
    }

    if (!usuario.getCorreo().equals(request.getCorreo())
            && usuarioRepository.existsByCorreo(request.getCorreo())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya existe");
    }

    Rol rol = rolRepository.findById(request.getRolId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

    usuario.setUsername(request.getUsername());
    usuario.setNombreCompleto(request.getNombreCompleto());
    usuario.setCorreo(request.getCorreo());

    if (request.getPassword() != null &&
            !request.getPassword().isBlank()) {
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    usuario.setRol(rol);
    usuarioRepository.save(usuario);
    UsuarioResponse response = new UsuarioResponse();
    response.setId(usuario.getId());
    response.setUsername(usuario.getUsername());
    response.setNombreCompleto(usuario.getNombreCompleto());
    response.setCorreo(usuario.getCorreo());
    response.setRol(usuario.getRol().getNombre().name());
    return response;
    }

    @Override
    public void eliminarUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    usuario.setActivo(false);
    usuarioRepository.save(usuario);
}

}
