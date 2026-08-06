package com.graspymar.ims.controller;

import com.graspymar.ims.dto.VehiculoRequestDTO;
import com.graspymar.ims.dto.VehiculoResponseDTO;
import com.graspymar.ims.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public VehiculoResponseDTO crear(@Valid @RequestBody VehiculoRequestDTO dto) {
        return vehiculoService.crear(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<VehiculoResponseDTO> listar() {
        return vehiculoService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public VehiculoResponseDTO buscarPorId(@PathVariable Long id) {
        return vehiculoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public VehiculoResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO dto) {
        return vehiculoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
    }
}
