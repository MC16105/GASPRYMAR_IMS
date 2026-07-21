package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.entity.Rol;
import com.graspymar.ims.repository.RolRepository;
import com.graspymar.ims.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository repository;

    @Override
    public List<Rol> listar() {
        return repository.findAll();
    }

    @Override
    public Rol guardar(Rol rol) {
        return repository.save(rol);
    }

}
