package com.gallego.curso.springboot.beerbackend.backend_beers.Dto;

import jakarta.validation.constraints.NotBlank;

public class ContactEmailDTO {

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
    
    
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    
}
