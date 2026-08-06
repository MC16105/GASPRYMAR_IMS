package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ControlMaquinariaRequestDTO;
import com.graspymar.ims.dto.ControlMaquinariaResponseDTO;
import com.graspymar.ims.dto.DetalleControlMaquinariaRequestDTO;
import com.graspymar.ims.dto.DetalleControlMaquinariaResponseDTO;
import com.graspymar.ims.entity.ControlMaquinaria;
import com.graspymar.ims.entity.DetalleControlMaquinaria;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.entity.Maquinaria;
import com.graspymar.ims.repository.ControlMaquinariaRepository;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.repository.MaquinariaRepository;
import com.graspymar.ims.service.ControlMaquinariaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlMaquinariaServiceImpl implements ControlMaquinariaService {

    private final ControlMaquinariaRepository controlMaquinariaRepository;
    private final MaquinariaRepository maquinariaRepository;
    private final InsumoRepository insumoRepository;

    @Override
    @Transactional
    public ControlMaquinariaResponseDTO crear(ControlMaquinariaRequestDTO dto) {
        Maquinaria maquinaria = maquinariaRepository.findById(dto.getMaquinariaId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Maquinaria no encontrado."));
        ControlMaquinaria control = ControlMaquinaria.builder()
                .maquinaria(maquinaria)
                .fecha(dto.getFecha())
                .horasUso(dto.getHorasUso())
                .operador(dto.getOperador())
                .build();
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("Debe registrar al menos un detalle." ); }

        for (DetalleControlMaquinariaRequestDTO detalleDTO : dto.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detalleDTO.getInsumoId())
                    .orElseThrow(() -> new EntityNotFoundException("Insumo no encontrado."));
            if (insumo.getStockActual() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el insumo: "+ insumo.getNombre());
            }
            insumo.setStockActual(insumo.getStockActual() - detalleDTO.getCantidad());
            insumoRepository.save(insumo);
            DetalleControlMaquinaria detalle = DetalleControlMaquinaria.builder()
                    .controlMaquinaria(control)
                    .insumo(insumo)
                    .cantidad(detalleDTO.getCantidad())
                    .build();
            control.getDetalles().add(detalle); }
        ControlMaquinaria guardado = controlMaquinariaRepository.save(control);
        return toResponse(guardado); }

    @Override
    public List<ControlMaquinariaResponseDTO> listar() {
        return controlMaquinariaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList(); }

    @Override
    public ControlMaquinariaResponseDTO buscarPorId(Long id) {
        ControlMaquinaria control = controlMaquinariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                                "Control de maquina no encontrado."));
        return toResponse(control); }

    @Override
    @Transactional
    public ControlMaquinariaResponseDTO actualizar(Long id, ControlMaquinariaRequestDTO dto) {
        throw new UnsupportedOperationException(
                "Actualizar aún no implementado."); }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ControlMaquinaria control = controlMaquinariaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de Maquinaria no encontrado."));
        control.setActivo(false);
        controlMaquinariaRepository.save(control); }

    private ControlMaquinariaResponseDTO toResponse(ControlMaquinaria control) {
        List<DetalleControlMaquinariaResponseDTO> detalles =
                control.getDetalles()
                        .stream()
                        .map(detalle ->
                                DetalleControlMaquinariaResponseDTO.builder()
                                        .id(detalle.getId())
                                        .insumoId(detalle.getInsumo().getId())
                                        .nombreInsumo(detalle.getInsumo().getNombre())
                                        .cantidad(detalle.getCantidad())
                                        .build()
                        ).toList();
        return ControlMaquinariaResponseDTO.builder()
                .id(control.getId())
                .maquinaria(control.getMaquinaria().getNombre())
                .fecha(control.getFecha())
                .horasUso(control.getHorasUso())
                .operador(control.getOperador())
                .detalles(detalles)
                .build(); }
}
