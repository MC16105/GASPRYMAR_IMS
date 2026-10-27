package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.CompraRequestDTO;
import com.graspymar.ims.dto.CompraResponseDTO;
import com.graspymar.ims.dto.DetalleCompraRequestDTO;
import com.graspymar.ims.dto.DetalleCompraResponseDTO;

import com.graspymar.ims.entity.Compra;
import com.graspymar.ims.entity.DetalleCompra;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.entity.Proveedor;

import com.graspymar.ims.repository.CompraRepository;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.repository.ProveedorRepository;

import com.graspymar.ims.service.CompraService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final InsumoRepository insumoRepository;

    // =====================================================
    // CREAR COMPRA
    // =====================================================

    @Override
    @Transactional
    public CompraResponseDTO crear(CompraRequestDTO dto) {

        // Validar que la compra tenga detalles
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La compra debe contener al menos un detalle."
            );
        }

        // Buscar proveedor
        Proveedor proveedor = proveedorRepository
                .findById(dto.getProveedorId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Proveedor no encontrado."
                        )
                );

        // Impedir compras a proveedores inactivos
        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede registrar una compra: " +
                            "el proveedor se encuentra inactivo."
            );
        }

        // Crear compra
        Compra compra = Compra.builder()
                .proveedor(proveedor)
                .fecha(dto.getFecha())
                .documentoFiscal(dto.getDocumentoFiscal())
                .observaciones(dto.getObservaciones())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // =================================================
        // PROCESAR DETALLES DE COMPRA
        // =================================================

        for (DetalleCompraRequestDTO detalleDTO : dto.getDetalles()) {

            // Validar cantidad
            if (detalleDTO.getCantidad() == null ||
                    detalleDTO.getCantidad() <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad de compra debe ser mayor que cero."
                );
            }

            // Validar precio unitario
            if (detalleDTO.getPrecioUnitario() == null ||
                    detalleDTO.getPrecioUnitario()
                            .compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "El precio unitario no puede ser negativo."
                );
            }

            // Buscar insumo
            Insumo insumo = insumoRepository
                    .findById(detalleDTO.getInsumoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Insumo no encontrado."
                            )
                    );

            // Impedir compras de insumos inactivos
            if (!Boolean.TRUE.equals(insumo.getActivo())) {
                throw new IllegalArgumentException(
                        "No se puede comprar el insumo inactivo: "
                                + insumo.getNombre()
                );
            }

            // Convertir cantidad a BigDecimal
            BigDecimal cantidad = BigDecimal.valueOf(
                    detalleDTO.getCantidad()
            );

            // Calcular subtotal
            BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                    .multiply(cantidad);

            // Crear detalle
            DetalleCompra detalle = DetalleCompra.builder()
                    .compra(compra)
                    .insumo(insumo)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();

            compra.getDetalles().add(detalle);

            // Acumular monto total
            total = total.add(subtotal);

            // Incrementar existencias del insumo
            insumo.setStockActual(
                    insumo.getStockActual().add(cantidad)
            );

            insumoRepository.save(insumo);
        }

        // Asignar monto total
        compra.setMontoTotal(total);

        // Guardar compra y detalles
        Compra compraGuardada = compraRepository.save(compra);

        return toResponse(compraGuardada);
    }

    // =====================================================
    // LISTAR COMPRAS ACTIVAS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CompraResponseDTO> listar() {

        return compraRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR COMPRA POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CompraResponseDTO buscarPorId(Long id) {

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Compra no encontrada."
                        )
                );

        // Impedir consultar compras inactivas
        if (!Boolean.TRUE.equals(compra.getActivo())) {
            throw new EntityNotFoundException(
                    "La compra no se encuentra activa."
            );
        }

        return toResponse(compra);
    }

    // =====================================================
    // ACTUALIZAR COMPRA
    // =====================================================

    @Override
    @Transactional
    public CompraResponseDTO actualizar(
            Long id,
            CompraRequestDTO dto
    ) {

        // Se conserva el comportamiento original
        throw new UnsupportedOperationException(
                "Actualizar compras aún no implementado."
        );
    }

    // =====================================================
    // ELIMINAR COMPRA (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Compra no encontrada."
                        )
                );

        // Evitar eliminar dos veces la misma compra
        if (!Boolean.TRUE.equals(compra.getActivo())) {
            throw new IllegalArgumentException(
                    "La compra ya se encuentra inactiva."
            );
        }

        // Eliminación lógica
        // No se descuentan automáticamente los insumos comprados
        compra.setActivo(false);

        compraRepository.save(compra);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private CompraResponseDTO toResponse(Compra compra) {

        List<DetalleCompraResponseDTO> detalles =
                compra.getDetalles()
                        .stream()
                        .map(detalle ->
                                DetalleCompraResponseDTO.builder()
                                        .id(detalle.getId())
                                        .insumoId(detalle.getInsumo().getId())
                                        .nombreInsumo(detalle.getInsumo().getNombre())
                                        .cantidad(detalle.getCantidad())
                                        .precioUnitario(detalle.getPrecioUnitario())
                                        .subtotal(detalle.getSubtotal())
                                        .build()
                        )
                        .toList();

        return CompraResponseDTO.builder()
                .id(compra.getId())
                .proveedor(compra.getProveedor().getNombreRazonSocial())
                .fecha(compra.getFecha())
                .documentoFiscal(compra.getDocumentoFiscal())
                .montoTotal(compra.getMontoTotal())
                .observaciones(compra.getObservaciones())
                .detalles(detalles)
                .build();
    }
}