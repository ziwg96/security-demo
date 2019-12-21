package com.starlz.ssoclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * @author wangzi
 * @version 1.0
 * @description 用户信息
 * @date 2019/12/21 10/56
 **/
@Controller
public class UserController {

    @ResponseBody
    @RequestMapping("user")
    public Object user(Principal principal) {

        if (principal == null) {
            return "未登录";
        }
        return principal.getName();
    }

    @RequestMapping("securedPage")
    public String html(Principal principal, HttpSession session) {
        session.setAttribute("name", principal.getName());
        return "securedPage";
    }


}
