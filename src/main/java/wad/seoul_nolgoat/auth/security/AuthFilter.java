package wad.seoul_nolgoat.auth.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserDto;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserImpl;
import wad.seoul_nolgoat.auth.service.AuthService;
import wad.seoul_nolgoat.exception.ApplicationException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static wad.seoul_nolgoat.auth.service.AuthService.AUTHORIZATION_HEADER;

@RequiredArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final AuthUrlManager authUrlManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더, 토큰 검증
        String accessToken;
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
            authService.verifyAuthorizationHeader(authorizationHeader);
            accessToken = authorizationHeader.split(" ")[1];
            authService.verifyAccessToken(accessToken);
        } catch (JwtException | ApplicationException e) {
            request.setAttribute("exception", e);
            filterChain.doFilter(request, response);
            return;
        }

        OAuth2UserImpl oAuth2User = new OAuth2UserImpl(
                new OAuth2UserDto(authService.getLoginId(accessToken))
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        oAuth2User,
                        null,
                        Collections.emptyList()
                )
        );
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(authUrlManager.getUserRequestMatchers())
                .noneMatch(matcher -> matcher.matches(request));
    }
}