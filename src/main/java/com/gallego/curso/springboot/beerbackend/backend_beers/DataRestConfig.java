package com.gallego.curso.springboot.beerbackend.backend_beers;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer{

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
      config.exposeIdsFor(Beers.class);
      config.exposeIdsFor(Users.class);
    }
    //Este metodo sirve simplemente para poder visualizar el id en el Json,muy necesario para el fronted de React 
    
}
