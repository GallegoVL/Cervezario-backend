package com.gallego.curso.springboot.beerbackend.backend_beers.security.filter;

import static com.gallego.curso.springboot.beerbackend.backend_beers.security.TokenJwtConfig.*;

import java.util.List;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gallego.curso.springboot.beerbackend.backend_beers.security.TokenJwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class jwtValidationFilter extends OncePerRequestFilter{

    public jwtValidationFilter() {
        
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain chain)
            throws IOException, ServletException {
           
            System.out.println("Filtro ejecutado");

            String path = request.getServletPath();
        if (path.equals("/users/register") || path.equals("/login")
            || path.equals("/") || path.equals("/index.html")
            || path.startsWith("/static/")  // normalmente no es accesible directamente pero si lo usas déjalo
            || path.startsWith("/dist/angular-app/")
            || path.startsWith("/assets/")
            || path.startsWith("/css/")
            || path.startsWith("/js/")
            || path.startsWith("/styles-") 
            || path.startsWith("/main-") 
            || path.startsWith("/polyfills-") 
            || path.startsWith("/runtime-") 
            || path.startsWith("/vendor-") 
            || path.startsWith("/assets/") 
            || path.equals("/") ){
            chain.doFilter(request, response);
             return;
}

           Enumeration<String> headerNames = request.getHeaderNames();
           while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            System.out.println("Header"+headerName+"="+request.getHeader(headerName));
           }    
           String header  = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
           System.out.println("JWT recibido"+ header);
           
           if (header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)){
            chain.doFilter(request, response);
            return;
           }

           String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
           
           try {
            Claims claims = Jwts.parser().verifyWith(TokenJwtConfig.SECREET_KEY).build().parseSignedClaims(token).getPayload();
              
            String username = claims.getSubject();//es getSubject por que es el nombre que tiene al crear el token     
           
            List<String> authoritiesClaims = claims.get("authorities", List.class);
            Collection<? extends GrantedAuthority> authorities = authoritiesClaims.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
                    
            System.out.println("Roles extraIDOS"+ claims);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null ,authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String,String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token JWT no es valido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }

     }
    
}
