package com.example.community.rest.community_rest.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;

    public SecurityConfig(OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return 
			http.authorizeHttpRequests(
					auth -> 
						auth
						.requestMatchers("/**").permitAll()
						.anyRequest().authenticated()
					)
			.httpBasic(Customizer.withDefaults())
			.sessionManagement(
					session -> session.sessionCreationPolicy
					(SessionCreationPolicy.STATELESS))
			.csrf().disable()	
			.formLogin().disable()
			.httpBasic().disable() // Basic Auth 비활성화
			.oauth2Login(oauth2 -> oauth2
					.loginPage("/oauth2/authorization/google")
	                .userInfoEndpoint(userInfo -> userInfo
	                    .userService(customOAuth2UserService) // OAuth2UserService 적용
	                )
	            )
			.build();
	}
}
