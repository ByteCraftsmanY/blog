package com.yogesh.blog.configurations;

import com.yogesh.blog.services.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserPrincipalService userPrincipalService;

    @Autowired
    public AppSecurityConfig(UserPrincipalService userPrincipalService) {
        this.userPrincipalService = userPrincipalService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userPrincipalService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/new-post", "/edit-post", "/delete-post")
            .hasAnyRole("ADMIN", "AUTHOR")
            .antMatchers("/delete-comment", "/update-comment")
            .hasAnyRole("ADMIN", "AUTHOR", "USER")
            .antMatchers("/css/**", "/user/**", "/", "/show-post", "/save-comment","/new-comment")
            .permitAll().anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/user/sign-in")
            .loginProcessingUrl("/auth-user")
            .permitAll()
            .and()
            .logout().permitAll();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}