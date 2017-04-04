package com.wwj.controller.base;

import com.wwj.model.User;
import com.wwj.service.UserService;
import com.wwj.utils.JsonUtil;
import com.wwj.utils.ShiroSessionUtil;
import com.wwj.utils.constant.Constant;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by sherry on 2016/10/17.
 */
@Controller
public class BaseController {
    @Resource
    private UserService userService;

    public void writeResp(HttpServletRequest request,HttpServletResponse response,Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        String json = JsonUtil.toJson(data);
        printWriter.write(json);
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 在session中保存数据
     */
    public void setRequestAttr(HttpServletRequest request,String key,Object value){
        request.setAttribute(key, value);
    }

    /**
     * 获取session中的值
     */
    public Object getSessionAttr(HttpServletRequest request,String key){
        return request.getSession().getAttribute(key);
    }

    /**
     * 保存 ：用户、角色、权限信息到httpServletSession与shiroSession中用
     */
    public void saveUserInfoInSession(HttpServletRequest request){
        User current_user = (User) ShiroSessionUtil.getAttr(Constant.CURRENT_USER);
        Set<String> userRoles = userService.findRoles(current_user.getUsername());
        Set<String> userPermissions = userService.findPermissions(userRoles);

        request.getSession().setAttribute(Constant.CURRENT_USER, current_user);
        request.getSession().setAttribute(Constant.USER_ROLES, userRoles);
        request.getSession().setAttribute(Constant.USER_PERMISSION, userPermissions);

        ShiroSessionUtil.setAttr(Constant.USER_ROLES, userRoles);
        ShiroSessionUtil.setAttr(Constant.USER_PERMISSION, userPermissions);
    }

    /**
     * 删除 ：在httpServletSession与shiroSession中用户、角色、权限信息
     */
    public void clearUserInfoInSession(HttpServletRequest request){
        request.getSession().removeAttribute(Constant.CURRENT_USER);
        request.getSession().removeAttribute(Constant.USER_ROLES);
        request.getSession().removeAttribute(Constant.USER_PERMISSION);

        ShiroSessionUtil.delAttr(Constant.CURRENT_USER);
        ShiroSessionUtil.delAttr(Constant.USER_ROLES);
        ShiroSessionUtil.delAttr(Constant.USER_PERMISSION);
    }
}
