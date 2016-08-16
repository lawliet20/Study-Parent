package com.spring.study.utils.others;

import java.math.BigDecimal;
import java.util.Random;

public class MyStringUtil extends StringUtils {

	/**
	 * 将对象转成string
	 */
	public static String obj2Str(Object obj) {
		if (null != obj) {
			return obj.toString();
		}
		return null;
	}

	/**
	 * 将对象转成long
	 */
	public static Long obj2Long(Object obj) {
		return str2Long(obj2Str(obj));
	}

	/**
	 * 将字符串转成long
	 */
	public static Long str2Long(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return Long.parseLong(str);
	}

	/**
	 * 将字符串转换Integer类型
	 */
	public static Integer str2Integer(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return Integer.valueOf(str);
	}

	/**
	 * 将Integer转字符串类型
	 */
	public static String Integer2str(Integer interger) {
		if (null == interger) {
			return null;
		}
		return interger.toString();
	}

	/**
	 * 将字符串转出double类型
	 */
	public static Double str2Double(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return Double.valueOf(str);
	}

	/**
	 * 将字符串转成float类型
	 */
	public static Float toFloat(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return Float.valueOf(str);
	}

	/**
	 * 将Interge转成short类型
	 */
	public static Short interger2Short(Integer i) {
		if (null == i) {
			return null;
		}
		return Short.valueOf(i.toString());
	}

	/**
	 * 将String转成short类型
	 */
	public static Short str2Short(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return Short.valueOf(str);
	}

	/**
	 * 将string转成 BigDecimal类型
	 * 
	 * @return
	 */
	public static BigDecimal str2BigDecimal(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return BigDecimal.valueOf(str2Double(str));
	}

	/**
	 * 将BigDecimal转成 string类型
	 * 
	 * @return
	 */
	public static String bigDecimal2Str(BigDecimal bigD) {
		if (null == bigD) {
			return null;
		}
		return bigD.toString();
	}

	/**
	 * 将double转成 BigDecimal类型
	 * 
	 * @return
	 */
	public static BigDecimal double2BigDecimal(Double double1) {
		if (null == double1) {
			return null;
		}
		return BigDecimal.valueOf(double1);
	}

	/**
	 * 将BigDecimal转成 Integer类型
	 * 
	 * @return
	 */
	public static Integer bigDecimal2Integer(BigDecimal bigDecimal) {
		if (null == bigDecimal) {
			return null;
		}
		String bigD = bigDecimal2Str(bigDecimal);
		return Integer.parseInt(bigD);
	}

	/**
	 * 截取字符串前面几位
	 */
	public static String getStrFirst(String str, int num) {
		int strLen = str.length();
		if (MyStringUtil.isEmpty(str) || strLen < num) {
			return "";
		}
		if (num > 0) {
			return str.substring(0, num);
		}
		return "";
	}

	/**
	 * 截取字符串最后几位
	 */
	public static String getStrLast(String str, int num) {
		int strLen = str.length();
		if (MyStringUtil.isEmpty(str) || strLen < num) {
			return "";
		}
		if (num > 0) {
			return str.substring(strLen - num, strLen);
		}
		return "";
	}

	/**
	 * 根据传入的字符串截图指定字符之间的文字 交银IC卡[交通银行] 截取'[',']' 返回：交通银行
	 */
	public static String subStrByRegular(String source, String s1, String s2) {
		int starIndex = source.indexOf(s1) + 1;
		int endIndex = source.indexOf(s2);
		if (starIndex > endIndex || starIndex < 0 || endIndex < 0) {
			return "";
		} else {
			return source.substring(starIndex, endIndex);
		}
	}

	/**
	 * 数字string不满m位的前面补0
	 * 
	 * @return string
	 */
	public static String getNumStrByLen(String strnum,int m){
		int numStrLen = strnum.length();
		StringBuffer sb = new StringBuffer();
		if(m > numStrLen){
			for(int i=0;i<(m-numStrLen);i++){
				sb.append("0");
			}
			sb.append(strnum);
			return sb.toString();
		}
		return strnum;
	}

	/**
	 * 会员卡 16位最后一位数字的验证
	 * */
	public static String createLuhnBit(String cardNo) {
		int[] CI = new int[] { 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1 };
		int i, d, result;
		int chk_dig = 0;
		int length = cardNo.length();
		for (i = 0; i < length; i++) {
			d = Integer.parseInt(cardNo.substring(length - i - 1, length - i));
			result = d * CI[i];
			chk_dig += result / 10 + result % 10;
		}
		chk_dig = 10 - chk_dig % 10;
		chk_dig = (chk_dig == 10) ? 0 : chk_dig;
		return cardNo + chk_dig;
	}

	// String类型补0 右边和左边补0
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			// sb.append("0").append(str);// 左(前)补0
			sb.append(str).append("0");// 右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	// String类型补0 右边和左边补0
	public static String addEndForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	/**
	 * 随机字符串
	 * 
	 * @param length生成字符串的长度
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否在指定长度
	 */
	public static boolean isLenBetween(String str, int begin, int end) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		if (str.length() >= begin && str.length() <= end) {
			return true;
		}
		return false;
	}

	/**
	 * 判断传入的参数中有没有为空的
	 * 
	 * @param strs
	 * @return
	 */
	public static boolean hasEmpty(String[] strs) {
		boolean res = false;
		for (int i = 0; i < strs.length; i++) {
			if(StringUtils.isEmpty(strs[i])){
				res = true;
				break;
			}
		}
		return res;
	}
}
