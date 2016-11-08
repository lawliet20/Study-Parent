package com.javadesgin.study.抽象工厂模式.Factory;

import com.javadesgin.study.抽象工厂模式.product.CPU;
import com.javadesgin.study.抽象工厂模式.product.Mainboard;

/**
 * Intel的factory
 * Created by sherry on 2016/11/4.
 */
public class IntelFactory extends IntelAbstractFactory{

    @Override
    Mainboard getIntelManboard() {
        return null;
    }

    @Override
    CPU getIntelCpu() {
        return null;
    }
}
