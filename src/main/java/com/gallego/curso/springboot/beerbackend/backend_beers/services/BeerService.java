package com.gallego.curso.springboot.beerbackend.backend_beers.services;

import java.util.List;
import java.util.Optional;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;

public interface BeerService {

    List<Beers> findAll();

    Optional<Beers> findById(Long id);

    Beers save (Beers beer);

    Optional<Beers> update(Long id, Beers beer);

    Optional<Beers> delete(Long id);

    List<Beers> searchBeers(String query);

    List<Beers> searchBeersCountry(String query);
    
}
