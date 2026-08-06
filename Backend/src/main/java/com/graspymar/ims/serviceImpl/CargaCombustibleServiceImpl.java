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
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CargaCombustibleServiceImpl implements CargaCombustibleService {

    private final CargaCombustibleRepository cargaCombustibleRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ProveedorRepository proveedorRepository;

    @Override
    public CargaCombustibleResponseDTO crear(CargaCombustibleRequestDTO request) {

        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        BigDecimal total = request.getPrecioGalon()
                .multiply(BigDecimal.valueOf(request.getGalones()));

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

        return convertirDTO(cargaCombustibleRepository.save(carga));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CargaCombustibleResponseDTO> listar() {
        return cargaCombustibleRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CargaCombustibleResponseDTO buscarPorId(Long id) {

        CargaCombustible carga = cargaCombustibleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga de combustible no encontrada"));

        return convertirDTO(carga);
    }

    @Override
    public CargaCombustibleResponseDTO actualizar(Long id, CargaCombustibleRequestDTO request) {

        CargaCombustible carga = cargaCombustibleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga de combustible no encontrada"));

        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        BigDecimal total = request.getPrecioGalon()
                .multiply(BigDecimal.valueOf(request.getGalones()));

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

        return convertirDTO(cargaCombustibleRepository.save(carga));
    }

    @Override
    public void eliminar(Long id) {

        CargaCombustible carga = cargaCombustibleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga de combustible no encontrada"));

        cargaCombustibleRepository.delete(carga);
    }

    private CargaCombustibleResponseDTO convertirDTO(CargaCombustible carga) {

        return CargaCombustibleResponseDTO.builder()
                .id(carga.getId())
                .vehiculo(carga.getVehiculo().getPlaca())
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
