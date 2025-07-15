package com.gallego.curso.springboot.beerbackend.backend_beers.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){//  hacemos que inyecte el authorities en vez del role, sino no funciona

    }
}
