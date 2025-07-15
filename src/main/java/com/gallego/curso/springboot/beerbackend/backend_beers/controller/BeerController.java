package com.gallego.curso.springboot.beerbackend.backend_beers.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.services.BeerServices;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/cervezas")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8080"})
public class BeerController {

    @Autowired
    private BeerServices service;

    @GetMapping
    public List<Beers> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        Optional<Beers> beerOptional = service.findById(id);
        if(beerOptional.isPresent()){
            return ResponseEntity.ok(beerOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/searchlist")
    public ResponseEntity<List<Beers>> searchBeers(@RequestParam String query){
        List<Beers> beers = service.searchBeers(query);
        System.out.println(beers);
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/searchlist/country")
    public ResponseEntity<List<Beers>> searchCountryBeers(@RequestParam String query){
        List<Beers> beers = service.searchBeersCountry(query);
        return ResponseEntity.ok(beers);
    }

    @PostMapping
    public ResponseEntity<?> create ( @Valid @RequestBody Beers beer, BindingResult result){
       

        if(result.hasFieldErrors()){
            return validation(result);
        }
        
        Beers beerDB = service.save(beer);
        return ResponseEntity.status(HttpStatus.CREATED).body(beerDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update ( @PathVariable Long id, @Valid @RequestBody Beers beer,BindingResult result){
       
        if(result.hasFieldErrors()){
            return validation(result);
        }
        
        Optional<Beers> beerOptional = service.update(id,beer);
        if (beerOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(beerOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
       
        Optional<Beers> beerOptional = service.delete(id);
        if(beerOptional.isPresent()){
            return ResponseEntity.ok(beerOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map <String,String> errors= new HashMap<>();

        result.getFieldErrors().forEach(err->{
            errors.put(err.getField(),err.getDefaultMessage());
            
        });

        return ResponseEntity.badRequest().body(errors);
    }


}
