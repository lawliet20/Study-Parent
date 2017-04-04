package com.spring.study.security.filter;

import com.spring.study.security.LogInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomFilterSecurityInterceptor extends
        AbstractSecurityInterceptor implements Filter {

    // 注入资源数据定义器
    @Resource
    @Qualifier("customFilterInvocationSecurityMetadataSource")
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    // 注入访问决策器
    @Resource
    @Qualifier("customAccessDecisionManager")
    @Override
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }

    // 注入认证管理器
    @Resource
    @Qualifier("authenticationManager")
    @Override
    public void setAuthenticationManager(AuthenticationManager newManager) {
        super.setAuthenticationManager(newManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 用户未登录情况下 通过在地址栏输入有效的url 访问系统 可能造成系统出现问题，所以限制匿名用户登录 自动跳转到登录页面
        if (LogInfoService.getLoginUserName() == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);

    }

    /**
     * @param fi
     * @throws ServletException
     * @throws IOException
     */
    private void invoke(FilterInvocation fi) throws IOException, ServletException {
//        InterceptorStatusToken token = super.beforeInvocation(fi);
//        try {
//            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
//        } finally {
//            super.afterInvocation(token, null);
//        }


        InterceptorStatusToken token = null;
        try {
            token = super.beforeInvocation(fi);

        } catch (Exception e) {

            // 用户登录情况下 系统中存在用户访问的资源url和权限，但是当前用户的角色中没有这个权限 所以提示跳转用户无权访问的页面
            if (e instanceof AccessDeniedException) {

//			    HttpServletRequest httpRequest = fi.getRequest();   
//			    HttpServletResponse httpResponse = fi.getResponse();   
//			    			    
//			    String path = httpRequest.getContextPath();
//		    	String basePath = httpRequest.getScheme()+"://"+httpRequest.getServerName()+":"+httpRequest.getServerPort()+path+"/";

//			    httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);   

//			    RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(basePath+"/common/403.jsp");   
//			    
//			    dispatcher.forward(httpRequest, httpResponse);   

//			    httpResponse.sendRedirect(basePath+"/common/403.jsp"); 
                throw new AccessDeniedException("用户无权访问");
            }
            return;
        }

        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }


    }


    @Override
    public void init(FilterConfig arg0) throws ServletException {


    }

    @Override
    public Class<? extends Object> getSecureObjectClass() {

        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {

        return this.securityMetadataSource;
    }

    @Override
    public void destroy() {


    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(
            FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

}
