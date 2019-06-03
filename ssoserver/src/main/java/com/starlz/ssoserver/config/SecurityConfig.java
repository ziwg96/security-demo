package com.starlz.ssoserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/login", "/oauth/authorize").permitAll()
                .and()
                .formLogin()   // 指定用户登录认证通过表单
                .permitAll()

                .and()
                .requestMatchers()
                .antMatchers("/", "/login", "/oauth/authorize", "/oauth/confirm_access", "/exit")

                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();   // 指定所有路径需要认证
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()    // 指定用户信息内存存储，此外还有jdbc存储方式
                .withUser("john")   // 以下位指定用户名、加密的密码及用户角色
                .password(passwordEncoder().encode("123"))
                .roles("USER");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();    // 声明用于密码加密的encoder
    }
}