package com.krishnadev.InventoryManagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BaseObjectTranslator {

    protected ModelMapper mapper;

    public BaseObjectTranslator(@Qualifier("modelMapper") ModelMapper mapper) {
        this.mapper = mapper;
    }

    // @SuppressWarnings({"unchecked", "rawtypes"})
    public <T, R> R map(T entity, Class<R> targetType) {
        return mapper.map(entity, targetType);
    }

    public <T, R> List<R> mapToList(List<T> entities, Class<R> targetType) {
        return entities.stream().map(source -> map(source, targetType)).collect(Collectors.toList());
    }
}
