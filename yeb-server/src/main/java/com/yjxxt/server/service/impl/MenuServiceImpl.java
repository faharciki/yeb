package com.yjxxt.server.service.impl;

import com.yjxxt.server.pojo.Admin;
import com.yjxxt.server.pojo.Menu;
import com.yjxxt.server.mapper.MenuMapper;
import com.yjxxt.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tk
 * @since 2021-09-23
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;


    @Override
    public List<Menu> getMenusByAdminId() {
        return menuMapper.getMenusByAdminId(((Admin) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getId());
    }


    /**
     * 通过角色获取菜单列表
     *
     * @return
     */
    @Override
    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }
}
