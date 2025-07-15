package com.gallego.curso.springboot.beerbackend.backend_beers.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Role;

@CrossOrigin(originPatterns = {"http://localhost:4200/","http://localhost:5173/"})
@RepositoryRestResource(path= "roles")//table de la app
public interface RoleRepository extends CrudRepository<Role,Long>{

    Optional<Role> findByName(String name);

}
