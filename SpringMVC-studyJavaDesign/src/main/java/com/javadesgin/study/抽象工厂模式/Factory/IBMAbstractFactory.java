package com.javadesgin.study.抽象工厂模式.Factory;

import com.javadesgin.study.抽象工厂模式.product.CPU;
import com.javadesgin.study.抽象工厂模式.product.Mainboard;

/**
 * 主板
 * Created by sherry on 2016/11/4.
 */
abstract public class IBMAbstractFactory {

    abstract Mainboard getIBMManboard();

    abstract CPU getIBMCpu();
}
