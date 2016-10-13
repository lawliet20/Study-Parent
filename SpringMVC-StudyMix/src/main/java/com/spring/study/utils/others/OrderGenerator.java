package com.spring.study.utils.others;

import org.apache.commons.lang3.RandomStringUtils;

public class OrderGenerator {
    /*
	 * add by JG 付款码 随机数
	 */

    /*
     * 生成一组20位随机数字
     */
    private static final int ORDER_DEFAULT_LENGTH = 18;

    public static String order() {
        return "99" + order(ORDER_DEFAULT_LENGTH);
    }

    // 获取一个指定长度的订单号
    private static String order(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
}
