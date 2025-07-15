package com.gallego.curso.springboot.beerbackend.backend_beers.security;

import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import com.gallego.curso.springboot.beerbackend.backend_beers.security.filter.JwtAuthenticationFilter;
import com.gallego.curso.springboot.beerbackend.backend_beers.security.filter.jwtValidationFilter;



@Configuration
public class SpringSecurityConfig {
    //Genera un componente estring con una instancia de BCrypt para codificar la pass
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


    @Bean
    AuthenticationManager authenticationManager() throws Exception{
       
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
         
       return  http.authorizeHttpRequests(auth-> auth
        .requestMatchers(   "/", "/index.html",
                            "/main-XH5PGVOQ.js",
                            "/polyfills-FFHMD2TL.js",
                            "/styles-VM25VDC6.css",
                            "/prerendered-routes.json",
                            "/3rdpartylicenses.txt",
                            "/assets/img/*",
                            "/assets/flags/*",
                            "/assets/brew/*",
                            "/userContact",
                            "/userdetails",
                            "/paginaPrincipal",
                            "/crearCerveza",
                            "/product",
                            "/userAddBeer"
                            ).permitAll()       
        .requestMatchers(HttpMethod.POST,"/users/register").permitAll()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .requestMatchers(HttpMethod.GET,"/users/check-username").permitAll()
        .requestMatchers(HttpMethod.GET,"/users/check-tlf").permitAll()
        .requestMatchers(HttpMethod.GET,"/users/usertlf").permitAll()
        .requestMatchers(HttpMethod.GET,"/users/check-email").permitAll()
        .requestMatchers(HttpMethod.GET,"/users/perfil").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.POST,"/users").permitAll()
        .requestMatchers(HttpMethod.PUT,"/users/*").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.PUT,"/users/mis-cervezas/*").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.DELETE,"/users/mis-cervezas/*").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/contar").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/contar-etiquetas").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/brew").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/brew1").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/graduation").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/etiquetas").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/country").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/users/mis-cervezas/country1").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.POST,"/cervezas").hasAnyRole("ADMIN")
        .requestMatchers(HttpMethod.PUT,"/cervezas/*").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.DELETE,"/cervezas/*").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/cervezas","/cervezas/*").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/cervezas/searchlist").permitAll()
        .requestMatchers(HttpMethod.GET,"/cervezas/searchlist/country").permitAll()
        .requestMatchers(HttpMethod.GET,"/cervezas/cantidad/contar").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.GET,"/cervezas/cantidad/contar-etiquetas").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.POST,"/contact/email").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .anyRequest().authenticated()
        )
        
        .csrf(csrf -> csrf.disable()) 
        .cors(cors -> cors.configurationSource(configurationSource()))
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilterBefore(new jwtValidationFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
        
    }

    @Bean
    CorsConfigurationSource configurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://localhost:8080")); 
    config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
    config.setAllowCredentials(true); 

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}

 
}

   