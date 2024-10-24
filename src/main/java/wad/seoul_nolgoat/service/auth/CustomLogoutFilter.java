package wad.seoul_nolgoat.service.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private static final String LOGOUT_URI = "/api/logout";
    private static final String POST_METHOD = "POST";

    private final AuthService authService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(LOGOUT_URI) || !request.getMethod().equals(POST_METHOD)) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = request.getHeader(AuthService.REFRESH_HEADER_TYPE);
        if (authService.isInvalidRefreshToken(refreshToken)
                || authService.isExpiredToken(refreshToken)
                || !authService.isExistRefreshToken(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        authService.deleteRefreshToken(refreshToken);
    }
}
