package com.gallego.curso.springboot.beerbackend.backend_beers.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BeerWhitEtiquetasDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.BrewBeerCountDto;
import com.gallego.curso.springboot.beerbackend.backend_beers.Dto.CountryBeerCountDTO;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Beers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Role;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.UserBeers;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.UserBeersId;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;
import com.gallego.curso.springboot.beerbackend.backend_beers.mappers.BeerMapper;
import com.gallego.curso.springboot.beerbackend.backend_beers.repositories.BeerRepository;
import com.gallego.curso.springboot.beerbackend.backend_beers.repositories.RoleRepository;
import com.gallego.curso.springboot.beerbackend.backend_beers.repositories.UserBeerRepository;
import com.gallego.curso.springboot.beerbackend.backend_beers.repositories.UserRepository;

@Service
public class UserServices implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBeerRepository userBeerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BeerMapper beerMapper;

    //-------GESTION USUARIOS CRUD-------------------

    @Override
    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return (List<Users>)userRepository.findAll();
    }

    @Override
    @Transactional
    public Users save(Users user) {
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roleList = new ArrayList<>();

        optionalRoleUser.ifPresent(roleList::add);

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin=roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roleList::add);
        }

        user.setRoles(roleList);

        String passEncoded =passwordEncoder.encode( user.getPassword());
        user.setPassword(passEncoded);
        
        return userRepository.save(user);
    }


    @Transactional
    @Override
    public Optional<Users> update(Long id, Users user) {
        Optional <Users> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            Users userDB = userOptional.get();
            userDB.setName(user.getName());
            userDB.setUsername(user.getUsername());
            userDB.setTlf(user.getTlf());
            userDB.setDate(user.getDate());
            userDB.setCountry(user.getCountry());

            return Optional.of(userRepository.save(userDB));
        }
        return  userOptional;
    }

//-----------------Comprobaciones-------------------------
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByTlf(String tlf){
        return userRepository.existsByTlf(tlf);
    }
    
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean findByTlf(String tlf, Long id){
        Optional<Users> existUser = userRepository.findByTlf(tlf);

        if(existUser.isEmpty())return false;

        return !existUser.get().getIdusers().equals(id);
    }

    public boolean emailExistsForOtherUser(String email, Long userId) {
         Optional<Users> user = userRepository.findByEmail(email);
        return user != null && !user.get().getIdusers().equals(userId);
    }

    public boolean usernameExistsForOtherUser(String username, Long userId) {
         Optional<Users> user = userRepository.findByUsername(username);
        return user != null && !user.get().getIdusers().equals(userId);
    }

    public boolean tlfExistsForOtherUser(String tlf, Long userId) {
         Optional<Users> user = userRepository.findByTlf(tlf);
        return user != null && !user.get().getIdusers().equals(userId);
    }

    
//-----------GESTION CERVEZAS USUARIO-------------------------------
    

    public List<BeerWhitEtiquetasDTO> obtenerCervezasUsuario(String username){
        Users usuario = findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println(usuario.getUserBeers());
        return usuario.getUserBeers().stream()
            .map(ub -> new BeerWhitEtiquetasDTO(ub.getBeer(), ub.getEtiquetas()))
            .collect(Collectors.toList());
    }
    
    
    public long contarEtiquetasUsuario(String username){
        Users usuario =findByUsername(username)
            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
        
        return usuario.getUserBeers().stream()
            .map(ub -> ub.getEtiquetas())
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue).sum();
    }
    
    public long contarCervezasUsuario(String username){
        Users usuario = findByUsername(username)
            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
        
            return usuario.getUserBeers().size();   
    }

    @Transactional
    public Optional<Beers> deleteBeerList(Long beerId, String username){
       Optional<Users> userOptional = userRepository.findByUsername(username);
       Optional <Beers> beerSelected = beerRepository.findById(beerId);
        
       if(userOptional.isEmpty() || beerSelected.isEmpty() ){
        return Optional.empty();
       }

       Users user = userOptional.get();
       Beers beer = beerSelected.get();

       UserBeersId userBeersId = new UserBeersId(user.getIdusers(),beer.getId());

       Optional<UserBeers> userBeerOptional = userBeerRepository.findById(userBeersId);
       userBeerOptional.ifPresent(userBeer ->{
        userBeerRepository.delete(userBeer);
       });

       return beerSelected;
    }

    
    @Transactional
    public boolean updateEtiqueta(Long beerId, String username, Long nuevaEtiqueta) {
        
        Optional<Users> userOptional = userRepository.findByUsername(username);
        Optional <Beers> beerSelected = beerRepository.findById(beerId);

        if(userOptional.isPresent() && beerSelected.isPresent()){
            Users user= userOptional.get();
            Beers beer= beerSelected.get();

            Optional<UserBeers> userBeersOptional = userBeerRepository.findByUserAndBeer(user, beer);

            if(userBeersOptional.isPresent()){
                UserBeers userBeers = userBeersOptional.get();
                userBeers.setEtiquetas(nuevaEtiqueta);
                userBeerRepository.save(userBeers);
                return true; 
            }
        }
        return false;
    }

     @Transactional
    public void guardarListaCervezas (Long userId,List <BeerWhitEtiquetasDTO> cervezasDTO){
        
        Users user= userRepository.findById(userId).orElseThrow(
            ()-> new RuntimeException("User no encontrado"));

        for( BeerWhitEtiquetasDTO dto : cervezasDTO){
            Beers beer = beerRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Cerveza no encontrada"));

            UserBeersId id = new UserBeersId(userId, dto.getId());
            UserBeers userBeer = userBeerRepository.findById(id).orElse(new UserBeers());

            userBeer.setId(id);
            userBeer.setUser(user);
            userBeer.setBeer(beer);
            userBeer.setEtiquetas(dto.getEtiquetas());

            System.out.println("EEEEEELLLLLL DTOOOOOOOOOo"+dto);

            userBeerRepository.save(userBeer);
        }
        
    }

