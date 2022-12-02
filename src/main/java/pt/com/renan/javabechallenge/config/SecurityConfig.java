package pt.com.renan.javabechallenge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import pt.com.renan.javabechallenge.security.jwt.JwtAuthFilter;
import pt.com.renan.javabechallenge.security.jwt.JwtService;
import pt.com.renan.javabechallenge.service.impl.UserServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private JwtService jwtService;
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public OncePerRequestFilter jwtFilter() {
		return new JwtAuthFilter(jwtService, userService);
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.
			userDetailsService(userService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/api/clients/**")
					.hasAnyRole("USER", "ADMIN")
				.antMatchers("/api/orders/**")
					.hasAnyRole("USER", "ADMIN")
				.antMatchers("/api/products/**")
					.hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/api/users/**")
					.permitAll()
				.antMatchers("/h2-console/**")
					.permitAll()
				.anyRequest().authenticated()
				.and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
		
			http.headers().frameOptions().disable();
						
	}
	
}
