package com.newstock.post.config;

import com.newstock.post.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableSpringConfigured
@Slf4j
public class SpringConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SpringConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);


        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/signup", "/", "/error",
                                "/search/**", "/news/**", "/post/stock", "/post/freeBoard", "/post/*", "/refresh").permitAll()
                        .anyRequest().hasRole("USER")
                )
                .logout((logout) -> logout.logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                );

        httpSecurity.formLogin((auth) -> auth.loginPage("/login")
                        .defaultSuccessUrl("/loginSuccess")
                        .permitAll());

        httpSecurity.logout((logout) -> logout
                .logoutUrl("/logout")
                .permitAll());


//        httpSecurity
//                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
//
//        httpSecurity
//                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        httpSecurity
//                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
