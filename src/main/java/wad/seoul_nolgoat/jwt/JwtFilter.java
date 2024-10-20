package wad.seoul_nolgoat.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserDto;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserImpl;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            if (jwtService.isValidAuthorization(authorization)) {
                filterChain.doFilter(request, response); // 다음 필터로 전달
                return;
            }

            String accessToken = authorization.split(" ")[1];


            OAuth2UserImpl oAuth2User = new OAuth2UserImpl(
                    new OAuth2UserDto(jwtService.getLoginId(accessToken))
            );
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            oAuth2User,
                            null,
                            null
                    )
            );
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleTokenExpiredException(response);
        } catch (Exception e) {
            handleGenericException(response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    private void handleTokenExpiredException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Token has expired\", \"status\": 401}");
    }

    private void handleGenericException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"An unexpected error occurred\", \"status\": 500}");
    }
}