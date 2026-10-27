package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.DetalleVentaRequestDTO;
import com.graspymar.ims.dto.DetalleVentaResponseDTO;
import com.graspymar.ims.dto.VentaRequestDTO;
import com.graspymar.ims.dto.VentaResponseDTO;

import com.graspymar.ims.entity.Cliente;
import com.graspymar.ims.entity.DetalleVenta;
import com.graspymar.ims.entity.Producto;
import com.graspymar.ims.entity.Venta;

import com.graspymar.ims.repository.ClienteRepository;
import com.graspymar.ims.repository.ProductoRepository;
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
    private final ProductoRepository productoRepository;

    // =====================================================
    // CREAR VENTA
    // =====================================================

    @Override
    @Transactional
    public VentaResponseDTO crear(VentaRequestDTO dto) {

        // Buscar cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado."
                        )
                );

        // Validar que el cliente esté activo
        if (!Boolean.TRUE.equals(cliente.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede registrar la venta porque el cliente está inactivo."
            );
        }

        // Validar que existan detalles
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe tener al menos un detalle."
            );
        }

        // Construir venta
        Venta venta = Venta.builder()
                .cliente(cliente)
                .fecha(dto.getFecha())
                .documentoFiscal(dto.getDocumentoFiscal())
                .observaciones(dto.getObservaciones())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // Procesar detalles
        for (DetalleVentaRequestDTO detalleDTO : dto.getDetalles()) {

            // Buscar producto
            Producto producto = productoRepository.findById(
                            detalleDTO.getProductoId()
                    )
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Producto no encontrado."
                            )
                    );

            // Validar que el producto esté activo
            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new IllegalArgumentException(
                        "No se puede vender el producto '" +
                                producto.getNombre() +
                                "' porque está inactivo."
                );
            }

            // Validar cantidad positiva
            if (detalleDTO.getCantidad() == null ||
                    detalleDTO.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad del producto '" +
                                producto.getNombre() +
                                "' debe ser mayor que cero."
                );
            }

            // Validar precio unitario
            if (detalleDTO.getPrecioUnitario() == null ||
                    detalleDTO.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "El precio unitario del producto '" +
                                producto.getNombre() +
                                "' no puede ser negativo."
                );
            }

            // Validar stock disponible
            if (producto.getStockActual()
                    .compareTo(detalleDTO.getCantidad()) < 0) {

                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " +
                                producto.getNombre()
                );
            }

            // Calcular subtotal
            BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                    .multiply(detalleDTO.getCantidad());

            // Construir detalle
            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();

            // Agregar detalle a la venta
            venta.getDetalles().add(detalle);

            // Acumular total
            total = total.add(subtotal);

            // Descontar stock
            producto.setStockActual(
                    producto.getStockActual()
                            .subtract(detalleDTO.getCantidad())
            );

            productoRepository.save(producto);
        }

        // Establecer monto total
        venta.setMontoTotal(total);

        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(venta);

        return toResponse(ventaGuardada);
    }

    // =====================================================
    // LISTAR VENTAS ACTIVAS
    // =====================================================

    @Override
    @Transactional
    public List<VentaResponseDTO> listar() {

        return ventaRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR VENTA POR ID
    // =====================================================

    @Override
    @Transactional
    public VentaResponseDTO buscarPorId(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Venta no encontrada."
                        )
                );

        // No mostrar ventas inactivas en la consulta normal
        if (!Boolean.TRUE.equals(venta.getActivo())) {
            throw new EntityNotFoundException(
                    "La venta no se encuentra activa."
            );
        }

        return toResponse(venta);
    }

    // =====================================================
    // ACTUALIZAR VENTA
    // =====================================================

    @Override
    @Transactional
    public VentaResponseDTO actualizar(Long id, VentaRequestDTO dto) {

        throw new UnsupportedOperationException(
                "Actualizar ventas aún no implementado."
        );
    }

    // =====================================================
    // ELIMINAR VENTA (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Venta no encontrada."
                        )
                );

        // Evitar eliminar nuevamente una venta inactiva
        if (!Boolean.TRUE.equals(venta.getActivo())) {
            throw new IllegalArgumentException(
                    "La venta ya se encuentra inactiva."
            );
        }

        // Eliminación lógica
        venta.setActivo(false);

        ventaRepository.save(venta);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private VentaResponseDTO toResponse(Venta venta) {

        List<DetalleVentaResponseDTO> detalles = venta.getDetalles()
                .stream()
                .map(detalle ->
                        DetalleVentaResponseDTO.builder()
                                .id(detalle.getId())
                                .productoId(
                                        detalle.getProducto().getId()
                                )
                                .nombreProducto(
                                        detalle.getProducto().getNombre()
                                )
                                .cantidad(detalle.getCantidad())
                                .precioUnitario(
                                        detalle.getPrecioUnitario()
                                )
                                .subtotal(detalle.getSubtotal())
                                .build()
                )
                .toList();

        return VentaResponseDTO.builder()
                .id(venta.getId())
                .cliente(
                        venta.getCliente().getNombreRazonSocial()
                )
                .fecha(venta.getFecha())
                .documentoFiscal(venta.getDocumentoFiscal())
                .montoTotal(venta.getMontoTotal())
                .observaciones(venta.getObservaciones())
                .detalles(detalles)
                .build();
    }
}