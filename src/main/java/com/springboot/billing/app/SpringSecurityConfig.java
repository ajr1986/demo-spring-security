package com.springboot.billing.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.billing.app.auth.handler.LoginSuccessHandler;
import com.springboot.billing.app.model.service.UserDetailsServiceImpl;

@EnableGlobalMethodSecurity(securedEnabled = true) // This is to enable the spring security annotations
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler loginSuccesHandler;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/img/**", "/list", "/h2-console/**").permitAll();
		http.authorizeRequests()
			//.antMatchers("/details/**").hasAnyRole("USER") it was replace using annotation method in ClienController method using @Secured 
			.antMatchers("/uploads/**").hasAnyRole("USER")
			.antMatchers("/form/**").hasAnyRole("ADMIN")
			.antMatchers("/delete/**").hasAnyRole("ADMIN")
			.antMatchers("/bill/**").hasAnyRole("ADMIN").anyRequest().authenticated()
			.and()
			.formLogin()
				.successHandler(loginSuccesHandler) // Is not neccessary, just add to show message for login success 
				.loginPage("/login")
				.permitAll()
			.and()
				.logout().permitAll()
			.and()
				.exceptionHandling().accessDeniedPage("/error_403");
		
		
		// IMPORTANT!!! The following lines are only to allow enter to h2-console. Remove it when use a real data base. 
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Autowired
	public void globalUserConfiguration(AuthenticationManagerBuilder build) throws Exception {

		build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

	}
}
