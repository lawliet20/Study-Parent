package com.spring.study.utils.others;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

	/**
	 * 获取真实ip地址
	 * @param request
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
}
