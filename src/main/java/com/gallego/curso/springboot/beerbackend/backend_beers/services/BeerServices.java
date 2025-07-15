package com.gallego.curso.springboot.beerbackend.backend_beers.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.repositories.BeerRepository;

@Service
public class BeerServices implements BeerService{

    @Autowired
    private BeerRepository beerRepository;

    

    @Transactional(readOnly = true)
    @Override
    public List<Beers> findAll() {
        return (List<Beers>)beerRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Beers> findById(Long id) {
        return beerRepository.findById(id);
    }

    @Transactional
    @Override
    public Beers save(Beers beer) {
        return beerRepository.save(beer);
    }

    @Transactional
    @Override
    public Optional<Beers> update(Long id, Beers beer) {
        Optional <Beers> beerOptional = beerRepository.findById(id);
        if (beerOptional.isPresent()){
            Beers beerDb = beerOptional.orElseThrow();
            beerDb.setName(beer.getName());
            beerDb.setCountry(beer.getCountry());
            beerDb.setBrew(beer.getBrew());
            beerDb.setTipo(beer.getTipo());
            beerDb.setGraduation(beer.getGraduation());
            return Optional.of(beerRepository.save(beerDb));
        }
        return  beerOptional;
    }

    @Transactional
    @Override
    public Optional<Beers> delete(Long id) {
        Optional <Beers> beerOptional = beerRepository.findById(id);
        beerOptional.ifPresent(beerDb ->{
        beerRepository.delete(beerDb);
        });
        return  beerOptional;
    }

    public List<Beers> searchBeers(String query){
        return beerRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Beers> searchBeersCountry(String query){
        return beerRepository.findByCountryContainingIgnoreCase(query);
    }
}
