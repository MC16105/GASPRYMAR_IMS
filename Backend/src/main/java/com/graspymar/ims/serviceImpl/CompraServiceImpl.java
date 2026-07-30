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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {
    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final InsumoRepository insumoRepository;

    @Override
    @Transactional
    public CompraResponseDTO crear(CompraRequestDTO dto) {

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Proveedor no encontrado."));

        Compra compra = Compra.builder()
                .proveedor(proveedor)
                .fecha(dto.getFecha())
                .documentoFiscal(dto.getDocumentoFiscal())
                .observaciones(dto.getObservaciones())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleCompraRequestDTO detalleDTO : dto.getDetalles()) {

            Insumo insumo = insumoRepository.findById(detalleDTO.getInsumoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Insumo no encontrado."));

            BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleCompra detalle = DetalleCompra.builder()
                    .compra(compra)
                    .insumo(insumo)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();

            compra.getDetalles().add(detalle);

            total = total.add(subtotal);

            // Actualizar stock del insumo
            insumo.setStockActual(
                    insumo.getStockActual() + detalleDTO.getCantidad()
            );
        }

        compra.setMontoTotal(total);

        Compra compraGuardada = compraRepository.save(compra);

        return toResponse(compraGuardada);
    }

    @Override
    public List<CompraResponseDTO> listar() {

        return compraRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CompraResponseDTO buscarPorId(Long id) {

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Compra no encontrada."));

        return toResponse(compra);
    }

    @Override
    @Transactional
    public CompraResponseDTO actualizar(Long id, CompraRequestDTO dto) {

        throw new UnsupportedOperationException(
                "Actualizar compras aún no implementado."
        );
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Compra no encontrada."));

        compra.setActivo(false);

        compraRepository.save(compra);
    }

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

                        ).toList();

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