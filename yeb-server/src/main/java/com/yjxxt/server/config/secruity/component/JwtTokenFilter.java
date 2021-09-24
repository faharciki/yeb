package com.yjxxt.server.config.secruity.component;

import com.yjxxt.server.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * * Jwt登录授权过滤器
 * *
 * * 获取头-->根据头获取token-->根据token 获取username-->判断用户是否登录-->执行
 * 登录-->设置登录信息-->过滤器放行
 * token 校验过滤
 *  1.从请求头获取token （请求头名:Authorization）
 *  2.判断token
 *     token 存在
 *  3.解析token
 *      根据token 获取用户名
 *  4.用户名存在
 *      根据用户名查询用户记录(UserDetails)
 *      校验token 合法性
 *  5.构建UsernamePasswordAuthenticationToken 对象放入security 环境
 *  6.过滤器放行
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer sfwerwerwerwerwerwerew.werwerwer.werwerewrw
        String head = request.getHeader(tokenHeader);
        if(StringUtils.isNotBlank(head)){
            if(head.startsWith(tokenHead)){
                // 如果前缀为Bearer
                String token = head.substring(tokenHead.length()+1);
                String username= jwtTokenUtil.getUserNameFormToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtTokenUtil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 放入security 环境
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        // 放行过滤器
        filterChain.doFilter(request,response);
    }

}