//---------- ESTADISTICAS DE USUARIO----------------------

    @Override
    public List<Beers> top3Graduation(String username) {
        Users usuario = getUserOrThrow(username);
        
        return usuario.getUserBeers().stream()
            .map(UserBeers::getBeer)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingDouble(Beers::getGraduation).reversed())
            .limit(3)
            .collect(Collectors.toList());
    }


   public List<BeerWhitEtiquetasDTO> top3Etiquetas(String username) {
        Users usuario = getUserOrThrow(username);
    
        return usuario.getUserBeers().stream()
        .filter(ub -> ub.getBeer() != null)
        .sorted(Comparator.comparingLong(UserBeers::getEtiquetas).reversed())
        .limit(3)
        .map(ub -> new BeerWhitEtiquetasDTO(ub.getBeer(), ub.getEtiquetas()))
        .collect(Collectors.toList());
    }

    
    public List<CountryBeerCountDTO> top3Country(String username){
       Users usuario = getUserOrThrow(username);
       
       return usuario.getUserBeers().stream()
        .filter(ub -> ub.getBeer() != null)
        .map(ub -> ub.getBeer().getCountry())
        .collect(Collectors.groupingBy(Function.identity(),
                Collectors.counting())).entrySet().stream()
        .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
        .limit(3)
        .map(e -> new CountryBeerCountDTO(e.getKey(),e.getValue()))
        .collect(Collectors.toList());
    }
    
    
    public CountryBeerCountDTO top1Country(String username){
        Users usuario = getUserOrThrow(username);
        
        return usuario.getUserBeers().stream()
        .filter(ub -> ub.getBeer() != null)
        .map(ub -> ub.getBeer().getCountry())
        .collect(Collectors.groupingBy(Function.identity(),
                Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> new CountryBeerCountDTO(e.getKey(), e.getValue()))
        .orElse(null);
    }

    @Override
    public BrewBeerCountDto top1Brew(String username) {
        Users usuario = getUserOrThrow(username);
        
        return usuario.getUserBeers().stream()
        .filter(ub -> ub.getBeer() != null)
        .map(ub -> ub.getBeer().getBrew())
        .collect(Collectors.groupingBy(Function.identity(),
                Collectors.counting()))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> new BrewBeerCountDto(e.getKey(), e.getValue()))
        .orElseThrow(null);
}

    public List<BrewBeerCountDto> top3Brew(String username){
        Users usuario = getUserOrThrow(username);

        return usuario.getUserBeers().stream()
            .filter(ub -> ub.getBeer() != null)
            .map(ub -> ub.getBeer().getBrew())
            .collect(Collectors.groupingBy(Function.identity(), 
            Collectors.counting())).entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(3)
            .map(e -> new BrewBeerCountDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
}

//----------UTILIDAD PRIVADA-------------

    private Users getUserOrThrow(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}