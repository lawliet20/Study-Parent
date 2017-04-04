package com.javadesgin.study.状态模式.地铁卖票机;

import com.javadesgin.study.状态模式.贩卖机.Enums;

/**
 * 地铁自动售票机
 * Created by sherry on 2016/11/6.
 */
public class VendingMachine {
    //消费者投入的金额
    private static int amount;
    //消费者选择的商品
    private static Input select;
    //售卖机初始状态为放入钱
    private static State state = State.PUT_MONEY;
    //交易结束标示
    public static boolean isEnd;

    /**
     * 自动售票机状态
     */
    enum State {
        //放入钱
        PUT_MONEY {
            void next(Input input) {
                switch (Category.categoryInput(input)) {
                    case MONEY:
                        amount += input.value;
                        System.out.println("您投入了" + input.value + "元，现在是" + amount + "元。");
                        state = SELECT_TICKET;
                        break;
                    case QUITE_TRANSACTION:
                        System.out.println("退出");
                        //TODO
                        break;
                    case SHUT_DOWN:
                        System.out.println("关机");
                        //TODO
                        break;
                    default:
                        state = PUT_MONEY;
                        break;
                }
            }
        },
        //选择地铁票
        SELECT_TICKET {
            void next(Input input) {
                switch (Category.categoryInput(input)) {
                    case MONEY:
                        amount += input.value;
                        System.out.println("您投入了" + input.value + "元,现在金额是" + amount + "元。");
                        break;
                    case TICKET:
                        select = input;
                        if (amount >= input.value) {
                            System.out.println("票的价格为：" + input.value + ",您投入了" + amount + "元。");
                            state = DISPENSING;
                        } else {
                            System.out.println("票的价格为：" + input.value + ",您投入了" + amount + "元。请继续投钱。");
                            state = PUT_MONEY;
                        }
                        break;
                    case QUITE_TRANSACTION:
                        System.out.println("退出");
                        //TODO
                        break;
                    case SHUT_DOWN:
                        System.out.println("关机");
                        //TODO
                        break;
                    default:
                        state = PUT_MONEY;
                        break;
                }
            }
        },
        //发出商品
        DISPENSING {
            void next() {
                System.out.println("请拿好你的票" + select);
                if (select.value == amount) {
                    state = TERMINAL;
                } else if (select.value < amount) {
                    System.out.println("请等待找零!");
                    amount -= select.value;
                    select = null;
                    state = GIVING_MONEY;
                } else {
                    System.out.println("投入的金额小于商品价格，请继续投币!");
                    state = PUT_MONEY;
                }
            }
        },
        //找零
        GIVING_MONEY {
            void next() {
                System.out.println("找零" + amount + "元。");
                amount = 0;
                state = TERMINAL;
            }
        },
        //交易结束
        TERMINAL {
            void next() {
                System.out.println("交易结束，欢迎下次再来!");
                isEnd = true;
            }
        };

        void next() {
            System.out.println("持续状态中，请勿进行其他操作！");
        }

        void next(Input input) {
            System.out.println("请选择要进行的操作！");
        }
    }


    public static void main(String[] args) {
        while (!isEnd) {
            switch (state) {
                case PUT_MONEY:
                    state.next(Enums.random(Category.MONEY.values));
                    break;
                case SELECT_TICKET:
                    if (null == select) {
                        select = Enums.random(Category.TICKET.values);
                    }
                    state.next(select);
                    break;
                case DISPENSING:
                    state.next();
                case GIVING_MONEY:
                    state.next();
                    break;
                case TERMINAL:
                    state.next();
                    break;
            }
        }
    }
}
