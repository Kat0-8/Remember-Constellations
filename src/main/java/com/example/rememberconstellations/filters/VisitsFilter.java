package com.example.rememberconstellations.filters;

import com.example.rememberconstellations.services.VisitsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class VisitsFilter extends OncePerRequestFilter {
    private final VisitsService visitsService;

    public VisitsFilter(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        visitsService.incrementCount(uri);
        filterChain.doFilter(request, response);
    }
}
