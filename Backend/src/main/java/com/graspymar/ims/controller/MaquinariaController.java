package com.graspymar.ims.controller;

import com.graspymar.ims.dto.MaquinariaRequestDTO;
import com.graspymar.ims.dto.MaquinariaResponseDTO;
import com.graspymar.ims.service.MaquinariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maquinarias")
@RequiredArgsConstructor
public class MaquinariaController {

    private final MaquinariaService maquinariaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public MaquinariaResponseDTO crear(@Valid @RequestBody MaquinariaRequestDTO dto) {
        return maquinariaService.crear(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<MaquinariaResponseDTO> listar() {
        return maquinariaService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public MaquinariaResponseDTO buscarPorId(@PathVariable Long id) {
        return maquinariaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public MaquinariaResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MaquinariaRequestDTO dto) {
        return maquinariaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        maquinariaService.eliminar(id);
    }
}
