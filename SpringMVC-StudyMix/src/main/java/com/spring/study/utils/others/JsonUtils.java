package com.spring.study.utils.others;

import com.alibaba.fastjson.JSONObject;


public class JsonUtils {

	//测试
	public static void main(String[] args) {
		String str = "{\"brand_no\":\"jycy,sy\",\"unit_rank\":\"2\",\"package\":\"2\"}";
		JSONObject abc = str2Json(str);
		System.out.println(abc.get("ae3"));
		System.out.println(abc.get("brand_no"));
	}

	/**
	 * 将取出的value值转成string
	 */
	public static String getStrVal(JSONObject json ,String key){
		Object obj = json.get(key);
		if(null != obj){
			return obj.toString();
		}
		return "";
	}

	/**
	 * 把string转换成json对象，并转化为字符串
	 * @param str
	 * @return
	 */
	public static JSONObject str2Json(String str) {
		if(StringUtils.isEmpty(str)){
			return null;
		}
		JSONObject json = JSONObject.parseObject(str);
		return json;
	}
	
	/**
	 * string转对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object jsonStr2Java(String jsonStr,Class clazz){
		if(StringUtils.isEmpty(jsonStr) || null == clazz){
			return null;
		}
		JSONObject jsonObj = str2Json(jsonStr);
		if(null != jsonObj){
			return JSONObject.toJavaObject(jsonObj, clazz);
		}
		return null;
	}
	
	/**
	 * 把java 对象列表转换为json格式字符串
	 */
	public static String java2JsonStr(Object obj) {
		Object target = null;
		if(null != obj){
			target = JSONObject.toJSON(obj);
			return MyStringUtil.obj2Str(target);
		}
		return "";
	}
	
	/**
	 * 把JSON字符串转换为JAVA 对象数组
	 * @param params
	 * @param clazz
	 * @return
	 */
	/*public static List jsonArr2Java(String params, Class clazz) {
		JSONArray json = JSONArray.fromObject(params);
		List list = (List) JSONArray.toCollection(json, clazz);
		return list;
	}*/

	  
	
}
