package com.dp9v.lingua_learn_server.api.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter that authenticates requests with a static API key in X-Bot-Api-Key header.
 * Grants ROLE_BOT authority to matched requests.
 */
@Component
public class BotApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-Bot-Api-Key";

    private final String botApiKey;

    public BotApiKeyFilter(@Value("${bot.api-key:}") String botApiKey) {
        this.botApiKey = botApiKey;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (StringUtils.isNoneBlank(botApiKey, requestApiKey) &&
                StringUtils.equals(requestApiKey, botApiKey)) {
            var auth = new BotApiKeyAuthentication(requestApiKey);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private static class BotApiKeyAuthentication extends AbstractAuthenticationToken {
        private final String apiKey;

        BotApiKeyAuthentication(String apiKey) {
            super(List.of(new SimpleGrantedAuthority("ROLE_BOT")));
            this.apiKey = apiKey;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return apiKey;
        }

        @Override
        public Object getPrincipal() {
            return "bot-service";
        }
    }
}

