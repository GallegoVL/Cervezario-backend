package com.gallego.curso.springboot.beerbackend.backend_beers.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.ContactEmailDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.services.EmailServices;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8080"})
public class EmailController {

    @Autowired
    private EmailServices emailServices;

    @PostMapping("/email")
    public ResponseEntity<Map<String,String>> contactEmail(@RequestBody ContactEmailDTO emailDTO){

        emailServices.sendContactMail(emailDTO.getSubject(), emailDTO.getText());
        return ResponseEntity.ok(Map.of("message","Email de contacto enviado con exito"));
    }
}
