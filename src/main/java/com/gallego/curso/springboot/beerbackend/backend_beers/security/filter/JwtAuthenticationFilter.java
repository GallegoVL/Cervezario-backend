package com.gallego.curso.springboot.beerbackend.backend_beers.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallego.curso.springboot.beerbackend.backend_beers.entities.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.gallego.curso.springboot.beerbackend.backend_beers.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      
        Users user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Users.class);
            username=user.getUsername();
            password=user.getPassword();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override //metodo sobreescrito para autenticarnos con el token
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
                User user = (User)authResult.getPrincipal();//obtenemos el usuario pero de springsecurity
                String username = user.getUsername();
                Collection <? extends GrantedAuthority> roles = authResult.getAuthorities();
               //-----------------------------------------
               List<String> authorities = roles.stream()
               .map(GrantedAuthority::getAuthority)
               .collect(Collectors.toList()); 

                //-------------------------------------------
                Claims claims= (Claims) Jwts.claims()
                //.add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("authorities", authorities)
                .add("username",username)
                .build();
                
                
                String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))//una hora en milisegundos de acceso del token
                .issuedAt(new Date())
                .signWith(SECREET_KEY)
                .compact();//y aqui generamos el token

                response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);//lo pasamos al cliente 

                Map<String,String> body = new HashMap<>();
                body.put("token", token);
                body.put("username", username);
                body.put("message", String.format("Hola %s has iniciado sesion con exito", username));
   
                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setContentType(CONTENT_TYPE);
                response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
                Map<String,String> body = new HashMap<>(); 
                body.put("message", "Usuario o Contraseña incorrectos");
                body.put("error", failed.getMessage());

                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setContentType(CONTENT_TYPE);
                response.setStatus(401);
    }
    

    
}
