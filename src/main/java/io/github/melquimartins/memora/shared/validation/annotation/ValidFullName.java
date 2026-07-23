package io.github.melquimartins.memora.shared.validation.annotation;

import io.github.melquimartins.memora.shared.validation.validator.ValidFullNameValidator;
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
@Constraint(validatedBy = ValidFullNameValidator.class)
public @interface ValidFullName {

    String message() default "Informe nome e sobrenome válidos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
