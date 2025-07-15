package com.gallego.curso.springboot.beerbackend.backend_beers.services;

import java.util.List;
import java.util.Optional;

import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BeerWhitEtiquetasDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BrewBeerCountDto;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.CountryBeerCountDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;

public interface UserService {

    List<Users> findAll();

    Users save(Users user);

    Optional<Users> update(Long id, Users user);

    boolean existsByUsername(String username);
    
    boolean existsByTlf(String tlf);
    
    boolean findByTlf(String tlf,Long id);

    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username);

    long contarEtiquetasUsuario(String username);

    long contarCervezasUsuario(String username);

    List<BeerWhitEtiquetasDTO> obtenerCervezasUsuario(String username);

    List<Beers> top3Graduation(String username);

    List<BeerWhitEtiquetasDTO> top3Etiquetas(String username);

    List<CountryBeerCountDTO> top3Country(String username);

    CountryBeerCountDTO top1Country(String username);

    BrewBeerCountDto top1Brew(String username);

    List<BrewBeerCountDto> top3Brew(String username);

    void guardarListaCervezas (Long userId,List <BeerWhitEtiquetasDTO> cervezasDTO);

    Optional<Beers> deleteBeerList(Long id, String username);

    boolean updateEtiqueta (Long beerId, String username, Long nuevaEtiqueta);
}
