package com.gallego.curso.springboot.beerbackend.backend_beers.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface IsRequired {

    String message() default "Es requerido";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
