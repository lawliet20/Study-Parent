package com.spring.study.security.service;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类是资源的访问权限的定义，实现资源和访问权限的对应关系
 * 该类的主要作用是在Spring Security的整个过滤链启动后，
 * 在容器启动的时候，程序就会进入到该类中的init()方法，init调用了loadResourceDefine()方法，
 * 该方法的主要目的是将数据库中的所有资源与权限读取到本地缓存中保存起来！
 * 类中的resourceMap就是保存的所有资源和权限的集合，URL为Key，权限作为Value！
 *
 * @author zhangwenchao
 */
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {


    //使用的是AntUrlPathMatcher这个path matcher来检查URL是否与资源定义匹配
    //private RequestMatcher urlMatcher = null;

    //resourceMap就是保存的所有资源和权限的集合，URL为Key，权限作为Value！
    private static HashMap<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     * 自定义方法，这个类放入到Spring容器后，
     * 指定init为初始化方法，从数据库中读取资源
     */
    @PostConstruct
    public void init() {
        loadResourceDefine();
    }

    /**
     * 程序启动的时候就加载所有资源信息
     * 初始化资源与权限的映射关系
     */
    private void loadResourceDefine() {
        ConfigAttribute ca1 = new SecurityConfig("GUEST"); //将ROLE_XXX 封装成spring的权限配置属性
        ConfigAttribute ca2 = new SecurityConfig("ROLE_USER"); //将ROLE_XXX 封装成spring的权限配置属性
        ConfigAttribute ca3 = new SecurityConfig("ROLE_ADMIN"); //将ROLE_XXX 封装成spring的权限配置属性

        //应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
        Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
        atts.add(ca1);
        atts.add(ca2);
        atts.add(ca3);
        System.out.println("初始化资源与权限对应关系--->");
        resourceMap.put("/user-account/login", atts);

    }

    /**
     * 根据URL获取该URL权限的配置
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        //object是一个URL ,为用户请求URL
        String url = ((FilterInvocation) object).getRequestUrl();
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        Iterator<String> iter = resourceMap.keySet().iterator();

        String matchUrl = null;//匹配url
        //取到请求的URL后与上面取出来的资源做比较
        while (iter.hasNext()) {
            String resURL = iter.next();

            //  if(urlMatcher.pathMatchesUrl(resURL,url)){
            if (url.startsWith(resURL)) {
//	            	return resourceMap.get(resURL);  //返回权限的集合
                //初次匹配或当前匹配的url更长则更新匹配url
                if (matchUrl == null || matchUrl.length() < resURL.length())
                    matchUrl = resURL;
            }

        }
        if (matchUrl != null) {
            //如果存在匹配的url则返回需具备的权限
//        	System.out.println(matchUrl+"-------"+resourceMap.get(matchUrl));
            return resourceMap.get(matchUrl);
        }
        // 当系统中没配资源权限url，用户在访问这个资源的情况下，返回null 表示放行 ，
        // 如果当系统分配了资源url,但是这个用户立属的角色没有则 提示用户无权访问这个页面
        return null;


    }

	/*
     * @return
     * @link org.springframework.security.access.SecurityMetadataSource#getAllConfigAttributes()
     */

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return true;
    }


}
