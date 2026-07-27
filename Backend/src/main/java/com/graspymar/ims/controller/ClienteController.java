package com.graspymar.ims.controller;

import com.graspymar.ims.dto.ClienteRequestDTO;
import com.graspymar.ims.dto.ClienteResponseDTO;
import com.graspymar.ims.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDTO crear(@Valid @RequestBody ClienteRequestDTO dto) {
        return clienteService.crear(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        return clienteService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    /*@ResponseStatus(HttpStatus.NO_CONTENT)*/
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

}
