package com.spring.study.utils.others.exceptions;


/**
 * 通用service异常
 */
public class ServiceException extends BaseException {
    private static final long serialVersionUID = 6811213429687353656L;

    public ServiceException(String code, Object[] args) {
        super("service", code, args, null);
    }

    public ServiceException(String code) {
        super("service", code, null, null);
    }

}
