package com.spring.study.utils.others;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Global {

    public static BigDecimal MER_FEE_RATE = new BigDecimal("0.0078");
    public static BigDecimal ALIPAY_MER_FEE_RATE = new BigDecimal("0.006");

    public static final String ALIPAY_ORDER_PAY = "alipay.acquire.createandpay";
    public static final String ALIPAY_ORDER_QUERY = "alipay.acquire.query";
    public static final String ALIPAY_PAY_CANCEL = "alipay.acquire.cancel";
    public static final String ALIPAY_PRE_ORDER = "alipay.acquire.precreate";
    public static final String ALIPAY_REFUND = "alipay.acquire.refund";

    public static Map<String, String> AlipayResultCodeMap() {
        Map<String, String> txnStaCdEnum = new HashMap<String, String>();
        txnStaCdEnum.put("ORDER_SUCCESS_PAY_SUCCESS", "00");
        txnStaCdEnum.put("ORDER_FAIL", "96");
        txnStaCdEnum.put("ORDER_SUCCESS_PAY_FAIL", "B1");
        txnStaCdEnum.put("ORDER_SUCCESS_PAY_INPROCESS", "B2");
        txnStaCdEnum.put("WAIT_BUYER_PAY", "B2");
        txnStaCdEnum.put("TRADE_CLOSED", "B4");
        txnStaCdEnum.put("TRADE_SUCCESS", "00");
        txnStaCdEnum.put("TRADE_FINISHED", "00");
        txnStaCdEnum.put("TRADE_PENDING", "B5");
        txnStaCdEnum.put("SUCCESS", "00");
        txnStaCdEnum.put("FAIL", "B0");
        txnStaCdEnum.put("UNKNOWN", "96");
        return txnStaCdEnum;
    }

    public static Map tradeCodeMap() {
        Map txnStaCdEnum = new HashMap();
        txnStaCdEnum.put("310000", "11");
        txnStaCdEnum.put("000000", "01");
        txnStaCdEnum.put("200000", "31");
        txnStaCdEnum.put("210000", "21");
        txnStaCdEnum.put("230000", "23");
        txnStaCdEnum.put("240000", "24");
        return txnStaCdEnum;
    }

    public static Map origTpMap() {
        Map txnStaCdEnum = new HashMap();
        txnStaCdEnum.put("00", "01");
        txnStaCdEnum.put("22", "22");
        txnStaCdEnum.put("25", "25");
        txnStaCdEnum.put("32", "25");
        return txnStaCdEnum;
    }

    public static Map replyCode() {
        Map replyMap = new HashMap();
        replyMap.put("00", "交易成功");
        replyMap.put("A0", "MAC校验错");
        replyMap.put("03", "无效商户");
        replyMap.put("12", "无关联交易");
        replyMap.put("14", "无效卡号");
        replyMap.put("21", "卡未初始化");
        replyMap.put("25", "找不到原始交易");
        replyMap.put("51", "可用余额不足");
        replyMap.put("55", "密码错");
        replyMap.put("61", "交易金额超限");
        replyMap.put("75", "密码错误次数超限");
        replyMap.put("94", "重复交易");
        return replyMap;
    }

    public static Map<String, String> blackResponseCode() {
        Map<String, String> brMap = new HashMap<String, String>();
        brMap.put("34", "有作弊嫌疑");
        brMap.put("41", "挂失卡");
        brMap.put("43", "被窃卡");
        brMap.put("54", "过期的卡");
        // brMap.put("59", "有作弊嫌疑");
        brMap.put("62", "受限制的卡");
        return brMap;
    }

    public static Map<String, String> msgCode() {
        Map<String, String> msgCodeMap = new HashMap<String, String>();
        msgCodeMap.put("0200", "0210");
        msgCodeMap.put("0800", "0810");
        msgCodeMap.put("0400", "0410");
        msgCodeMap.put("0500", "0510");
        msgCodeMap.put("0100", "0110");
        msgCodeMap.put("0820", "0830");
        msgCodeMap.put("0220", "0230");
        msgCodeMap.put("0320", "0330");

        msgCodeMap.put("0210", "0210");
        msgCodeMap.put("0810", "0810");
        msgCodeMap.put("0410", "0410");
        msgCodeMap.put("0510", "0510");
        msgCodeMap.put("0110", "0110");
        msgCodeMap.put("0830", "0830");
        msgCodeMap.put("0230", "0230");
        msgCodeMap.put("0330", "0320");
        return msgCodeMap;
    }

    /**
     * 获取访问者IP
     * <p/>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p/>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static void main(String[] args) {
        // String testString =
        // Global.replyCode().get("14")!=null?Global.replyCode().get("14").toString()
        // :"����ʧ��";
        // String testString = StringUtils.Int2String(2, 2);
        // System.out.println(Global.tradeCodeMap().get("310000"));
        BigDecimal test = new BigDecimal("100.00");
        System.out.println(test.subtract(test.multiply(MER_FEE_RATE)));
    }
}
