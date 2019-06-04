package com.starlz.ssoserver.dao;

/**
 * @Description
 * @Author wangzi
 * @Date 2019/6/4 10:04
 */
import com.starlz.ssoserver.entity.Permission;

import java.util.List;

public interface PermissionDao {

    List<Permission> findAll();
    List<Permission> findByAdminUserId(int userId);
}
