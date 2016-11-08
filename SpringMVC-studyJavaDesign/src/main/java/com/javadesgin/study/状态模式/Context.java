package com.javadesgin.study.状态模式;

/**
 * 状态模式：对象的行为根据状态的变化而变化
 * Created by sherry on 2016/11/2.
 */
public class Context {
    public static void main(String[] args) {
        Context context = new Context();
        if ("i like you" == "i love you") {
            StateImpl1 state1 = new StateImpl1();
            context.setState(state1);
        }else{
            StateImpl2 state2 = new StateImpl2();
            context.setState(state2);
        }
        context.operation1();
        context.operation2();
        context.operation3();
    }

    private State state;

    public void setState(State state) {
        this.state = state;
    }

    public void operation1() {
        state.operation1();
    }

    public void operation2() {
        state.operation2();
    }

    public void operation3() {
        state.operation2();
    }

}
