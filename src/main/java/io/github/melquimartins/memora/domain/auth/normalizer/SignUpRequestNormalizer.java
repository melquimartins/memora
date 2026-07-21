package io.github.melquimartins.memora.domain.auth.normalizer;

import io.github.melquimartins.memora.domain.auth.dto.SignUpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class SignUpRequestNormalizer {

  public SignUpRequest normalize(SignUpRequest request) {
    return new SignUpRequest(
          normalizeName(request.name()),
          request.email(),
          request.password()
    );
  }

  private String normalizeName(String name) {
    if (name == null) {
      return null;
    }

    String[] prepositions = new String[]{"de", "da", "do", "dos", "das", "e"};

    return Arrays.stream(name
          .trim()
          .split("\\s+"))
          .map(String::toLowerCase)
          .map(word -> {
            if (Arrays.asList(prepositions).contains(word)) {
              return word;
            }
            return Character.toUpperCase(word.charAt(0)) + word.substring(1);
          })
          .collect(Collectors.joining(" "));
  }

}
