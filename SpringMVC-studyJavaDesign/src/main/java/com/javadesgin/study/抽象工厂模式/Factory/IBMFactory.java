package com.javadesgin.study.抽象工厂模式.Factory;

import com.javadesgin.study.抽象工厂模式.product.CPU;
import com.javadesgin.study.抽象工厂模式.product.IBMCpu;
import com.javadesgin.study.抽象工厂模式.product.IBMMainboard;
import com.javadesgin.study.抽象工厂模式.product.Mainboard;

/**
 * IBM产品工厂
 * Created by sherry on 2016/11/4.
 */
public class IBMFactory extends IBMAbstractFactory {

    @Override
    Mainboard getIBMManboard() {
        Mainboard mainboard = new IBMMainboard();
        mainboard.getName();
        return mainboard;
    }

    @Override
    CPU getIBMCpu() {
        CPU cpu = new IBMCpu();
        cpu.getName();
        return cpu;
    }
}
