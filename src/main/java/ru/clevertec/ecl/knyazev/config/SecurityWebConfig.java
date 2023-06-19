package ru.clevertec.ecl.knyazev.config;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.knyazev.controller.ExceptionController.ErrorMessage;

@Configuration
@Profile(value = "dev")
@EnableWebSecurity
@NoArgsConstructor
public class SecurityWebConfig {
	
	private static final String CLIENT_REQUEST_URL = "/users";
	
	private ObjectMapper objectMapper;
	
	@Autowired
	public SecurityWebConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		 
		SecurityFilterChain securityFilterChain = 
				httpSecurity.cors().and().csrf().disable()
				
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

		.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
			
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			try (OutputStream outputStream = response.getOutputStream()) {
				ErrorMessage errorMessage = ErrorMessage.builder()
														.message(ex.getMessage())
														.statusCode(HttpStatus.UNAUTHORIZED.value())
														.timestamp(new Date())
														.build();
				
				objectMapper.writeValue(outputStream, errorMessage);
				
				outputStream.flush();
			}

		}).and()
		
		.authorizeHttpRequests()
		
		.requestMatchers(CLIENT_REQUEST_URL).hasRole("CLIENT")
		
		.anyRequest().authenticated()
		
		.and()	
		
		.addFilter(basicAuthenticationFilter())
		
		.build();
		
		return 	securityFilterChain;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	@Bean
	UserDetailsService inMemoryUserDetailsManager() {		
		
		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
		inMemoryUserDetailsManager.createUser(User.builder()
											      .username("ConnectionClient")
											      .password("{bcrypt}$2a$12$rC8JSZDWnLggwmDRrA5sWu0xZShWO7NdsEvvRuotaUJnxK2tMYn06")
											      .authorities("ROLE_CLIENT")
											      .build());
		
		
		return inMemoryUserDetailsManager;
	}
	
	@Bean
	AuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(inMemoryUserDetailsManager());
		
		return daoAuthenticationProvider;
	}
	
	@Bean
	AuthenticationManager authenticationManager() {
		ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider());
	
		return providerManager;
	}
	
	@Bean
	BasicAuthenticationFilter basicAuthenticationFilter() {
		
		BasicAuthenticationFilter basicAuthenticationFilter = new BasicAuthenticationFilter(authenticationManager());
		
		return basicAuthenticationFilter;
	}
	
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}
	
}
