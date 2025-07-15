package com.gallego.curso.springboot.beerbackend.backend_beers.Dto;

public class BrewBeerCountDto {

    private String brew;
    private long count;

    public BrewBeerCountDto(String brew, long count){
        this.brew=brew;
        this.count=count;
    }

    public String getBrew() {
        return brew;
    }

    public void setBrew(String brew) {
        this.brew = brew;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
    
}
