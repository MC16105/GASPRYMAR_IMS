package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.MaquinariaRequestDTO;
import com.graspymar.ims.dto.MaquinariaResponseDTO;
import com.graspymar.ims.entity.Maquinaria;
import com.graspymar.ims.repository.MaquinariaRepository;
import com.graspymar.ims.service.MaquinariaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquinariaServiceImpl implements MaquinariaService {

    private final MaquinariaRepository maquinariaRepository;

    @Override
    public MaquinariaResponseDTO crear(MaquinariaRequestDTO dto) {
        maquinariaRepository.findByCodigo(dto.getCodigo())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe una Maquina con ese Codigo");
                });
        Maquinaria maquinaria = Maquinaria.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .tipo(dto.getTipo())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anio(dto.getAnio())
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .build();
        maquinaria = maquinariaRepository.save(maquinaria);
        return toResponse(maquinaria); }

    @Override
    public List<MaquinariaResponseDTO> listar() {
        return maquinariaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList(); }

    @Override
    public MaquinariaResponseDTO buscarPorId(Long id) {
        Maquinaria maquinaria = maquinariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maquinaria no encontrada."));
        return toResponse(maquinaria); }

    @Override
    public MaquinariaResponseDTO actualizar(Long id, MaquinariaRequestDTO dto) {
        Maquinaria maquinaria = maquinariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maquinaria no encontrada."));
        maquinaria.setNombre(dto.getNombre());
        maquinaria.setCodigo(dto.getCodigo());
        maquinaria.setTipo(dto.getTipo());
        maquinaria.setMarca(dto.getMarca());
        maquinaria.setModelo(dto.getModelo());
        maquinaria.setAnio(dto.getAnio());
        maquinaria.setEstado(dto.getEstado());
        maquinaria.setObservaciones(dto.getObservaciones());
        maquinaria = maquinariaRepository.save(maquinaria);
        return toResponse(maquinaria); }

    @Override
    public void eliminar(Long id) {
        Maquinaria maquinaria = maquinariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maquinaria no encontrada."));
        maquinaria.setActivo(false);
        maquinariaRepository.save(maquinaria); }

    private MaquinariaResponseDTO toResponse(Maquinaria maquinaria) {
        return MaquinariaResponseDTO.builder()
                .id(maquinaria.getId())
                .nombre(maquinaria.getNombre())
                .codigo(maquinaria.getCodigo())
                .tipo(maquinaria.getTipo())
                .marca(maquinaria.getMarca())
                .modelo(maquinaria.getModelo())
                .anio(maquinaria.getAnio())
                .estado(maquinaria.getEstado())
                .observaciones(maquinaria.getObservaciones())
                .build();
    }
}
