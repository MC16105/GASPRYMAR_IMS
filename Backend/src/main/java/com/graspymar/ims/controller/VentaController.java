package com.graspymar.ims.controller;

import com.graspymar.ims.dto.VentaRequestDTO;
import com.graspymar.ims.dto.VentaResponseDTO;
import com.graspymar.ims.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;

    @PostMapping
    public VentaResponseDTO crear(
            @Valid @RequestBody VentaRequestDTO dto){
        return ventaService.crear(dto);
    }

    @GetMapping
    public List<VentaResponseDTO> listar(){
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public VentaResponseDTO buscarPorId(
            @PathVariable Long id){
        return ventaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public VentaResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody VentaRequestDTO dto){
        return ventaService.actualizar(id,dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id){
        ventaService.eliminar(id);
    }
}
