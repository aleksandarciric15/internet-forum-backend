package com.example.internetforum.models;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class XSSValidator implements ConstraintValidator<XSSValid, String> {

    @Override
    public void initialize(XSSValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private final List<Pattern> XSS_PATTERNS = Arrays.asList(
            Pattern.compile("<\\s*script", Pattern.CASE_INSENSITIVE),
            Pattern.compile("</\\s*script", Pattern.CASE_INSENSITIVE),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("alert\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("document\\.cookie", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseover=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onerror=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<\\s*img\\s*src=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("eval\\(", Pattern.CASE_INSENSITIVE)
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        for (Pattern pattern : XSS_PATTERNS){
            if (pattern.matcher(value).find()){
                return false;
            }
        }
        return true;
    }

}
