package br.com.fiap.bluegather.service;

import br.com.fiap.bluegather.dto.MomentoDTO;
import br.com.fiap.bluegather.model.Momento;
import br.com.fiap.bluegather.repository.MomentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MomentoService {

    @Autowired
    private MomentoRepository momentoRepository;

    public Page<MomentoDTO> listAll(Pageable pageRequest) {
        Page<Momento> list = momentoRepository.findAll(pageRequest);
        return list.map(this::convertToDto);
    }

    public MomentoDTO findById(Long id) {
        Momento entity = findEntityById(id);
        return convertToDto(entity);
    }

    @Transactional
    public MomentoDTO create(MomentoDTO newData) {
        Momento entity = convertToEntity(newData);
        Momento savedEntity = momentoRepository.save(entity);
        return convertToDto(savedEntity);
    }

    @Transactional
    public MomentoDTO update(Long id, MomentoDTO updatedData) {
        findEntityById(id);
        updatedData.setId(id);
        Momento updatedEntity = convertToEntity(updatedData);    
        Momento savedEntity = momentoRepository.save(updatedEntity);
        return convertToDto(savedEntity);
    }
    
    @Transactional
    public void delete(Long id) {
        Momento entity = findEntityById(id);
        momentoRepository.delete(entity);
    }

    public Momento findEntityById(Long id) {
        return momentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "(" + getClass().getSimpleName() + ") - Momento não encontrado(a) por ID: " + id));
    }

    public Set<MomentoDTO> findByNomeContainingIgnoreCase(String nome) {
        Set<Momento> list = momentoRepository.findByNomeContainingIgnoreCase(nome);
        return list.stream().map(this::convertToDto).collect(Collectors.toSet());
    }
    
    public MomentoDTO convertToDto(Momento entity) {
        MomentoDTO dto = new MomentoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        return dto;
    }

    public Momento convertToEntity(MomentoDTO dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("(" + getClass().getSimpleName() + ") - MomentoDTO não pode ser nulo.");
        }
        Momento entity;
        if (dto.getId() != null) {
            entity = findEntityById(dto.getId());
        } else {
            entity = new Momento();
        }
        entity.setNome(dto.getNome());
        return entity;
    }
}