package com.graspymar.ims.controller;

import com.graspymar.ims.dto.InsumoRequestDTO;
import com.graspymar.ims.dto.InsumoResponseDTO;
import com.graspymar.ims.service.InsumoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService insumoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public InsumoResponseDTO crear(@Valid @RequestBody InsumoRequestDTO dto) {
        return insumoService.crear(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<InsumoResponseDTO> listar() {
        return insumoService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public InsumoResponseDTO buscarPorId(@PathVariable Long id) {
        return insumoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public InsumoResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InsumoRequestDTO dto) {
        return insumoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        insumoService.eliminar(id);
    }
}
