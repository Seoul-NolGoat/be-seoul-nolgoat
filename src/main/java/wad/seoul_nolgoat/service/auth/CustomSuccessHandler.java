package wad.seoul_nolgoat.service.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wad.seoul_nolgoat.jwt.JwtService;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserImpl;

import java.io.IOException;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.urls.frontend-base-url}")
    private String frontendBaseUrl;

    private static final String SUCCESS_URL = "/loginSuccess";
    private static final String CHARSET = "UTF-8";

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserImpl oAuth2User = (OAuth2UserImpl) authentication.getPrincipal();
        String accessToken = jwtService.createAccessToken(oAuth2User.getName());
        String refreshToken = jwtService.createRefreshToken(oAuth2User.getName());
        jwtService.saveRefreshToken(refreshToken);

        String successBaseUrl = frontendBaseUrl + SUCCESS_URL;

        String url = String.format(
                "%s?access=%s&refresh=%s",
                successBaseUrl,
                URLEncoder.encode(accessToken, CHARSET),
                URLEncoder.encode(refreshToken, CHARSET)
        );
        response.sendRedirect(url);
    }
}
