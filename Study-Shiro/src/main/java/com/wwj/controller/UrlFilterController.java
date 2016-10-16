package com.wwj.controller;

import com.wwj.model.UrlFilter;
import com.wwj.shiro.service.UrlFilterService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sherry on 16/10/14.
 */
@RequestMapping("/urlFilter")
@Controller
public class UrlFilterController {
    @Resource
    private UrlFilterService urlFilterService;

    @RequestMapping("/update")
    public String update(UrlFilter urlFilter,HttpServletRequest request,HttpServletResponse response){
        urlFilterService.updateUrlFilter(urlFilter);
        SecurityUtils.getSubject().getSession();
        SecurityUtils.getSubject().getSession().setAttribute("perList", urlFilterService.findAll());
        return "success";
    }
}
