package com.in28minutes.springboot.myfirstwebapp.filter;

import com.in28minutes.springboot.myfirstwebapp.context.LanguageContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class LanguageFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String language = request.getHeader("language");

            if (language == null || language.isEmpty()) {
                language = "en";
            }

            LanguageContext.setLocale(new Locale(language));

            filterChain.doFilter(request, response);

        } finally {
            LanguageContext.clear();
        }
    }
}