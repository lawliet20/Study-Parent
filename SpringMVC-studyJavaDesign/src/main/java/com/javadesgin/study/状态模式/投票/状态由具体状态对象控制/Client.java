package com.javadesgin.study.状态模式.投票.状态由具体状态对象控制;

public class Client {

    public static void main(String[] args) {
        VoteManager manager = new VoteManager();
        for (int i = 0; i < 8; i++) {
            manager.vote("JOE", "A");
        }
    }
}