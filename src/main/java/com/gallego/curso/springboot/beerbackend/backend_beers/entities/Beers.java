package com.gallego.curso.springboot.beerbackend.backend_beers.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gallego.curso.springboot.beerbackend.backend_beers.validation.IsRequired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name= "cervezas")
public class Beers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IsRequired (message = "{IsRequired.beers.name}")
    private String name; 
    
    @NotBlank (message = "{IsRequired.beers.country}" )
    private String country;
    
    @NotBlank (message = "{IsRequired.beers.brew}" )
    private String brew;
    
    @NotBlank (message = "{IsRequired.beers.tipo}" )
    private String tipo;
    
    
    @Min(value=1,message = "{Min.beers.graduation}" ) 
    @Max(value=99, message = "{Max.beers.graduation}") 
    private double graduation;

    @OneToMany(
        mappedBy = "beer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<UserBeers> userBeers = new ArrayList<>();
    
    private long etiquetas;
    
    public long getEtiquetas() {
        return etiquetas;
    }
    public void setEtiquetas(long etiquetas) {
        this.etiquetas = etiquetas;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getBrew() {
        return brew;
    }
    public void setBrew(String brew) {
        this.brew = brew;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public double getGraduation() {
        return graduation;
    }
    public void setGraduation(double graduation) {
        this.graduation = graduation;
    }
    public List<UserBeers> getUserBeers() {
        return userBeers;
    }
    public void setUserBeers(List<UserBeers> userBeers) {
        this.userBeers = userBeers;
    }
   
}
