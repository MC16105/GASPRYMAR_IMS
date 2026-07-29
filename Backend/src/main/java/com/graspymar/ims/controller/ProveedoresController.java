package com.graspymar.ims.controller;

import com.graspymar.ims.dto.ProveedorRequestDTO;
import com.graspymar.ims.dto.ProveedorResponseDTO;
import com.graspymar.ims.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedoresController {

    private final ProveedorService proveedorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ProveedorResponseDTO crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        return proveedorService.crear(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<ProveedorResponseDTO> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ProveedorResponseDTO buscarPorId(@PathVariable Long id) {
        return proveedorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ProveedorResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorRequestDTO dto) {
        return proveedorService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
    }

}