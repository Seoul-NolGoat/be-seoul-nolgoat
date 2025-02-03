package wad.seoul_nolgoat.config;

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
import wad.seoul_nolgoat.auth.oauth2.security.CustomOAuth2UserService;
import wad.seoul_nolgoat.auth.oauth2.security.CustomSuccessHandler;
import wad.seoul_nolgoat.auth.oauth2.security.OAuth2AuthorizationRequestResolverImpl;
import wad.seoul_nolgoat.auth.oauth2.security.RedisOAuth2AuthorizedClientService;
import wad.seoul_nolgoat.auth.security.AuthFilter;
import wad.seoul_nolgoat.auth.security.AuthUrlManager;
import wad.seoul_nolgoat.auth.security.AuthenticationEntryPointImpl;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final OAuth2AuthorizationRequestResolverImpl oAuth2AuthorizationRequestResolver;
    private final CustomOAuth2UserService oAuth2UserService;
    private final RedisOAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final CustomSuccessHandler successHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AuthUrlManager authUrlManager;
    private final AuthFilter authFilter;

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
                                .userService(oAuth2UserService)
                        )
                        .authorizedClientService(oAuth2AuthorizedClientService)
                        .successHandler(successHandler)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authUrlManager.getUserRequestMatchers()).authenticated()
                        .requestMatchers(authUrlManager.getPublicRequestMatchers()).permitAll()
                        .requestMatchers(authUrlManager.getReissueTokenRequestMatcher()).permitAll()
                        .anyRequest().denyAll()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
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