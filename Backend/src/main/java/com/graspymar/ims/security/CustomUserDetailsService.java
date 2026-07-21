package com.graspymar.ims.security;

import com.graspymar.ims.entity.Usuario;
import com.graspymar.ims.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    System.out.println(">>> Intentando autenticar: " + username);

    Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    System.out.println(">>> Usuario encontrado");
    System.out.println("Password BD: " + usuario.getPassword());

    return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .authorities(List.of(
                    new SimpleGrantedAuthority(
                            "ROLE_" + usuario.getRol().getNombre().name())))
            .disabled(!usuario.getActivo())
            .build();
}

}
