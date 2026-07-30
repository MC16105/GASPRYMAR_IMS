package com.graspymar.ims.controller;

import com.graspymar.ims.dto.CompraRequestDTO;
import com.graspymar.ims.dto.CompraResponseDTO;
import com.graspymar.ims.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {
    private final CompraService compraService;

    @PostMapping
    public CompraResponseDTO crear(
            @Valid @RequestBody CompraRequestDTO dto){

        return compraService.crear(dto);

    }

    @GetMapping
    public List<CompraResponseDTO> listar(){

        return compraService.listar();

    }

    @GetMapping("/{id}")
    public CompraResponseDTO buscarPorId(
            @PathVariable Long id){

        return compraService.buscarPorId(id);

    }

    @PutMapping("/{id}")
    public CompraResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody CompraRequestDTO dto){

        return compraService.actualizar(id,dto);

    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id){

        compraService.eliminar(id);

    }
}
