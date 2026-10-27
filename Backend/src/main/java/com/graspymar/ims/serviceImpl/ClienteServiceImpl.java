package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ClienteRequestDTO;
import com.graspymar.ims.dto.ClienteResponseDTO;
import com.graspymar.ims.entity.Cliente;
import com.graspymar.ims.repository.ClienteRepository;
import com.graspymar.ims.service.ClienteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    // =====================================================
    // CREAR CLIENTE
    // =====================================================

    @Override
    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {

        // Verificar que el DUI/NIT no esté registrado
        clienteRepository.findByDuiNit(dto.getDuiNit())
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Ya existe un cliente con ese DUI/NIT."
                    );
                });

        Cliente cliente = Cliente.builder()
                .nombreRazonSocial(dto.getNombreRazonSocial())
                .duiNit(dto.getDuiNit())
                .nrc(dto.getNrc())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .build();

        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    // =====================================================
    // LISTAR CLIENTES ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {

        return clienteRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR CLIENTE POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado."
                        )
                );

        // Impedir consultar clientes inactivos
        validarClienteActivo(cliente);

        return toResponse(cliente);
    }

    // =====================================================
    // ACTUALIZAR CLIENTE
    // =====================================================

    @Override
    @Transactional
    public ClienteResponseDTO actualizar(
            Long id,
            ClienteRequestDTO dto
    ) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado."
                        )
                );

        // Impedir modificar clientes inactivos
        validarClienteActivo(cliente);

        // Verificar que el DUI/NIT no pertenezca a otro cliente
        clienteRepository.findByDuiNit(dto.getDuiNit())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "El DUI/NIT ya pertenece a otro cliente."
                        );
                    }
                });

        cliente.setNombreRazonSocial(dto.getNombreRazonSocial());
        cliente.setDuiNit(dto.getDuiNit());
        cliente.setNrc(dto.getNrc());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());

        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    // =====================================================
    // ELIMINAR CLIENTE (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente no encontrado."
                        )
                );

        // Evitar eliminar dos veces el mismo cliente
        if (!Boolean.TRUE.equals(cliente.getActivo())) {
            throw new IllegalArgumentException(
                    "El cliente ya se encuentra inactivo."
            );
        }

        // Eliminación lógica
        cliente.setActivo(false);

        clienteRepository.save(cliente);
    }

    // =====================================================
    // VALIDAR CLIENTE ACTIVO
    // =====================================================

    private void validarClienteActivo(Cliente cliente) {

        if (!Boolean.TRUE.equals(cliente.getActivo())) {
            throw new EntityNotFoundException(
                    "El cliente no se encuentra activo."
            );
        }
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private ClienteResponseDTO toResponse(Cliente cliente) {

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nombreRazonSocial(cliente.getNombreRazonSocial())
                .duiNit(cliente.getDuiNit())
                .nrc(cliente.getNrc())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .build();
    }
}