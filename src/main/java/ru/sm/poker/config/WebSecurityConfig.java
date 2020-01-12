package ru.sm.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf()
//                .disable()
//                .formLogin()
//                .permitAll()
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//        ;
//  }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("test")
                .password(passwordEncoder().encode("test"))
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("test1")
                .password(passwordEncoder().encode("test1"))
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("test2")
                .password(passwordEncoder().encode("test2"))
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("test3")
                .password(passwordEncoder().encode("test3"))
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
