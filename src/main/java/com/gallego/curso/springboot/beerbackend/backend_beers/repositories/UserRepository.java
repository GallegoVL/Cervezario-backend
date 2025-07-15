package com.gallego.curso.springboot.beerbackend.backend_beers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.UserBeers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;
import java.util.List;


@CrossOrigin(originPatterns = {"http://localhost:4200/","http://localhost:5173/"})
@RepositoryRestResource(path= "users")
public interface UserRepository extends CrudRepository<Users,Long>{

    boolean existsByUsername(String username);

    boolean existsByTlf(String tlf);

    boolean existsByEmail(String email);

    Optional<Users> findByTlf(String tlf);

     Optional<Users> findByEmail(String email);

    @EntityGraph(attributePaths = "beers")
    Optional<Users> findByUsername(String username);

    List<UserBeers> findByIdusers(Long idusers);
}
