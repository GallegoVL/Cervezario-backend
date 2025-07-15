package com.gallego.curso.springboot.beerbackend.backend_beers.Dto;

public class CountryBeerCountDTO {

    private String country;
    private long count;

    public CountryBeerCountDTO(String country, long count){
        this.country=country;
        this.count=count;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
    
}
