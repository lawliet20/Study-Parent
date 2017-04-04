package com.javadesgin.study.状态模式.投票.状态由context控制;

public class BlackVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        System.out.println("进入黑名单，将禁止登陆和使用本系统。");
    }
}