package wad.seoul_nolgoat.auth.oauth2.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wad.seoul_nolgoat.auth.oauth2.dto.OAuth2UserImpl;
import wad.seoul_nolgoat.auth.service.AuthService;

import java.io.IOException;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.urls.frontend-base-url}")
    private String frontendBaseUrl;

    private static final String SUCCESS_URI = "/loginSuccess";
    private static final String CHARSET = "UTF-8";

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2UserImpl oAuth2User = (OAuth2UserImpl) authentication.getPrincipal();
        String refreshToken = authService.createRefreshToken(oAuth2User.getName());
        String accessToken = authService.createAccessToken(oAuth2User.getName());

        String successBaseUrl = frontendBaseUrl + SUCCESS_URI;
        String url = String.format(
                "%s?access=%s",
                successBaseUrl,
                URLEncoder.encode(accessToken, CHARSET)
        );
        response.addCookie(authService.createRefreshTokenCookie(refreshToken));
        response.sendRedirect(url);
    }
}
