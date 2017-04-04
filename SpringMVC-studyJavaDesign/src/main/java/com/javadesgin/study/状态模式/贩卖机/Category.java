package com.javadesgin.study.状态模式.贩卖机;

import java.util.EnumMap;

import static com.javadesgin.study.状态模式.贩卖机.Input.*;

/**
 * 对自动售货机的状态分类
 */
public enum Category {
    /**
     * 放入钞票*
     */
    MONEY(NICKEL, DIME, QUARTER, DOLLAR),

    /**
     * 选择商品*
     */
    ITEM_SELECTION(TOOTHPASTE, CHIPS, SODA, SOAP),

    /**
     * 退出*
     */
    QUIT_TRANSACTION(ABORT_TRANSACTION),

    /**
     * 关机*
     */
    SHUT_DOWN(STOP);

    private Input[] values;


    Category(Input... types) {
        values = types;
    }

    public static EnumMap<Input, Category> categories = new EnumMap<Input, Category>(Input.class);

    public Input[] getValues() {
        return values;
    }

    //初始化自动售货机状态集合   
    static {
        for (Category c : Category.class.getEnumConstants()) {
            for (Input input : c.values) {
                categories.put(input, c);
            }
        }
    }

    /**
     * 返回该操作项所属状态*
     */
    public static Category categorize(Input input) {
        return categories.get(input);
    }

}