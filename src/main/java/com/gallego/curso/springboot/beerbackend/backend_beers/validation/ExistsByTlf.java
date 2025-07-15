package com.gallego.curso.springboot.beerbackend.backend_beers.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsByTlfValidation.class)
public @interface ExistsByTlf {

     String message() default "Ya hay un usuario registrado con este telefono";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
