package com.mashreq.conferencebooking.config.validator;

import com.mashreq.conferencebooking.config.validator.MaxRoomSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxRoomSizeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxRoomSize {

    String message() default "Max room size exceeded";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}