package com.yjxxt.server.controller;


import com.yjxxt.server.pojo.Menu;
import com.yjxxt.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统配置Controller
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system")
public class SystemConfigController {

    @Resource
    private IMenuService menuService;

    @ApiOperation(value = "通过用户id获取菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByHrId(){
        return menuService.getMenusByAdminId();
    }
}
