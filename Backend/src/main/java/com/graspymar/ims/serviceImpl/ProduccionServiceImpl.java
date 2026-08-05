package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ProduccionRequestDTO;
import com.graspymar.ims.dto.ProduccionResponseDTO;
import com.graspymar.ims.entity.Produccion;
import com.graspymar.ims.entity.Producto;
import com.graspymar.ims.repository.ProduccionRepository;
import com.graspymar.ims.repository.ProductoRepository;
import com.graspymar.ims.service.ProduccionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduccionServiceImpl implements ProduccionService {

    private final ProduccionRepository produccionRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public ProduccionResponseDTO crear(ProduccionRequestDTO dto) {

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Producto no encontrado."));

        Produccion produccion = Produccion.builder()
                .producto(producto)
                .tipoMovimiento(dto.getTipoMovimiento())
                .fecha(dto.getFecha())
                .cantidad(dto.getCantidad())
                .unidadMedida(dto.getUnidadMedida())
                .observaciones(dto.getObservaciones())
                .build();

        producto.setStockActual(
                producto.getStockActual().add(dto.getCantidad())
        );

        productoRepository.save(producto);

        Produccion produccionGuardada = produccionRepository.save(produccion);

        return toResponse(produccionGuardada);
    }

    @Override
    public List<ProduccionResponseDTO> listar() {

        return produccionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProduccionResponseDTO buscarPorId(Long id) {

        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Producción no encontrada."));

        return toResponse(produccion);
    }

    @Override
    @Transactional
    public ProduccionResponseDTO actualizar(Long id, ProduccionRequestDTO dto) {

        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Producción no encontrada."));

        Producto productoAnterior = produccion.getProducto();

        productoAnterior.setStockActual(
                productoAnterior.getStockActual()
                        .subtract(produccion.getCantidad())
        );

        productoRepository.save(productoAnterior);

        Producto nuevoProducto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Producto no encontrado."));

        nuevoProducto.setStockActual(
                nuevoProducto.getStockActual()
                        .add(dto.getCantidad())
        );

        productoRepository.save(nuevoProducto);

        produccion.setProducto(nuevoProducto);
        produccion.setTipoMovimiento(dto.getTipoMovimiento());
        produccion.setFecha(dto.getFecha());
        produccion.setCantidad(dto.getCantidad());
        produccion.setUnidadMedida(dto.getUnidadMedida());
        produccion.setObservaciones(dto.getObservaciones());

        Produccion actualizada = produccionRepository.save(produccion);

        return toResponse(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Producción no encontrada."));

        Producto producto = produccion.getProducto();

        producto.setStockActual(
                producto.getStockActual()
                        .subtract(produccion.getCantidad())
        );

        productoRepository.save(producto);

        produccion.setActivo(false);

        produccionRepository.save(produccion);
    }

    private ProduccionResponseDTO toResponse(Produccion produccion) {

        return ProduccionResponseDTO.builder()
                .id(produccion.getId())
                .producto(produccion.getProducto().getNombre())
                .tipoMovimiento(produccion.getTipoMovimiento())
                .fecha(produccion.getFecha())
                .cantidad(produccion.getCantidad())
                .unidadMedida(produccion.getUnidadMedida())
                .observaciones(produccion.getObservaciones())
                .build();
    }


}
