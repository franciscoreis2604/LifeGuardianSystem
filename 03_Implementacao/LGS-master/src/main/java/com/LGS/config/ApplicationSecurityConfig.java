package com.LGS.config;

import com.LGS.security.jwt.JwtConfig;
import com.LGS.security.jwt.JwtTokenVerifier;
import com.LGS.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.LGS.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     UserServiceImpl userService,
                                     SecretKey secretKey,
                                     JwtConfig jwtConfig,
                                     UserServiceImpl userServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtConfig = jwtConfig;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //JWT is stateless
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, userServiceImpl))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/lgs/v1/user").permitAll()
                .antMatchers("/lgs/**")
                .authenticated();
    }

    /*
    Methods needed for users in DB
    */
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(daoAuthenticationProvider());

    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
