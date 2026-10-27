package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Estanque;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstanqueRepository extends JpaRepository<Estanque, Long> {

    Optional<Estanque> findByCodigo(String codigo);

    List<Estanque> findByActivoTrue();
}
