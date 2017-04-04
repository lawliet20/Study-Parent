package com.javadesgin.study.状态模式.投票.状态由具体状态对象控制;

public class BlackVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        System.out.println("你进入了黑名单。");
    }
}