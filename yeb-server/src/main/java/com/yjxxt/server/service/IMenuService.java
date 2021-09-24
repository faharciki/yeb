package com.yjxxt.server.service;

import com.yjxxt.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tk
 * @since 2021-09-23
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户id获取菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 通过角色获取菜单列表
     * @return
     */
    List<Menu> getAllMenusWithRole();


}
