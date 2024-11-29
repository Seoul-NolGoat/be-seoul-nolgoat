package wad.seoul_nolgoat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import wad.seoul_nolgoat.auth.AuthUrlManager;
import wad.seoul_nolgoat.auth.jwt.AuthFilter;
import wad.seoul_nolgoat.auth.jwt.AuthenticationEntryPointImpl;
import wad.seoul_nolgoat.auth.oauth2.CustomOAuth2UserService;
import wad.seoul_nolgoat.auth.oauth2.CustomSuccessHandler;
import wad.seoul_nolgoat.auth.oauth2.OAuth2AuthorizationRequestResolverImpl;
import wad.seoul_nolgoat.auth.oauth2.RedisOAuth2AuthorizedClientService;
import wad.seoul_nolgoat.auth.service.AuthService;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final RedisOAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final OAuth2AuthorizationRequestResolverImpl oAuth2AuthorizationRequestResolver;

    @Value("${app.urls.frontend-base-url}")
    private String frontendBaseUrl;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(oAuth2AuthorizationRequestResolver)
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .authorizedClientService(oAuth2AuthorizedClientService)
                        .successHandler(customSuccessHandler)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new AuthenticationEntryPointImpl(objectMapper))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AuthUrlManager.getUserRequestMatchers()).authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new AuthFilter(authService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendBaseUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization")); // FE에서 받을 수 있도록 설정
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}