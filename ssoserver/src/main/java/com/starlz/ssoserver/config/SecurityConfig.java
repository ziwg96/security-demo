package com.starlz.ssoserver.config;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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
            .permitAll()
            .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req,
                                                        HttpServletResponse resp,
                                                        Authentication auth) throws IOException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        HashMap<String, Object> success = new HashMap<>();
                        success.put("code", 200);
                        success.put("msg", "登陆成功");
                        out.write(JSONObject.toJSONString(success));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        HashMap<String, Object> success = new HashMap<>();
                        success.put("code", 200);
                        success.put("msg", "注销成功");
                        out.write(JSONObject.toJSONString(success));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll();

            /*//单点登录配置
            .and()
            .requestMatchers()
            .antMatchers("/", "/login", "/oauth/authorize", "/oauth/confirm_access", "/exit");*/

        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        http.csrf().disable()
        .exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler)
        .and().exceptionHandling().authenticationEntryPoint(myLoginUrlAuthenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminUserService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();    // 声明用于密码加密的encoder
    }

    @Bean
    public AuthenticationEntryPoint myLoginUrlAuthenticationEntryPoint() {
        return new MyLoginUrlAuthenticationEntryPoint("/login_p");
    }

}