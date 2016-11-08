package com.javadesgin.study.单例模式.主键自增;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式：主键自增
 * Created by sherry on 2016/11/7.
 * desc：KeyGenerator是单例的，KeyInfo是多例模式（每个状态的对象只有一个实例）
 */
public class KeyGenerator {
    private static KeyGenerator keyGenerator = new KeyGenerator();
    private Map<String, KeyInfo> map = new HashMap<String, KeyInfo>();
    KeyInfo keyInfo;

    private KeyGenerator() {

    }

    public static KeyGenerator getInstance() {
        return keyGenerator;
    }

    public synchronized int getNextKey(String keyName) {
        if (map.containsKey(keyName)) {
            keyInfo = map.get(keyName);
        } else {
            keyInfo = new KeyInfo(keyName);
            map.put(keyName,keyInfo);
        }
        return keyInfo.getNextKey();
    }

    public static void main(String[] args) {
        KeyGenerator k = KeyGenerator.getInstance();
        for (int i = 0; i < 20; i++) {
            int nextKey = k.getNextKey("");
            System.out.println("nextKey:" + nextKey);
        }
    }

}
