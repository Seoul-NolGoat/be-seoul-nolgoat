package wad.seoul_nolgoat.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wad.seoul_nolgoat.exception.ErrorMessages;
import wad.seoul_nolgoat.exception.auth.TokenExpiredException;
import wad.seoul_nolgoat.service.auth.AuthService;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserDto;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserImpl;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AuthService.AUTHORIZATION_HEADER_TYPE);
        if (authService.isInvalidAuthorization(authorization)) {
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String accessToken = authorization.split(" ")[1];
        if (authService.isExpiredToken(accessToken)) {
            throw new TokenExpiredException(ErrorMessages.TOKEN_EXPIRED_MESSAGE);
        }

        OAuth2UserImpl oAuth2User = new OAuth2UserImpl(
                new OAuth2UserDto(authService.getLoginIdFromToken(accessToken))
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        oAuth2User,
                        null,
                        null
                )
        );
        filterChain.doFilter(request, response);
    }
}
