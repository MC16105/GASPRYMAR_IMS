package com.graspymar.ims.repository;

import com.graspymar.ims.entity.DetalleCompra;
import com.graspymar.ims.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra,Long> {

}
