package kr.co.pawpaw.ws.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	@Value("${server.allowedOrigins}")
	private List<String> allowedOrigins;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.antMatchers(HttpMethod.GET, "/ws/info").permitAll()
				.antMatchers(HttpMethod.GET, "/ws/**/**/websocket").permitAll()
				.anyRequest().authenticated())
			.build();
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedHeaders(List.of("*"));
		corsConfig.setAllowedMethods(Arrays.asList("GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH".split(",")));
		corsConfig.setAllowedOriginPatterns(allowedOrigins);

		corsConfig.setExposedHeaders(List.of("*"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setMaxAge(3600L);
		corsConfigSource.registerCorsConfiguration("/**", corsConfig);

		return corsConfigSource;
	}
}