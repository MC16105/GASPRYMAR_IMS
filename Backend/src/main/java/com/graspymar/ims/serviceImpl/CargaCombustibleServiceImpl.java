package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.CargaCombustibleRequestDTO;
import com.graspymar.ims.dto.CargaCombustibleResponseDTO;

import com.graspymar.ims.entity.CargaCombustible;
import com.graspymar.ims.entity.Proveedor;
import com.graspymar.ims.entity.Vehiculo;

import com.graspymar.ims.repository.CargaCombustibleRepository;
import com.graspymar.ims.repository.ProveedorRepository;
import com.graspymar.ims.repository.VehiculoRepository;

import com.graspymar.ims.service.CargaCombustibleService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CargaCombustibleServiceImpl implements CargaCombustibleService {

    private final CargaCombustibleRepository cargaCombustibleRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ProveedorRepository proveedorRepository;

    // =====================================================
    // CREAR CARGA DE COMBUSTIBLE
    // =====================================================

    @Override
    public CargaCombustibleResponseDTO crear(
            CargaCombustibleRequestDTO request
    ) {

        // Validar cantidades y precio
        validarValores(request);

        // Buscar vehículo
        Vehiculo vehiculo = vehiculoRepository
                .findById(request.getVehiculoId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vehículo no encontrado."
                        )
                );

        // Impedir cargas para vehículos inactivos
        validarVehiculoActivo(vehiculo);

        // Buscar proveedor
        Proveedor proveedor = proveedorRepository
                .findById(request.getProveedorId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Proveedor no encontrado."
                        )
                );

        // Impedir utilizar proveedores inactivos
        validarProveedorActivo(proveedor);

        // Calcular total
        BigDecimal total = request.getPrecioGalon()
                .multiply(
                        BigDecimal.valueOf(request.getGalones())
                );

        // Crear carga
        CargaCombustible carga = CargaCombustible.builder()
                .vehiculo(vehiculo)
                .proveedor(proveedor)
                .fecha(request.getFecha())
                .galones(request.getGalones())
                .precioGalon(request.getPrecioGalon())
                .total(total)
                .kilometraje(request.getKilometraje())
                .tipoCombustible(request.getTipoCombustible())
                .numeroFactura(request.getNumeroFactura())
                .observaciones(request.getObservaciones())
                .build();

        CargaCombustible guardada =
                cargaCombustibleRepository.save(carga);

        return convertirDTO(guardada);
    }

    // =====================================================
    // LISTAR CARGAS ACTIVAS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<CargaCombustibleResponseDTO> listar() {

        return cargaCombustibleRepository.findByActivoTrue()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // =====================================================
    // BUSCAR CARGA POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CargaCombustibleResponseDTO buscarPorId(Long id) {

        CargaCombustible carga = cargaCombustibleRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Carga de combustible no encontrada."
                        )
                );

        // Impedir consultar cargas inactivas
        validarCargaActiva(carga);

        return convertirDTO(carga);
    }

    // =====================================================
    // ACTUALIZAR CARGA DE COMBUSTIBLE
    // =====================================================

    @Override
    public CargaCombustibleResponseDTO actualizar(
            Long id,
            CargaCombustibleRequestDTO request
    ) {

        CargaCombustible carga = cargaCombustibleRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Carga de combustible no encontrada."
                        )
                );

        // Impedir modificar cargas inactivas
        validarCargaActiva(carga);

        // Validar cantidades y precio
        validarValores(request);

        // Buscar vehículo
        Vehiculo vehiculo = vehiculoRepository
                .findById(request.getVehiculoId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vehículo no encontrado."
                        )
                );

        validarVehiculoActivo(vehiculo);

        // Buscar proveedor
        Proveedor proveedor = proveedorRepository
                .findById(request.getProveedorId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Proveedor no encontrado."
                        )
                );

        validarProveedorActivo(proveedor);

        // Recalcular total
        BigDecimal total = request.getPrecioGalon()
                .multiply(
                        BigDecimal.valueOf(request.getGalones())
                );

        // Actualizar datos
        carga.setVehiculo(vehiculo);
        carga.setProveedor(proveedor);
        carga.setFecha(request.getFecha());
        carga.setGalones(request.getGalones());
        carga.setPrecioGalon(request.getPrecioGalon());
        carga.setTotal(total);
        carga.setKilometraje(request.getKilometraje());
        carga.setTipoCombustible(request.getTipoCombustible());
        carga.setNumeroFactura(request.getNumeroFactura());
        carga.setObservaciones(request.getObservaciones());

        CargaCombustible actualizada =
                cargaCombustibleRepository.save(carga);

        return convertirDTO(actualizada);
    }

    // =====================================================
    // ELIMINAR CARGA (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    public void eliminar(Long id) {

        CargaCombustible carga = cargaCombustibleRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Carga de combustible no encontrada."
                        )
                );

        // Evitar eliminar dos veces la misma carga
        if (!Boolean.TRUE.equals(carga.getActivo())) {
            throw new IllegalArgumentException(
                    "La carga de combustible ya se encuentra inactiva."
            );
        }

        // Eliminación lógica
        carga.setActivo(false);

        cargaCombustibleRepository.save(carga);
    }

    // =====================================================
    // VALIDAR VEHÍCULO ACTIVO
    // =====================================================

    private void validarVehiculoActivo(Vehiculo vehiculo) {

        if (!Boolean.TRUE.equals(vehiculo.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede registrar la operación: " +
                            "el vehículo se encuentra inactivo."
            );
        }
    }

    // =====================================================
    // VALIDAR PROVEEDOR ACTIVO
    // =====================================================

    private void validarProveedorActivo(Proveedor proveedor) {

        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede registrar la operación: " +
                            "el proveedor se encuentra inactivo."
            );
        }
    }

    // =====================================================
    // VALIDAR CARGA ACTIVA
    // =====================================================

    private void validarCargaActiva(CargaCombustible carga) {

        if (!Boolean.TRUE.equals(carga.getActivo())) {
            throw new EntityNotFoundException(
                    "La carga de combustible no se encuentra activa."
            );
        }
    }

    // =====================================================
    // VALIDAR GALONES, PRECIO Y KILOMETRAJE
    // =====================================================

    private void validarValores(CargaCombustibleRequestDTO request) {

        // Galones: deben ser mayores que cero
        if (request.getGalones() == null ||
                BigDecimal.valueOf(request.getGalones())
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad de galones debe ser mayor que cero."
            );
        }

        // Precio: no puede ser negativo
        if (request.getPrecioGalon() == null ||
                request.getPrecioGalon()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El precio por galón no puede ser negativo."
            );
        }

        // Kilometraje: no puede ser negativo
        if (request.getKilometraje() != null &&
                new BigDecimal(request.getKilometraje().toString())
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El kilometraje no puede ser negativo."
            );
        }
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private CargaCombustibleResponseDTO convertirDTO(
            CargaCombustible carga
    ) {

        return CargaCombustibleResponseDTO.builder()
                .id(carga.getId())
                .vehiculoId(carga.getVehiculo().getId())
                .vehiculo(carga.getVehiculo().getPlaca())
                .proveedorId(carga.getProveedor().getId())
                .proveedor(carga.getProveedor().getNombreRazonSocial())
                .fecha(carga.getFecha())
                .galones(carga.getGalones())
                .precioGalon(carga.getPrecioGalon())
                .total(carga.getTotal())
                .kilometraje(carga.getKilometraje())
                .tipoCombustible(carga.getTipoCombustible())
                .numeroFactura(carga.getNumeroFactura())
                .observaciones(carga.getObservaciones())
                .build();
    }
}