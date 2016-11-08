package com.javadesgin.study.状态模式.贩卖机;

public class Enums
{  
    private static java.util.Random rand = new java.util.Random();  
  
    public static <T extends Enum<T>> T random(Class<T> ec) {  
        System.out.print("随机抽取" + ec + "中的一个元素:");  
        return random(ec.getEnumConstants());  
    }  
  
    public static <T> T random(T[] values){  
        int index = rand.nextInt(values.length);  
        //System.out.println(".index: " + index);
  
        return values[index];  
    }  
}  