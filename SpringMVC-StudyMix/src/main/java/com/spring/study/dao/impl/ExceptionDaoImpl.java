package com.spring.study.dao.impl;

import org.aspectj.weaver.AjAttribute;
import org.springframework.stereotype.Repository;

import com.spring.study.dao.ExceptionDao;
import com.spring.study.exception.BusinessException;
import com.spring.study.exception.ParameterException;

@Repository("exceptionDao")
public class ExceptionDaoImpl implements ExceptionDao {

    public void exception(Integer id) throws Exception {
        switch (id) {
            case 1:
                throw new BusinessException("12", "dao12");
            case 2:
                throw new BusinessException("22", "dao22");
            case 3:
                throw new BusinessException("32", "dao32");
            case 4:
                throw new BusinessException("42", "dao42");
            case 5:
                throw new BusinessException("52", "dao52");
            default:
                throw new ParameterException("Dao Parameter Error");
        }
    }
}
