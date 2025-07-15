package com.gallego.curso.springboot.beerbackend.backend_beers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SpaController {
    
    @RequestMapping(value = {
        "/userContact", 
        "/paginaPrincipal", 
        "/userdetails",
        "/product", // agrega todas las rutas Angular aquí
        "/users/**",  // si usas rutas más complejas puedes usar wildcard
        "/cervezas/**",
        "/userAddBeer",
        "/crearCerveza"
        // ... otras rutas que maneja Angular, pero no el backend
    })
    public String forwardToIndex() {
        // Devuelve index.html para que Angular maneje la ruta
        return "forward:/index.html";
    }
}
