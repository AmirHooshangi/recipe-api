package com.abn.recipe.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	log.debug("setting up Security configs...");
    	httpSecurity.csrf().disable()
    				.cors().disable()
    				.authorizeRequests()
    				//.antMatchers("/").permitAll()
    				.antMatchers("/error").permitAll()
                    .antMatchers("/api/**").permitAll()
    				//.antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
    				.anyRequest().authenticated()
    				.and()
    				//.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
    				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
}
