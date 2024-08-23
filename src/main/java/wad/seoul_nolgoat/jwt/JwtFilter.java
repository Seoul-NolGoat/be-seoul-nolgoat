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
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserDto;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserImpl;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_INVALID_MESSAGE = "token is null or invalid";
    private static final String TOKEN_EXPIRED_MESSAGE = "token has expired";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            log.info(TOKEN_INVALID_MESSAGE);
            filterChain.doFilter(request, response); // 다음 필터로 전달
            return;
        }

        String token = authorization.split(" ")[1];
        if (jwtUtil.isExpired(token)) {
            log.info(TOKEN_EXPIRED_MESSAGE);
            filterChain.doFilter(request, response);
            return;
        }

        OAuth2UserImpl oAuth2User = new OAuth2UserImpl(
                new OAuth2UserDto(jwtUtil.getLoginId(token))
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
