package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Maquinaria;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {

    Optional<Maquinaria> findByCodigo(String codigo);

    List<Maquinaria> findByActivoTrue();

}
