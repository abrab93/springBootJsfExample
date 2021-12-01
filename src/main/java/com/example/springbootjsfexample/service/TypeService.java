package com.example.springbootjsfexample.service;

import com.example.springbootjsfexample.dto.TypeDto;
import com.example.springbootjsfexample.mapper.TypeMapper;
import com.example.springbootjsfexample.model.Type;
import com.example.springbootjsfexample.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;

    public TypeDto createOrUpdate(TypeDto typeDto) {
        // TODO start logging
        Type saveType = typeRepository.save(typeMapper.mapTypeDtoToType(typeDto));
        // TODO end  logging
        return typeMapper.mapTypeToTypeDto(saveType);
    }

    public void delete(TypeDto typeDto) {
        // TODO start logging
        typeRepository.delete(typeMapper.mapTypeDtoToType(typeDto));
        // TODO end  logging
    }

    public List<TypeDto> findAll() {
        // TODO start logging
            Iterable<Type> typesIterable = typeRepository.findAll();
        return typeMapper.mapTypeListToTypeDtoList(IteratorUtils.toList(typesIterable.iterator()));
        // TODO end  logging
    }

    public TypeDto findById(Integer id) {
        // TODO start logging
        return typeMapper.mapTypeToTypeDto(typeRepository.findById(id).orElse(null));
        // TODO end  logging
    }
}
