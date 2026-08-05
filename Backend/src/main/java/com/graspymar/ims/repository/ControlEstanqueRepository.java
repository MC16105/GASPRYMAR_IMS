package com.graspymar.ims.repository;

import com.graspymar.ims.entity.ControlEstanque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlEstanqueRepository extends JpaRepository<ControlEstanque, Long> {
}
