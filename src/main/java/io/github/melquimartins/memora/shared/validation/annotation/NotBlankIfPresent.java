package io.github.melquimartins.memora.shared.validation.annotation;

import io.github.melquimartins.memora.shared.validation.validator.NotBlankIfPresentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.RECORD_COMPONENT
})
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
public @interface NotBlankIfPresent {

    String message() default "O campo não pode conter apenas espaços.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
