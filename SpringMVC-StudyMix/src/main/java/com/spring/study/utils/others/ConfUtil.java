package com.spring.study.utils.others;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 本类用于读取指定配置文件
 */
public class ConfUtil {
    private static final Map<String, String> PROP_FILE_NAME_MAP = new HashMap<String, String>();

    private static Map<String, Properties> proMap = new HashMap<String, Properties>();

    static {
        PROP_FILE_NAME_MAP.put("config", "/config.properties");
        PROP_FILE_NAME_MAP.put("rsa", "/rsa.property");
        PROP_FILE_NAME_MAP.put("redis", "/redis.properties");
        init();
    }

    /** 初始化属性文件。 取key=value中key和value存入propties中,过滤空行，注释行(#)。 */
    @SuppressWarnings("rawtypes")
    private static void init() {
        for (Map.Entry entry : PROP_FILE_NAME_MAP.entrySet()) {
            String confFile = entry.getValue().toString();
            try {
                Properties tmpPro = new Properties();
                tmpPro.load(ConfUtil.class.getResourceAsStream(confFile));
                proMap.put(entry.getKey().toString(), tmpPro);
            } catch (Exception ex) {
                System.out.println("加载配置文件 " + confFile + " 出错：" + ex.getMessage());
            }
        }
    }

    /**
     * 根据配置文件名获取某配置的值
     *
     * @param proName      配置文件名
     * @param key          配置的key
     * @param defaultValue 当不存在配置项时，默认返回的值
     * @return 返回配置的value值
     */
    private static String get(String proName, String key, String defaultValue) {
        String rtnCode = null;
        if (proMap.containsKey(proName)) {
            rtnCode = proMap.get(proName).getProperty(key, defaultValue);
        }

        return rtnCode;
    }

    /**
     * 读取配置文件某配置项的值，当获取不到配置项时返回null
     *
     * @param proName 配置文件名
     * @param key     配置的key
     * @return 返回配置的value
     */
    public static String getParam(String proName, String key) {
        return get(proName, key, null);
    }

    /**
     * 读取配置文件某配置项的值，当获取不到配置项时返回 defaultValue
     *
     * @param proName      配置文件名
     * @param key          配置的key
     * @param defaultValue 当获取不到配置项时默认返回的值
     * @return 返回配置的value
     */
    public static String getParam(String proName, String key, String defaultValue) {
        return get(proName, key, defaultValue);
    }

}
