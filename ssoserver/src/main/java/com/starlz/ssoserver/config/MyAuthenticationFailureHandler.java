package com.starlz.ssoserver.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest requ, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        HashMap<String, Object> error = new HashMap<>();
        error.put("code", 401);
        if (e instanceof BadCredentialsException ||
                e instanceof UsernameNotFoundException) {
            error.put("msg", "账户名或者密码输入错误");
        } else if (e instanceof LockedException) {
            error.put("msg", "账户被锁定，请联系管理员");
        } else if (e instanceof CredentialsExpiredException) {
            error.put("msg", "密码过期，请联系管理员");
        } else if (e instanceof AccountExpiredException) {
            error.put("msg", "账户过期，请联系管理员");
        } else if (e instanceof DisabledException) {
            error.put("msg", "账户被禁用，请联系管理员");
        } else {
            error.put("msg", "登录失败");
        }
        resp.setStatus(401);
        PrintWriter out = resp.getWriter();
        out.write(JSONObject.toJSONString(error));
        out.flush();
        out.close();
    }
}
