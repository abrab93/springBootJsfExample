package com.example.springbootjsfexample.mapper;

import com.example.springbootjsfexample.dto.TypeDto;
import com.example.springbootjsfexample.model.Type;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TypeMapper {

    TypeDto mapTypeToTypeDto (Type type);
    Type mapTypeDtoToType (TypeDto typeDto);
    List<TypeDto> mapTypeListToTypeDtoList(Iterable<Type> typesIterable);
}
