package com.yjxxt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yjxxt.server.mapper.RoleMapper;
import com.yjxxt.server.pojo.Admin;
import com.yjxxt.server.mapper.AdminMapper;
import com.yjxxt.server.pojo.RespBean;
import com.yjxxt.server.pojo.Role;
import com.yjxxt.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjxxt.server.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tk
 * @since 2021-09-22
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    /**
     * 根据用户id获取权限列表
     */
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AdminMapper adminMapper;
    /**
     * 引入UserDetailsService
     */
    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 对密码进行加密
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 用来签发token
     */
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * tokenHead
     */
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public Admin queryAdminByName(String name) {
        return this.getOne(new QueryWrapper<Admin>().eq("name",name));
    }

    @Override
    public Integer updateByParams(Admin admin) {
        return this.update(new UpdateWrapper<Admin>().eq("id",admin.getId()).eq("username",admin.getUsername()).set("name",admin.getName()))?1:0;
    }

    /**
     * 用户登录 登录返回token
     * @param username
     * @param password
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        /**
         * 1.参数校验
         * 2.根据用户名查找
         * 3.密码比对
         * 4.生成token返回
         */

        /**
         * 添加验证码
         */
        String captcha=(String) request.getSession().getAttribute("captcha");
        if(StringUtils.isBlank(code) || !captcha.equals(code)){
            return RespBean.error("验证码填写错误！");
        }


        if(StringUtils.isBlank(username)){
            return RespBean.error("用户名不能为空！");
        }
        if(StringUtils.isBlank(password)){
            return RespBean.error("用户密码不能为空！");
        }

        //重写UserDetailsService -->loadUserByUsername
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);

        //对密码进行比对
        if(!(passwordEncoder.matches(password,userDetails.getPassword()))){
            return RespBean.error("密码错误！");
        }

        /**
         * 验证码使用
         */
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        //签发token
        String token=jwtTokenUtil.generateToken(userDetails);
        Map<String,Object> map=new HashMap<>();
        map.put("token",token);
        map.put("tokenHead",tokenHead);
        return RespBean.success("用户登录成功",map);

    }

    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username));
    }

    /**
     * 根据用户id获取权限列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }


}
