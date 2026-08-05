package com.graspymar.ims.controller;

import com.graspymar.ims.dto.ProduccionRequestDTO;
import com.graspymar.ims.dto.ProduccionResponseDTO;
import com.graspymar.ims.service.ProduccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producciones")
@RequiredArgsConstructor
public class ProduccionController {

    private final ProduccionService produccionService;

    @PostMapping
    public ProduccionResponseDTO crear(
            @Valid @RequestBody ProduccionRequestDTO dto){
        return produccionService.crear(dto);
    }

    @GetMapping
    public List<ProduccionResponseDTO> listar(){
        return produccionService.listar();
    }

    @GetMapping("/{id}")
    public ProduccionResponseDTO buscarPorId(@PathVariable Long id){
        return produccionService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProduccionResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProduccionRequestDTO dto){
        return produccionService.actualizar(id,dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        produccionService.eliminar(id);
    }
}
