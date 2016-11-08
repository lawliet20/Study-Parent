package com.wwj.controller;

import com.wwj.controller.base.BaseController;
import com.wwj.model.UrlFilter;
import com.wwj.service.ResourceService;
import com.wwj.shiro.service.UrlFilterService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by sherry on 16/10/14.
 */
@RequestMapping("/resources")
@Controller
public class ResourcesController extends BaseController{
    @Resource
    private ResourceService resourceService;


    @RequestMapping("/getAllRes")
    public String update(HttpServletRequest request,HttpServletResponse response) throws IOException {
        List<com.wwj.model.Resource> resourceList = resourceService.findAll();
        setRequestAttr(request,"menus",resourceList);
        return "/WEB-INF/jsp/menu";
    }

    @RequestMapping("/add")
    public String add(){
        return null;
    }
}
