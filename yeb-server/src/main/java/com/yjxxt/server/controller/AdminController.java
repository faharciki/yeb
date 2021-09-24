package com.yjxxt.server.controller;


import com.yjxxt.server.pojo.Admin;
import com.yjxxt.server.service.IAdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tk
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    @GetMapping("/{id}")
    public Admin findOneById(@PathVariable  Integer id){
        return adminService.getById(id);
    }

    @PutMapping("save")
    public void saveAdmin(Admin admin){
        adminService.save(admin);
    }

    @PostMapping("update")
    public void updateAdmin(Admin admin){
        adminService.updateById(admin);
    }

    @DeleteMapping("delete")
    public void deleteAdmin(Integer id){
        adminService.removeById(id);
    }

    @GetMapping("/name/{name}")
    public Admin queryAdminByName(@PathVariable String name){
        return adminService.queryAdminByName(name);
    }

    @PostMapping("updateByParams")
    public void updateByParams(Admin admin){
        adminService.updateByParams(admin);
    }

}
