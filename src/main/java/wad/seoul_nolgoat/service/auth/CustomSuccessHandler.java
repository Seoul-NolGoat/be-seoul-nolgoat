package wad.seoul_nolgoat.service.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import wad.seoul_nolgoat.service.auth.dto.OAuth2UserImpl;

import java.io.IOException;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String BASE_URL = "http://localhost:3000/loginSuccess";
    private static final String CHARSET = "UTF-8";

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserImpl oAuth2User = (OAuth2UserImpl) authentication.getPrincipal();
        String accessToken = authService.createAccessToken(oAuth2User.getName());
        String refreshToken = authService.createRefreshToken(oAuth2User.getName());
        authService.saveRefreshToken(refreshToken);

        String url = String.format(
                "%s?access=%s&refresh=%s",
                BASE_URL,
                URLEncoder.encode(accessToken, CHARSET),
                URLEncoder.encode(refreshToken, CHARSET)
        );
        response.sendRedirect(url);
    }
}
