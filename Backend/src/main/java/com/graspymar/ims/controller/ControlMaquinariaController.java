package com.graspymar.ims.controller;

import com.graspymar.ims.dto.ControlMaquinariaRequestDTO;
import com.graspymar.ims.dto.ControlMaquinariaResponseDTO;
import com.graspymar.ims.service.ControlMaquinariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/control_maquinaria")
@RequiredArgsConstructor
public class ControlMaquinariaController {

    private final ControlMaquinariaService controlMaquinariaService;

    @PostMapping
    public ControlMaquinariaResponseDTO crear(
            @Valid @RequestBody ControlMaquinariaRequestDTO dto){
        return controlMaquinariaService.crear(dto);
    }

    @GetMapping
    public List<ControlMaquinariaResponseDTO> listar(){
        return controlMaquinariaService.listar();
    }

    @GetMapping("/{id}")
    public ControlMaquinariaResponseDTO buscarPorId(
            @PathVariable Long id){
        return controlMaquinariaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ControlMaquinariaResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody ControlMaquinariaRequestDTO dto){
        return controlMaquinariaService.actualizar(id,dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id){
        controlMaquinariaService.eliminar(id);
    }
}
