package com.graspymar.ims.service;

import java.util.List;
import com.graspymar.ims.entity.Rol;
public interface RolService {
     
    List<Rol> listar();
    Rol guardar(Rol rol);

}
