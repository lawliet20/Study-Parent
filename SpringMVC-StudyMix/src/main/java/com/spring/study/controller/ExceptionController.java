package com.spring.study.controller;

import com.spring.study.exception.BusinessException;
import com.spring.study.exception.ParameterException;
import com.spring.study.service.ExceptionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/exception")
public class ExceptionController {

    @Resource
    private ExceptionService testService;

    @RequestMapping(value = "/controller.do", method = RequestMethod.GET)
    public void controller(HttpServletResponse response, Integer id) throws Exception {
        switch (id) {
            case 1:
                throw new BusinessException("10", "controller10");
            case 2:
                throw new BusinessException("20", "controller20");
            case 3:
                throw new BusinessException("30", "controller30");
            case 4:
                throw new BusinessException("40", "controller40");
            case 5:
                throw new BusinessException("50", "controller50");
            default:
                throw new ParameterException("Controller Parameter Error");
        }
    }

    @RequestMapping(value = "/service.do", method = RequestMethod.GET)
    public void service(HttpServletResponse response, Integer id) throws Exception {
        testService.exception(id);
    }

    @RequestMapping(value = "/dao.do", method = RequestMethod.GET)
    public void dao(HttpServletResponse response, Integer id) throws Exception {
        testService.dao(id);
    }
}
