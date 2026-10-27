package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Cliente;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

    Optional<Cliente> findByDuiNit(String duiNit);

    List<Cliente> findByActivoTrue();

}
