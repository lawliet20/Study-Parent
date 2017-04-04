package com.wwj.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码工具类
 * Created by sherry on 16/9/24.
 *
 * @since 1.0.0
 */
public final class CodecUtil {

    private static final Logger logger = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将URL编码
     */
    public static String encodeUrl(String source) {
        String target;
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("encode url failure ", e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将URL解码
     */
    public static String decodeUrl(String source){
        String target;
        try {
            target = URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            logger.error("decode url failure ",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
