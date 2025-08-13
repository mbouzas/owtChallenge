package dev.manubouzas.owtchallenge.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to modify HTTP headers for H2 Console access.
 * Specifically, it allows framing the H2 Console by setting the Content-Security-Policy header.
 * This is necessary because the H2 Console uses frames and the default X-Frame-Options header
 * would block it in most browsers.
 *
 * @author Manuel Bouzas
 */
@Component
public class H2ConsoleFrameOptionsFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request.getRequestURI().startsWith("/h2-console")) {
            // Remove old header just in case
            response.setHeader("X-Frame-Options", "");

            // Use Content-Security-Policy to allow framing from same origin
            response.setHeader("Content-Security-Policy", "frame-ancestors 'self'");
        }

        chain.doFilter(request, response);
    }
}

