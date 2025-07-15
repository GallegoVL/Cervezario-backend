package com.gallego.curso.springboot.beerbackend.backend_beers.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import java.util.List;


//@CrossOrigin(originPatterns = "http://localhost:5173/")
@CrossOrigin(originPatterns = {"http://localhost:4200/","http://localhost:5173/"})
@RepositoryRestResource(path= "cervezas")//table de la app
public interface BeerRepository extends CrudRepository<Beers, Long>{

  @Query("SELECT c FROM Beers c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
  List<Beers> findByNameContainingIgnoreCase(@Param("query") String query);

  @Query("SELECT c FROM Beers c WHERE LOWER(c.country) LIKE LOWER(CONCAT('%', :query, '%'))")
  List<Beers> findByCountryContainingIgnoreCase(@Param("query") String query);


}
