package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Estanque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstanqueRepository extends JpaRepository<Estanque, Long> {

    Optional<Estanque> findByCodigo(String codigo);

}
