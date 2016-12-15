package com.javadesgin.study.状态模式.投票.状态由context控制;

public class RepeatVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        System.out.println("请不要重复投票！");
    }
}