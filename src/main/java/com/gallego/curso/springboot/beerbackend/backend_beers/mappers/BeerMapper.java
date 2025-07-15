package com.gallego.curso.springboot.beerbackend.backend_beers.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BeerWhitEtiquetasDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;

import io.micrometer.common.lang.Nullable;

@Mapper (componentModel="spring", nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT )
public interface BeerMapper {
    BeerWhitEtiquetasDTO toDto(@Nullable Beers beer);
    
}
