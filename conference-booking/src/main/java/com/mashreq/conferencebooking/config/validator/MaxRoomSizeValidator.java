package com.mashreq.conferencebooking.config.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("file:application.yaml")
public class MaxRoomSizeValidator implements ConstraintValidator<MaxRoomSize, Integer> {

    @Value("${app.maxRoomSize}")
    int maxSize;
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer <= maxSize;
    }
}
