package io.github.melquimartins.memora.security;

import io.github.melquimartins.memora.shared.exception.UnauthorizedException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            @Nonnull
            HttpServletRequest request,
            @Nonnull
            HttpServletResponse response,
            @Nonnull
            AuthenticationException e
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String message = e.getMessage();

        if (e.getCause() instanceof UnauthorizedException) {
            message = e.getCause().getMessage();
        } else if (e instanceof InsufficientAuthenticationException) {
            message = "Acesso negado. É necessário estar autenticado para acessar este recurso.";
        }

        response.getWriter().write(message);
    }

}
