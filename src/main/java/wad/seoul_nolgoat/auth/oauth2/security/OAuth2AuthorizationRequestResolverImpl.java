package wad.seoul_nolgoat.auth.oauth2.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthorizationRequestResolverImpl implements OAuth2AuthorizationRequestResolver {

    private static final String AUTHORIZATION_REQUESTS_BASE_URI = "/oauth2/authorization";

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public OAuth2AuthorizationRequestResolverImpl(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                AUTHORIZATION_REQUESTS_BASE_URI
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        return customizeAuthorizationRequest(defaultResolver.resolve(request));
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        return customizeAuthorizationRequest(defaultResolver.resolve(request, clientRegistrationId));
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        if (authorizationRequest == null) {
            return null;
        }

        Object registrationId = authorizationRequest.getAttribute("registration_id");

        // 구글일 때만 Refresh 토큰을 받기 위해, 파라미터 추가
        if (registrationId.equals("google")) {
            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(params -> {
                        params.put("prompt", "consent");
                        params.put("access_type", "offline");
                    })
                    .build();
        }

        // 다른 제공자는 그대로 반환
        return authorizationRequest;
    }
}
