package com.yjxxt.server.service;

import com.yjxxt.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yjxxt.server.pojo.RespBean;
import com.yjxxt.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tk
 * @since 2021-09-22
 */
public interface IAdminService extends IService<Admin> {

    Admin queryAdminByName(String name);

    Integer updateByParams(Admin admin);

    /**
     * 用户登录 登录返回token
     * @param username
     * @param password
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);


    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);


    /**
     * 根据用户id或者权限列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
