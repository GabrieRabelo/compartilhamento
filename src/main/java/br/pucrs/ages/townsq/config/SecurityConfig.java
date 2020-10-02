package br.pucrs.ages.townsq.config;

import br.pucrs.ages.townsq.security.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Authentication authentication;

    @Autowired
    public SecurityConfig (Authentication authentication){
        this.authentication = authentication;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/signin/**", "/signup/**").anonymous()
                .and()
                .authorizeRequests().antMatchers("/css/**","/img/**", "/", "/question").permitAll()
                .and()
                .authorizeRequests().antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/signin")
                .usernameParameter("email")
                .defaultSuccessUrl("/", true)
                .failureUrl("/signin?error=credentials")
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/");
    }

    @Autowired
    public void setupPasswordEncoder(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authentication)
                .passwordEncoder(passwordEncoder());
    }

}