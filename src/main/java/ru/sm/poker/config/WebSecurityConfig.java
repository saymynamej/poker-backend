package ru.sm.poker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .csrf()
                .disable()
                .formLogin()
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
        ;
  }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("4")
                .password(passwordEncoder().encode("4"))
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("5")
                .password(passwordEncoder().encode("5"))
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("6")
                .password(passwordEncoder().encode("6"))
                .roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("7")
                .password(passwordEncoder().encode("7"))
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
