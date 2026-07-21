package com.graspymar.ims.config;

import com.graspymar.ims.entity.Rol;
import com.graspymar.ims.enums.RolNombre;
import com.graspymar.ims.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {

        for (RolNombre nombre : RolNombre.values()) {

            if (rolRepository.findByNombre(nombre).isEmpty()) {

                Rol rol = new Rol();
                rol.setNombre(nombre);
                rol.setDescripcion("Rol " + nombre.name());

                rolRepository.save(rol);

            }

        }

    }
}