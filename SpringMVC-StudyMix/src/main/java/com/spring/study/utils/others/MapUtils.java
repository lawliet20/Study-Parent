package com.spring.study.utils.others;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.maikong.model.MapConvert;
import com.maikong.model.Result;

/**
 * 微信坐标转百度坐标
 * 
 * @author L
 * 
 */
public class MapUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(getBaiDuLocationXY("118.701248", "32.119019"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Result getBaiDuLocationXY(String x, String y) throws Exception {
		String baidu_ak = ConfUtil.getParam("config", "baidu_ak");
		String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + x + "," + y + "&from=1&to=5&ak=" + baidu_ak;
		String response = ReqRemoteUrl.httpPost(url, null);
		if (StringUtils.isNotBlank(response)) {
			MapConvert map = (MapConvert) JsonUtils.jsonStr2Java(response, MapConvert.class);
			return map.getResult()[0];
		}
		return null;
	}

	/**
	 * 输出map中的键值对
	 * @param parameters
	 */
	public static void showKeyValue(Map<String, String> parameters) {
		Set set = parameters.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		System.out.println("输出map中的键值对");
		for (int i = 0; i < entries.length; i++) {
			System.out.println(entries[i].getKey().toString()+":"+ entries[i].getValue().toString());
		}
	}
	
	/**
	 * Map 转 SortedMap
	 */
	public static SortedMap map2SortedMap(Map map){
		if(null == map){
			return null;
		}
		SortedMap sortedMap = new TreeMap<String,String>(); 
		Set set = map.entrySet();
		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
		for (int i = 0; i < entries.length; i++) {
			String key = entries[i].getKey().toString();
			String value = entries[i].getValue().toString();
			sortedMap.put(key, value);
		}
		return sortedMap;
	}

}
