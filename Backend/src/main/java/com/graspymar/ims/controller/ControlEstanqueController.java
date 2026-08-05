package com.graspymar.ims.controller;

import com.graspymar.ims.dto.ControlEstanqueRequestDTO;
import com.graspymar.ims.dto.ControlEstanqueResponseDTO;
import com.graspymar.ims.service.ControlEstanqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/control_estanque")
@RequiredArgsConstructor
public class ControlEstanqueController {

    private final ControlEstanqueService controlEstanqueService;

    @PostMapping
    public ControlEstanqueResponseDTO crear(
            @Valid @RequestBody ControlEstanqueRequestDTO dto){
        return controlEstanqueService.crear(dto);
    }

    @GetMapping
    public List<ControlEstanqueResponseDTO> listar(){
        return controlEstanqueService.listar();
    }

    @GetMapping("/{id}")
    public ControlEstanqueResponseDTO buscarPorId(
            @PathVariable Long id){
        return controlEstanqueService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ControlEstanqueResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody ControlEstanqueRequestDTO dto){
        return controlEstanqueService.actualizar(id,dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id){
        controlEstanqueService.eliminar(id);
    }
}
