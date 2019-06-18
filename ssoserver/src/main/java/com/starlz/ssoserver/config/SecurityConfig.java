package com.starlz.ssoserver.config;

import com.starlz.ssoserver.service.AdminUserService;
import com.starlz.ssoserver.service.MyFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
    @Autowired
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers( "/login", "/oauth/authorize").permitAll()
            .antMatchers(HttpMethod.POST, "/ss").permitAll()
            .and()
            .formLogin()   // 指定用户登录认证通过表单
                .loginPage("/login_p")
            .loginProcessingUrl("/login")
            .failureHandler(myAuthenticationFailureHandler)
            .permitAll();

            /*//单点登录配置
            .and()
            .requestMatchers()
            .antMatchers("/", "/login", "/oauth/authorize", "/oauth/confirm_access", "/exit");*/

        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        http.csrf().disable()
        .exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminUserService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();    // 声明用于密码加密的encoder
    }

}