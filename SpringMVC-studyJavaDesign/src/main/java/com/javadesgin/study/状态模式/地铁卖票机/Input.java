package com.javadesgin.study.状态模式.地铁卖票机;

/**
 * 卖票机所有的枚举
 * Created by sherry on 2016/11/4.
 */
public enum Input {

    //一元
    UNITARY(1),
    //两元
    TWOSPOT(2),
    //五元
    FIVESPOT(5),
    //十元
    TENSPOT(10),

    //地铁2元票
    TICKET2(2),
    //地铁5元票
    TICKET5(5),

    ABORT_TRANSACTION{
        public int amount(){
            throw new RuntimeException("退出时不能获取余额");
        }
    },
    STOP{
        public int amount(){
            throw new RuntimeException("关机时不能获取余额");
        }
    };

    public int value;

    Input(){

    }

    Input(int value){
        this.value = value;
    }

    /**
     * 返回该操作项的金额
     */
    int amount(){
        return value;
    }

}
