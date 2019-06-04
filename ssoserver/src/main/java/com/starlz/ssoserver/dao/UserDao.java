package com.starlz.ssoserver.dao;

import com.starlz.ssoserver.entity.SysUser;

/**
 * @Description
 * @Author wangzi
 * @Date 2019/6/4 10:28
 */
public interface UserDao {
    SysUser findByUserName(String username);
}
