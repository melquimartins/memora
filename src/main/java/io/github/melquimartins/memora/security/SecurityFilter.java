package io.github.melquimartins.memora.security;

import io.github.melquimartins.memora.domain.user.User;
import io.github.melquimartins.memora.domain.user.UserRepository;
import io.github.melquimartins.memora.shared.exception.UnauthorizedException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull
            HttpServletRequest request,
            @Nonnull
            HttpServletResponse response,
            @Nonnull
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = recoverToken(request);

            if (token != null) {
                String email = jwtService.validateToken(recoverToken(request));

                User user = userRepository.findByEmail(email).orElseThrow(UnauthorizedException::new);

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        ));
            }
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private String recoverToken(@Nonnull HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("access-token".equals(cookie.getName()) && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }

        return null;
    }

}
