package io.github.melquimartins.memora.shared.validation.validator;

import io.github.melquimartins.memora.shared.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidPasswordValidator
      implements ConstraintValidator<ValidPassword, String>
{
  // Pelo menos uma letra maiúscula
  private final Pattern UPPERCASE = Pattern.compile("[A-Z]");

  // Pelo menos uma letra minúscula
  private final Pattern LOWERCASE = Pattern.compile("[a-z]");

  // Pelo menos um número
  private final Pattern NUMBER = Pattern.compile("[0-9]");

  // Pelo menos um caractere especial
  private final Pattern SPECIAL_CHAR = Pattern
        .compile("[!@#$%&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return true;
    }

    if (!UPPERCASE.matcher(value).find()) {
      return false;
    }

    if (!LOWERCASE.matcher(value).find()) {
      return false;
    }

    if (!NUMBER.matcher(value).find()) {
      return false;
    }

    return SPECIAL_CHAR.matcher(value).find();
  }

}
