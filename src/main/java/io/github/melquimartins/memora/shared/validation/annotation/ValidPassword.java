package io.github.melquimartins.memora.shared.validation.annotation;

import io.github.melquimartins.memora.shared.validation.validator.ValidPasswordValidator;
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
@Constraint(validatedBy = ValidPasswordValidator.class)
public @interface ValidPassword {
  String message() default "Informe uma senha válida.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
