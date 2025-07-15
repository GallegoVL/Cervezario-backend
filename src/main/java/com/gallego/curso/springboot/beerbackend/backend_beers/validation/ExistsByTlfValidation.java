package com.gallego.curso.springboot.beerbackend.backend_beers.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gallego.curso.springboot.beerbackend.backend_beers.services.UserServices;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByTlfValidation implements ConstraintValidator<ExistsByTlf,String> {

   @Autowired
    private UserServices userService;

   @Override
    public boolean isValid(String tlf, ConstraintValidatorContext context) {
        if(userService==null){
            return true;
          }
    return !userService.existsByTlf(tlf);
    }

    
}


