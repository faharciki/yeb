package com.yjxxt.server.controller;

import com.yjxxt.server.pojo.Admin;
import com.yjxxt.server.pojo.AdminLoginParam;
import com.yjxxt.server.pojo.RespBean;
import com.yjxxt.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Api(tags="LoginController")
@RestController
public class LoginController {

    @Resource
    private IAdminService adminService;

    /**
     * 用户登录
     * @param adminLoginParam
     * @return
     */
    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(), request);
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if(null==principal){
            return null;
        }
        String username =principal.getName();
        Admin admin=adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
    }


}
