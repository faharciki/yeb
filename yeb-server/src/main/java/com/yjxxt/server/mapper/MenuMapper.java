package com.yjxxt.server.mapper;

import com.yjxxt.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjxxt.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tk
 * @since 2021-09-23
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过用户id获取菜单列表
     * @param id
     * @return
     */
    List<Menu> getMenusByAdminId(Integer id);

    /**
     * 通过角色获取菜单列表
     * @return
     */
    List<Menu> getAllMenusWithRole();

}
