package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{

    Optional<Proveedor> findByDuiNit(String duiNit);

}
