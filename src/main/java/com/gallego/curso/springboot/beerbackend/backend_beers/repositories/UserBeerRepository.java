package com.gallego.curso.springboot.beerbackend.backend_beers.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.UserBeers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.UserBeersId;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;

public interface UserBeerRepository extends JpaRepository<UserBeers, UserBeersId> {

    List<UserBeers> findByUserIdusers(Long userId);
    
    Optional<UserBeers> findByUserAndBeer(Users user,Beers beer);
}
