package io.github.melquimartins.memora.shared.validation.validator;

import io.github.melquimartins.memora.shared.validation.annotation.NotBlankIfPresent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator
        implements ConstraintValidator<NotBlankIfPresent, String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {

        if (value == null) {
            return true;
        }

        return !value.trim().isEmpty();
    }

}
