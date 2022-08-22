package com.ai.lms.config;

import com.ai.lms.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.formLogin(form -> form.loginPage("/login"));
		http.logout(logout -> logout.logoutUrl("/logout").invalidateHttpSession(true).logoutSuccessUrl("/login"));
		http.exceptionHandling().accessDeniedPage("/denied-page");
		http.authorizeHttpRequests(auth -> auth
				.mvcMatchers("/login").permitAll()
				.mvcMatchers("/admin/**").hasAuthority(User.Role.Admin.name())
				.mvcMatchers("/teacher/**").hasAuthority(User.Role.Teacher.name())
                .mvcMatchers("/student/**").hasAuthority(User.Role.Student.name()));

    }
}
