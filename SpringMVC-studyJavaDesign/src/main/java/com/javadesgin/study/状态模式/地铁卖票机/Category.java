package com.javadesgin.study.状态模式.地铁卖票机;

import java.util.EnumMap;

import static com.javadesgin.study.状态模式.地铁卖票机.Input.*;

/**
 * 对Input枚举进行分类
 * Created by sherry on 2016/11/6.
 */
public enum Category {

    MONEY(UNITARY, TWOSPOT, FIVESPOT, TENSPOT),
    TICKET(TICKET2, TICKET5),
    //退出
    QUITE_TRANSACTION(ABORT_TRANSACTION),
    //完成
    SHUT_DOWN(STOP);

    Input[] values;

    Category() {

    }

    Category(Input... input) {
        this.values = input;
    }

    private static EnumMap<Input, Category> enumMap = new EnumMap<Input, Category>(Input.class);

    static {
        Category[] categories = Category.class.getEnumConstants();
        for (Category c : categories) {
            for(Input input:c.values){
                enumMap.put(input,c);
            }
        }
    }

    public static Category categoryInput(Input input) {
        return enumMap.get(input);
    }
}
