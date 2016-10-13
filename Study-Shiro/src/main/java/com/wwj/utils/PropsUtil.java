package com.wwj.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sherry on 16/9/21.
 */
public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProperties(String fileName) {
        Properties properties = null;
        InputStream is = null;

        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(null==is){
                throw new FileNotFoundException(fileName+" not found");
            }

            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            LOGGER.error("load properties failure",e);
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close inputStream failure",e);
                }
            }
        }
        return properties;
    }

    /**
     * 获取字符串类型数据（默认值为空）
     */
    public static String getStr(Properties props,String key){
        return getStr(props,key,"");
    }

    /**
     * 获取字符串类型数据（可指定默认值）
     */
    public static String getStr(Properties props,String key,String defaultVal){
        String value = defaultVal;
        if(props.containsKey(key)){
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数字型数据(默认值是0)
     */
    public static int getInt(Properties props,String key){
        return getInt(props,key,0);
    }

    /**
     * 获取数字型数据（可指定默认值）
     */
    public static int getInt(Properties props,String key,int defaultVal){
        int value = defaultVal;
        if(props.containsKey(key)){
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取boolean类型数据（默认值是false）
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }

    /**
     * 获取boolean类型数据（可以指定默认值）
     */
    public static boolean getBoolean(Properties props,String key,boolean defaultVal){
        boolean value = false;
        if(props.containsKey(key)){
            value = CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }


}
