package com.graspymar.ims.controller;

import com.graspymar.ims.entity.Rol;
import com.graspymar.ims.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService service;

    @GetMapping
    public List<Rol> listar(){
        return service.listar();
    }

   @PostMapping
    public Rol guardar(@RequestBody Rol rol){
        return service.guardar(rol);
    }

    
}
