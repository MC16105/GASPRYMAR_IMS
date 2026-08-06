package com.graspymar.ims.repository;

import com.graspymar.ims.entity.ControlMaquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlMaquinariaRepository extends JpaRepository<ControlMaquinaria, Long> {
}
