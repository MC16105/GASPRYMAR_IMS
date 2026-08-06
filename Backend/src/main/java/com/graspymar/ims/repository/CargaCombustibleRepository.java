package com.graspymar.ims.repository;

import com.graspymar.ims.entity.CargaCombustible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargaCombustibleRepository extends JpaRepository<CargaCombustible, Long> {

}
