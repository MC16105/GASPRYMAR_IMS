package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ControlEstanqueRequestDTO;
import com.graspymar.ims.dto.ControlEstanqueResponseDTO;
import com.graspymar.ims.dto.DetalleControlEstanqueRequestDTO;
import com.graspymar.ims.dto.DetalleControlEstanqueResponseDTO;
import com.graspymar.ims.entity.ControlEstanque;
import com.graspymar.ims.entity.DetalleControlEstanque;
import com.graspymar.ims.entity.Estanque;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.repository.ControlEstanqueRepository;
import com.graspymar.ims.repository.EstanqueRepository;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.service.ControlEstanqueService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlEstanqueServiceImpl implements ControlEstanqueService {

    private final ControlEstanqueRepository controlEstanqueRepository;
    private final EstanqueRepository estanqueRepository;
    private final InsumoRepository insumoRepository;

    @Override
    @Transactional
    public ControlEstanqueResponseDTO crear(ControlEstanqueRequestDTO dto) {

        Estanque estanque = estanqueRepository.findById(dto.getEstanqueId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Estanque no encontrado."));

        ControlEstanque control = ControlEstanque.builder()
                .estanque(estanque)
                .fecha(dto.getFecha())
                .observaciones(dto.getObservaciones())
                .build();

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe registrar al menos un detalle."
            );
        }

        for (DetalleControlEstanqueRequestDTO detalleDTO : dto.getDetalles()) {

            Insumo insumo = insumoRepository.findById(detalleDTO.getInsumoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException("Insumo no encontrado."));

            if (insumo.getStockActual() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el insumo: "
                                + insumo.getNombre());
            }

            insumo.setStockActual(
                    insumo.getStockActual() - detalleDTO.getCantidad()
            );

            insumoRepository.save(insumo);

            DetalleControlEstanque detalle = DetalleControlEstanque.builder()
                    .controlEstanque(control)
                    .insumo(insumo)
                    .cantidad(detalleDTO.getCantidad())
                    .build();

            control.getDetalles().add(detalle);
        }

        ControlEstanque guardado = controlEstanqueRepository.save(control);

        return toResponse(guardado);
    }

    @Override
    public List<ControlEstanqueResponseDTO> listar() {

        return controlEstanqueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ControlEstanqueResponseDTO buscarPorId(Long id) {

        ControlEstanque control = controlEstanqueRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de estanque no encontrado."));

        return toResponse(control);
    }

    @Override
    @Transactional
    public ControlEstanqueResponseDTO actualizar(
            Long id,
            ControlEstanqueRequestDTO dto) {

        throw new UnsupportedOperationException(
                "Actualizar aún no implementado.");
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        ControlEstanque control = controlEstanqueRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de estanque no encontrado."));

        control.setActivo(false);

        controlEstanqueRepository.save(control);
    }

    private ControlEstanqueResponseDTO toResponse(ControlEstanque control) {

        List<DetalleControlEstanqueResponseDTO> detalles =
                control.getDetalles()
                        .stream()
                        .map(detalle ->
                                DetalleControlEstanqueResponseDTO.builder()
                                        .id(detalle.getId())
                                        .insumoId(detalle.getInsumo().getId())
                                        .nombreInsumo(detalle.getInsumo().getNombre())
                                        .cantidad(detalle.getCantidad())
                                        .build()
                        ).toList();

        return ControlEstanqueResponseDTO.builder()
                .id(control.getId())
                .estanque(control.getEstanque().getNombre())
                .fecha(control.getFecha())
                .observaciones(control.getObservaciones())
                .detalles(detalles)
                .build();
    }
}
