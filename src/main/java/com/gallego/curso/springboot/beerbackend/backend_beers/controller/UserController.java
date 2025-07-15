package com.gallego.curso.springboot.beerbackend.backend_beers.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BeerWhitEtiquetasDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BrewBeerCountDto;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.CountryBeerCountDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.UpdateEtiquetasDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.UpdateUser;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;
import com.gallego.curso.springboot.beerbackend.backend_beers.services.EmailServices;
import com.gallego.curso.springboot.beerbackend.backend_beers.services.UserServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8080"})
public class UserController {

    @Autowired
    private UserServices userService;

    @Autowired
    private EmailServices emailService;

//-------VALIDACIONES----------------------------------------------
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
    boolean exists = userService.existsByUsername(username);
    return ResponseEntity.ok(exists);
}
    @GetMapping("/check-tlf")
    public ResponseEntity<Boolean> checkTlf(@RequestParam String tlf) {
    boolean exists = userService.existsByTlf(tlf);
    return ResponseEntity.ok(exists);
}

    @GetMapping("/usertlf")
    public ResponseEntity<Boolean> getUserByTlf(@RequestParam String tlf,@RequestParam(required = false) Long id) {
       
       boolean exists;
       
       if(id == null){
        exists = userService.existsByTlf(tlf);
       }else{
        exists = userService.findByTlf(tlf, id);
       }
       return ResponseEntity.ok(exists);
    }
    

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
    boolean exists = userService.existsByEmail(email);
    return ResponseEntity.ok(exists);
}


//----------PERFIL-------------------

    @GetMapping("/perfil")
    public ResponseEntity<?> perfil (Authentication authentication){
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else if(principal instanceof String){
            username = (String) principal;
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no validado");
        }

        Optional<Users> userOpt = userService.findByUsername(username);
        if(userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        Users user = userOpt.get();
        return ResponseEntity.ok(user);
}
    
//---------CRUD--USUARIOS--------------
    @GetMapping
    public List<Users> list(){
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Users user, BindingResult result){
        if(result.hasFieldErrors()){
            return validation(result);
        }
        Users userDB = userService.save(user);

        try{
            emailService.sendWelcomeMail(userDB.getEmail(), userDB.getName());
        }catch(Exception e){
            System.err.println("Error al enviar el email" + e.getMessage());
        }
       return ResponseEntity.status(HttpStatus.CREATED).body(userDB);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Users user, BindingResult result){
       
        user.setAdmin(false);
        return create(user, result);    
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map <String,String> errors= new HashMap<>();

        result.getFieldErrors().forEach(err->{
            errors.put(err.getField(),err.getDefaultMessage());
            
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update ( 
        @PathVariable Long id, 
        @Validated(UpdateUser.class) 
        @RequestBody Users user,BindingResult result){
       
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }
        
        if(userService.emailExistsForOtherUser(user.getEmail(), id)){
             return ResponseEntity.badRequest().body("Email ya registrado");
        }
         if (userService.usernameExistsForOtherUser(user.getUsername(), id)) {
            return ResponseEntity.badRequest().body("Username ya registrado");
        }
        if (userService.tlfExistsForOtherUser(user.getTlf(), id)) {
            return ResponseEntity.badRequest().body("Teléfono ya registrado");
        }

        Optional<Users> userOptional = userService.update(id,user);
        if (userOptional.isPresent()){
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

//----------------------CERVEZAS DEL USUARIO-----------------------------------

    @GetMapping("/mis-cervezas")
    public List<BeerWhitEtiquetasDTO> obtenerCervezasUsuario(Authentication authentication){
        String username = authentication.getName();
        return userService.obtenerCervezasUsuario(username);    
    }

    @DeleteMapping("/mis-cervezas/{beerId}")
    public ResponseEntity<Map<String, String>> deleteBeer(@PathVariable Long beerId, Authentication authentication){

        if(authentication == null || !authentication.isAuthenticated()){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","No autenticado"));
        }

        Object principal = authentication.getPrincipal();
        String username;
        if(principal instanceof UserDetails){
            username= ((UserDetails)principal).getUsername();
        }else if(principal instanceof String){
            username = (String) principal;
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Error de autorización"));
        }

        Optional <Beers> deleted = userService.deleteBeerList(beerId, username);

         if (deleted.isPresent()) {
            return ResponseEntity.ok(Map.of("message","Cerveza eliminada de la lista"));
        
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Cerveza o usuario no encontrado"));
        }
    }

    @PutMapping("/mis-cervezas")
    public ResponseEntity<Map<String, String>> guardarCervezas(@RequestBody List<BeerWhitEtiquetasDTO> beers, Authentication authentication){
        
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","No autenticado"));
        }

        Object principal = authentication.getPrincipal();
        Long userId;
        String username;

        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else if (principal instanceof String){
            username = (String) principal;
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Error autorizacion en el guardarCervezas"));
        }

        Users user = userService.findByUsername(username).orElseThrow(
            ()-> new RuntimeException("Usuario no encontrado en guardarCervezas"));
        userId= user.getIdusers();

        System.out.println("DTO recibido: " + beers);
        userService.guardarListaCervezas(userId, beers);
        return ResponseEntity.ok(Map.of("message","Cervezas guardadas correctamente"));
    }

    @PutMapping("/mis-cervezas/{beerId}")
    public ResponseEntity<?> updateEtiquetaUserBeer(@PathVariable Long beerId, @RequestBody UpdateEtiquetasDTO updateDTO, Authentication authentication){
        String username = authentication.getName();
        boolean actualizado = userService.updateEtiqueta(beerId, username, updateDTO.getEtiquetas());

        if(actualizado){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cerveza no encontrada");
        }
    }   

//------ESTADISTICAS------------------------------------
    
    @GetMapping("/mis-cervezas/contar")
    public long contarCervezasUsuario(Authentication authentication){
        String username = authentication.getName();
        return userService.contarCervezasUsuario(username);   
    }

    @GetMapping("mis-cervezas/contar-etiquetas")
    public long contarEtiquetasUsuario(Authentication authentication){
        String username = authentication.getName();
        return userService.contarEtiquetasUsuario(username);
    }
    
    @GetMapping("mis-cervezas/graduation")
    public List<Beers> top3Graduation(Authentication authentication){
        String username = authentication.getName();
        return userService.top3Graduation(username);
    }

    @GetMapping("mis-cervezas/etiquetas")
    public List<BeerWhitEtiquetasDTO> top3Etiquetas(Authentication authentication){
        String username = authentication.getName();
        return userService.top3Etiquetas(username);
    }
    
    @GetMapping("mis-cervezas/country")
    public List<CountryBeerCountDTO> top3Country(Authentication authentication){
        String username = authentication.getName();
        return userService.top3Country(username);
    }
     @GetMapping("mis-cervezas/country1")
    public CountryBeerCountDTO top1Country(Authentication authentication){
        String username = authentication.getName();
        return userService.top1Country(username);
    }

    @GetMapping("mis-cervezas/brew")
    public List<BrewBeerCountDto>top3Brew(Authentication authentication){
        String username = authentication.getName();
        return userService.top3Brew(username);
    }

    @GetMapping("mis-cervezas/brew1")
    public BrewBeerCountDto top1Brew(Authentication authentication){
        String username = authentication.getName();
        return userService.top1Brew(username);
    }


   

    
}
