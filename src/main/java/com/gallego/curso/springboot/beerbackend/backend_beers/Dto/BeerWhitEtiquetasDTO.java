package com.gallego.curso.springboot.beerbackend.backend_beers.Dto;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;

public class BeerWhitEtiquetasDTO {

    private Long id;
    private String name;
    private String country;
    private String brew;
    private String tipo;
    private double graduation;
    private long etiquetas;
    
    public BeerWhitEtiquetasDTO(Beers beer, long etiquetas){
        if (beer==null) throw new IllegalArgumentException("Beer is null");
        this.id = beer.getId();
        this.name = beer.getName();
        this.country = beer.getCountry();
        this.brew = beer.getBrew();
        this.tipo = beer.getTipo();
        this.graduation = beer.getGraduation();
        this.etiquetas = etiquetas;
    }

    public BeerWhitEtiquetasDTO(){
        
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

    public long getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(long etiquetas) {
        this.etiquetas = etiquetas;
    }
    
}
