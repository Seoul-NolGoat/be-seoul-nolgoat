package wad.seoul_nolgoat.auth.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wad.seoul_nolgoat.auth.AuthUrlManager;
import wad.seoul_nolgoat.auth.dto.OAuth2UserDto;
import wad.seoul_nolgoat.auth.dto.OAuth2UserImpl;
import wad.seoul_nolgoat.exception.ApiException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더, 토큰 검증
        String accessToken;
        try {
            String authorization = request.getHeader(JwtService.AUTHORIZATION_HEADER);
            jwtService.verifyAuthorization(authorization);
            accessToken = authorization.split(" ")[1];
            jwtService.verifyAccessToken(accessToken);
        } catch (JwtException | ApiException e) {
            request.setAttribute("exception", e);
            filterChain.doFilter(request, response);
            return;
        }

        OAuth2UserImpl oAuth2User = new OAuth2UserImpl(
                new OAuth2UserDto(jwtService.getLoginId(accessToken))
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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(AuthUrlManager.getUserRequestMatchers())
                .noneMatch(matcher -> matcher.matches(request));
    }
}