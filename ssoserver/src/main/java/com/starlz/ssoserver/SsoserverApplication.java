package com.starlz.ssoserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@MapperScan("com.starlz.ssoserver.dao")
public class SsoserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoserverApplication.class, args);
    }

}
