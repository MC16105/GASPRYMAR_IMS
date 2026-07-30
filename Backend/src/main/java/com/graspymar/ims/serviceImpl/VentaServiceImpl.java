package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.DetalleVentaRequestDTO;
import com.graspymar.ims.dto.DetalleVentaResponseDTO;
import com.graspymar.ims.dto.VentaRequestDTO;
import com.graspymar.ims.dto.VentaResponseDTO;
import com.graspymar.ims.entity.Cliente;
import com.graspymar.ims.entity.DetalleVenta;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.entity.Venta;
import com.graspymar.ims.repository.ClienteRepository;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.repository.VentaRepository;
import com.graspymar.ims.service.VentaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final InsumoRepository insumoRepository;

    @Override
    @Transactional
    public VentaResponseDTO crear(VentaRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Cliente no encontrado."));

        Venta venta = Venta.builder()
                .cliente(cliente)
                .fecha(dto.getFecha())
                .documentoFiscal(dto.getDocumentoFiscal())
                .observaciones(dto.getObservaciones())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe tener al menos un detalle."
            );
        }

        for (DetalleVentaRequestDTO detalleDTO : dto.getDetalles()) {

            Insumo insumo = insumoRepository.findById(detalleDTO.getInsumoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Insumo no encontrado."));

            BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .insumo(insumo)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();

            venta.getDetalles().add(detalle);

            total = total.add(subtotal);

            // Actualizar stock del insumo
            if (insumo.getStockActual() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente.");
            }

            insumo.setStockActual(
                    insumo.getStockActual() - detalleDTO.getCantidad()
            );

            insumoRepository.save(insumo);
        }

       venta.setMontoTotal(total);

        Venta ventaGuardada = ventaRepository.save(venta);

        return toResponse(ventaGuardada);
    }

    @Override
    public List<VentaResponseDTO> listar() {

        return ventaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public VentaResponseDTO buscarPorId(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Venta no encontrada."));

        return toResponse(venta);
    }

    @Override
    @Transactional
    public VentaResponseDTO actualizar(Long id, VentaRequestDTO dto) {

        throw new UnsupportedOperationException(
                "Actualizar ventas aún no implementado."
        );
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Venta no encontrada."));

        venta.setActivo(false);

        ventaRepository.save(venta);
    }

    private VentaResponseDTO toResponse(Venta venta) {

        List<DetalleVentaResponseDTO> detalles =
                venta.getDetalles()
                        .stream()
                        .map(detalle ->

                                DetalleVentaResponseDTO.builder()
                                        .id(detalle.getId())
                                        .insumoId(detalle.getInsumo().getId())
                                        .nombreInsumo(detalle.getInsumo().getNombre())
                                        .cantidad(detalle.getCantidad())
                                        .precioUnitario(detalle.getPrecioUnitario())
                                        .subtotal(detalle.getSubtotal())
                                        .build()

                        ).toList();

        return VentaResponseDTO.builder()
                .id(venta.getId())
                .cliente(venta.getCliente().getNombreRazonSocial())
                .fecha(venta.getFecha())
                .documentoFiscal(venta.getDocumentoFiscal())
                .montoTotal(venta.getMontoTotal())
                .observaciones(venta.getObservaciones())
                .detalles(detalles)
                .build();

    }
}
