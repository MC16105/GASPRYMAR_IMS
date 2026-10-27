package com.graspymar.ims.repository;

import com.graspymar.ims.entity.CargaCombustible;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargaCombustibleRepository extends JpaRepository<CargaCombustible, Long> {

    List<CargaCombustible> findByActivoTrue();
}
