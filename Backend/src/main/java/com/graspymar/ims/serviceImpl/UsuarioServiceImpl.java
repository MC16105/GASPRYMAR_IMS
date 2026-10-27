package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.UsuarioRequest;
import com.graspymar.ims.dto.UsuarioResponse;
import com.graspymar.ims.entity.Rol;
import com.graspymar.ims.entity.Usuario;
import com.graspymar.ims.repository.RolRepository;
import com.graspymar.ims.repository.UsuarioRepository;
import com.graspymar.ims.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // =====================================================
    // CREAR USUARIO
    // =====================================================

    @Override
    @Transactional
    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        if (request.getPassword() == null ||
                request.getPassword().isBlank() ||
                request.getPassword().length() < 8) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El username ya existe"
            );
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo ya existe"
            );
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Rol no encontrado"
                        )
                );

        // No asignar roles inactivos
        if (!Boolean.TRUE.equals(rol.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede asignar un rol inactivo"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setUsername(request.getUsername());
        usuario.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setCorreo(request.getCorreo());
        usuario.setRol(rol);

        usuario = usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    // =====================================================
    // LISTAR USUARIOS ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .filter(usuario ->
                        Boolean.TRUE.equals(usuario.getActivo())
                )
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // OBTENER USUARIO POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El usuario no se encuentra activo"
            );
        }

        return toResponse(usuario);
    }

    // =====================================================
    // ACTUALIZAR USUARIO
    // =====================================================

    @Override
    @Transactional
    public UsuarioResponse actualizarUsuario(
            Long id,
            UsuarioRequest request
    ) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"
                        )
                );

        // Impedir modificaciones a usuarios inactivos
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede actualizar un usuario inactivo"
            );
        }

        // Verificar username duplicado
        if (!usuario.getUsername().equals(request.getUsername())
                && usuarioRepository.existsByUsername(
                request.getUsername()
        )) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El username ya existe"
            );
        }

        // Verificar correo duplicado
        if (!usuario.getCorreo().equals(request.getCorreo())
                && usuarioRepository.existsByCorreo(
                request.getCorreo()
        )) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo ya existe"
            );
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Rol no encontrado"
                        )
                );

        // No asignar roles inactivos
        if (!Boolean.TRUE.equals(rol.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede asignar un rol inactivo"
            );
        }

        // Validar nueva contraseña antes de modificar datos
        if (request.getPassword() != null &&
                !request.getPassword().isBlank() &&
                request.getPassword().length() < 8) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nueva contraseña debe tener al menos 8 caracteres"
            );
        }

        usuario.setUsername(request.getUsername());
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setCorreo(request.getCorreo());
        usuario.setRol(rol);

        // Actualizar contraseña solo cuando se proporciona
        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            usuario.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        usuario = usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    // =====================================================
    // ELIMINAR USUARIO (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario ya se encuentra inactivo"
            );
        }

        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private UsuarioResponse toResponse(Usuario usuario) {

        UsuarioResponse response = new UsuarioResponse();

        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol().getNombre().name());

        return response;
    }
}
