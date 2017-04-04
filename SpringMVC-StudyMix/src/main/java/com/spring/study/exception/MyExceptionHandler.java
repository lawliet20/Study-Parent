package com.spring.study.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class MyExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ex", ex);

        // 根据不同错误转向不同页面
        if (ex instanceof BusinessException) {
            System.out.println("BusinessException:");
            //return new ModelAndView("error-business", model);
        } else if (ex instanceof ParameterException) {
            System.out.println("ParameterException:");
            //return new ModelAndView("error-parameter", model);
        } else {
            System.out.println("error:");
            //return new ModelAndView("error", model);
        }
        System.out.println(ex.getMessage());
        return new ModelAndView("err-page/error", model);
    }

}
