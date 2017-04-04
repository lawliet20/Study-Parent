package com.javadesgin.study.单例模式.主键自增;

/**
 * Created by sherry on 2016/11/7.
 */
public class KeyInfo {
    private int poolSize;
    private String keyName;
    private int keyMin;
    private int keyMax;
    private int nextKey;

    public KeyInfo(String keyName){
        this(keyName,20);
    }

    public KeyInfo(String keyName,int poolSize) {
        this.keyName = keyName;
        this.poolSize = poolSize;
        getNextGens();
    }

    public int getNextKey() {
        if (nextKey > keyMax) {
            getNextGens();
        }
        return nextKey++;
    }

    public void getNextGens() {
        String sql = "update table set " + keyName + "=(" + keyName + "+"+poolSize+")";
        //execute sql
        String sql2 = "select max(" + keyName + ") from table ";
        //execute sql2
        //max等于刚刚从数据库查出的最大值，这里是示例代码
        keyMax = 1000;
        keyMin = keyMax - poolSize + 1;
        nextKey = keyMin;
    }
}
