package com.wwj.controller;

import com.wwj.controller.base.BaseController;
import com.wwj.model.pageModel.EasyUiMenu;
import com.wwj.model.pageModel.Menu;
import com.wwj.service.ResourceService;
import com.wwj.utils.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * Created by sherry on 2016/10/18.
 */
@Controller
@RequestMapping("/index")
public class IndexController extends BaseController{
    @Resource
    private ResourceService resourceService;

    @RequestMapping("/initMenu")
    public String init(HttpServletRequest request,HttpServletResponse response){
        List<EasyUiMenu> menus = resourceService.findMenus((Set<String>)getSessionAttr(request, Constant.USER_PERMISSION));
        request.setAttribute("menus",menus);
        return "index";
    }

}
