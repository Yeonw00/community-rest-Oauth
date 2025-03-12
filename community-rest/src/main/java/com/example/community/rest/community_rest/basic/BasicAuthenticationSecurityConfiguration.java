//package com.example.community.rest.community_rest.basic;

//@Configuration
//public class BasicAuthenticationSecurityConfiguration {

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		return 
//			http.authorizeHttpRequests(
//					auth -> 
//						auth
//						.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//						.anyRequest().authenticated()
//					)
//			.httpBasic(Customizer.withDefaults())
//			.sessionManagement(
//					session -> session.sessionCreationPolicy
//					(SessionCreationPolicy.STATELESS))
//			.csrf().disable()		
//			.build();
//	}
//}
