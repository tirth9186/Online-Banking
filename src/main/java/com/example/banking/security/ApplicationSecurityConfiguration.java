package com.example.banking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailsService cdService;

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(cdService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder(11));
		provider.setAuthoritiesMapper(authorityMapper());
		return provider;
	}

	@Bean
	public GrantedAuthoritiesMapper authorityMapper() {
		SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
		mapper.setConvertToUpperCase(true);
		mapper.setDefaultAuthority("USER");
		return mapper;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/","/index","/css/*","/js/*").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login").permitAll()
				.and()
				.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/logout-success").permitAll();
	}
	
}
