package com.example.internetforum.models;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = XSSValidator.class)
public @interface XSSValid {
    String message() default "Field potentially has dangerous characters!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
