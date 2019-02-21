package com.cloud.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Created by Frank on 2017/8/7. Spring Security配置 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired UserDetailsService userDetailsService;
  @Autowired AuthenticationProvider authenticationProvider;
  @Autowired LogoutSuccessHandler logoutSuccessHandler;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    httpSecurity
        .authorizeRequests()
        .antMatchers(
            "/static/**",
            "/favicon.ico",
            "/register",
            "/user/isExist/**",
            "/actuator/**",
            "/forgetPwd",
            "/getBackPwd",
            "/verifyBackPwd",
            "/restPwd",
            "/getAuthConfig",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/webjars/springfox-swagger-ui/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new UnauthorizedEntryPoint())
        .and()
        .formLogin()
        .loginPage("/login")
        .failureUrl("/login?error=true")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessHandler(logoutSuccessHandler)
        .permitAll()
        .and()
        .rememberMe()
        .tokenValiditySeconds(60 * 60 * 24 * 7)
        .and()
        .exceptionHandling()
        .accessDeniedPage("/403")
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(
            new JWTLoginFilter("/auth", authenticationManager()),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    httpSecurity.headers().cacheControl();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider);
    auth.userDetailsService(userDetailsService);
  }
}
