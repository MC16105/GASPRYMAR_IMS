package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Produccion;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduccionRepository extends JpaRepository<Produccion, Long> {

    List<Produccion> findByActivoTrue();
}
