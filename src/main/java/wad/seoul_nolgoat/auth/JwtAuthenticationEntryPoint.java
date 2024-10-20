package wad.seoul_nolgoat.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import wad.seoul_nolgoat.web.exception.dto.response.ErrorResponse;

import java.io.IOException;

import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_TOKEN_FORMAT;
import static wad.seoul_nolgoat.exception.ErrorCode.TOKEN_EXPIRED;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");
        if (exception instanceof ExpiredJwtException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(TOKEN_EXPIRED)));
            return;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(INVALID_TOKEN_FORMAT)));
    }
}
