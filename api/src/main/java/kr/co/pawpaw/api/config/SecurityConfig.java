package kr.co.pawpaw.api.config;

import kr.co.pawpaw.api.config.auth.filter.JwtFilter;
import kr.co.pawpaw.api.config.auth.handler.JwtAccessDeniedHandler;
import kr.co.pawpaw.api.config.auth.handler.JwtAuthenticationEntryPointHandler;
import kr.co.pawpaw.api.config.auth.handler.OAuth2FailureHandler;
import kr.co.pawpaw.api.config.auth.handler.OAuth2SuccessHandler;
import kr.co.pawpaw.api.config.auth.repository.CookieAuthorizationRequestRepository;
import kr.co.pawpaw.api.config.auth.service.CustomOAuth2UserService;
import kr.co.pawpaw.api.config.property.ServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final ServerProperties serverProperties;
    private final JwtFilter jwtFilter;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
            .headers(headers -> headers.addHeaderWriter((request, response) -> {
                response.setHeader("Cross-Origin-Embedder-Policy", "credentialless");
                response.setHeader("Cross-Origin-Resource-Policy", "same-site");
            }))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .antMatchers(HttpMethod.GET, "/api/auth/sign-up/social/info").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/sign-up/social").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/sign-up/verification").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/sign-up/verification/check").permitAll()
                .antMatchers(HttpMethod.GET, "/api/auth/sign-up/check/duplicate/email").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/password/reset/mail").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/auth/password").permitAll()
                .antMatchers(HttpMethod.GET, "/api/auth/email").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler))
            .build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowedMethods(Arrays.asList("GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH".split(",")));
        corsConfig.setAllowedOriginPatterns(serverProperties.getAllowedOrigins());

        corsConfig.setExposedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);

        return corsConfigSource;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().mvcMatchers(
            "/h2/**",
            "/v3/api-docs/**",
            "/swagger-ui.html/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html"
        );
    }
}
