package io.github.melquimartins.memora.shared.validation.validator;

import io.github.melquimartins.memora.shared.validation.annotation.ValidFullName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ValidFullNameValidator
      implements ConstraintValidator<ValidFullName, String>
{

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    String[] parts = value.trim().split("\\s+");

    if (parts.length < 2) {
      return false;
    }

    return Arrays.stream(parts).allMatch(part -> part.length() >= 2);
  }

}
