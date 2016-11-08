package com.javadesgin.study.抽象工厂模式.Factory;

import com.javadesgin.study.抽象工厂模式.product.CPU;
import com.javadesgin.study.抽象工厂模式.product.Mainboard;

/**
 * Intel工厂
 * Created by sherry on 2016/11/4.
 */
abstract public class IntelAbstractFactory {

    abstract Mainboard getIntelManboard();

    abstract CPU getIntelCpu();
}
