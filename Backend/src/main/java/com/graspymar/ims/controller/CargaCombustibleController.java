package com.graspymar.ims.controller;

import com.graspymar.ims.dto.CargaCombustibleRequestDTO;
import com.graspymar.ims.dto.CargaCombustibleResponseDTO;
import com.graspymar.ims.service.CargaCombustibleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carga_combustibles")
@RequiredArgsConstructor
public class CargaCombustibleController {

    private final CargaCombustibleService cargaCombustibleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public CargaCombustibleResponseDTO crear(@Valid @RequestBody CargaCombustibleRequestDTO dto) {
        return cargaCombustibleService.crear(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<CargaCombustibleResponseDTO> listar() {
        return cargaCombustibleService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public CargaCombustibleResponseDTO buscarPorId(@PathVariable Long id) {
        return cargaCombustibleService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public CargaCombustibleResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CargaCombustibleRequestDTO dto) {
        return cargaCombustibleService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        cargaCombustibleService.eliminar(id);
    }
}
