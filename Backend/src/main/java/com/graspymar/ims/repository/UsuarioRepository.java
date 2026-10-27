package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Usuario;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    List<Usuario> findByActivoTrue();

}
