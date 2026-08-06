package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {

    Optional<Maquinaria> findByCodigo(String codigo);

}
