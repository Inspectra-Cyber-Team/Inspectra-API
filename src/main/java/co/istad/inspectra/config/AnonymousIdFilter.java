package co.istad.inspectra.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class AnonymousIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        String anonId = request.getHeader("X-Anonymous-Id");

        if (anonId == null) {
            // Generate a new anonymous ID
            anonId = UUID.randomUUID().toString();
            response.addHeader("X-Anonymous-Id", anonId); // Send it back to the client
        }

        request.setAttribute("ANONYMOUS_ID", anonId);
        filterChain.doFilter(request, response);
    }
}
