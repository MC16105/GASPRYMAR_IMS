package com.graspymar.ims.controller;

import com.graspymar.ims.dto.EstanqueRequestDTO;
import com.graspymar.ims.dto.EstanqueResponseDTO;
import com.graspymar.ims.service.EstanqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estanques")
@RequiredArgsConstructor
public class EstanqueController {

        private final EstanqueService estanqueService;

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
        public EstanqueResponseDTO crear(@Valid @RequestBody EstanqueRequestDTO dto) {
            return estanqueService.crear(dto);
        }

        @GetMapping
        @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
        public List<EstanqueResponseDTO> listar() {
            return estanqueService.listar();
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
        public EstanqueResponseDTO buscarPorId(@PathVariable Long id) {
            return estanqueService.buscarPorId(id);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
        public EstanqueResponseDTO actualizar(
                @PathVariable Long id,
                @Valid @RequestBody EstanqueRequestDTO dto) {
            return estanqueService.actualizar(id, dto);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public void eliminar(@PathVariable Long id) {
            estanqueService.eliminar(id);
        }

    }
