package com.yjxxt.server.config.secruity;

import com.yjxxt.server.config.secruity.component.*;
import com.yjxxt.server.pojo.Admin;
import com.yjxxt.server.service.IAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private IAdminService adminService;
    @Resource
    private YebAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Resource
    private YebAccessDeniedHandler restAccessDeniedHandler;

    @Resource
    private CustomFilter customFilter;

    @Resource
    private CustomUrlDecisionManager customUrlDecisionManager;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用JWT，不需要csrf
        http.csrf().disable()
                //禁用session创建==>基于token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //允许登录访问
                .antMatchers("/login","/logout").permitAll()
                //除上面外，掺请求都要求认证
                .anyRequest().authenticated()
                /**
                 * 动态权限配置
                 */
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilter);
                        return object;
                    }
                })
                //禁用缓存
                .and().headers().cacheControl();

        //添加jwt登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    /**
     * 用来放行静态资源
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        //放行静态资源
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "/img/**",
                "/fonts/**",
                "/favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha"
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        //获取登录用户信息
        return username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if (null != admin) {
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw  new UsernameNotFoundException("用户名或密码不正确！");
        };

    }

    @Bean
    public JwtTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtTokenFilter();
    }

}
