package com.example.community.rest.community_rest.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//        	.cors(Customizer.withDefaults())
//            .csrf().disable()  // CSRF 비활성화
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/public/**").permitAll()  // 특정 경로는 인증 없이 접근 가능
//                .anyRequest().authenticated()  // 나머지는 인증 필요
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .formLogin().disable()  // 폼 로그인 비활성화
//            .httpBasic().disable();  // Basic Auth 비활성화
//        return http.build();
//    }
	
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
			.build();
	}
}
