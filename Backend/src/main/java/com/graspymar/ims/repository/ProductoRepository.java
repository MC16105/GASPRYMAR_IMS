package com.graspymar.ims.repository;

import com.graspymar.ims.entity.Producto;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

   Optional<Producto> findByNombre(String nombre);

   List<Producto> findByActivoTrue();
}
