package com.yjxxt.server.config.secruity.component;

import com.yjxxt.server.pojo.Menu;
import com.yjxxt.server.pojo.Role;
import com.yjxxt.server.service.IMenuService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 根据请求url分析出请求所需角色
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {
    @Resource
    private IMenuService menuService;

    AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求的url
        String url=((FilterInvocation) object).getRequestUrl();
        //获取菜单
        List<Menu> menus=menuService.getAllMenusWithRole();
        for(Menu menu:menus){
            //判断请求url与菜单角色是否匹配
            if(antPathMatcher.match(menu.getUrl(),url)){
                String[] str=menu.getRoles().stream()
                        .map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        //没有匹配的url默认为登录 即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
