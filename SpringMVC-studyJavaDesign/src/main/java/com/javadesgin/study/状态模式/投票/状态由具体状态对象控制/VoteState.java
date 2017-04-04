package com.javadesgin.study.状态模式.投票.状态由具体状态对象控制;

public interface VoteState {

    public void vote(String user, String voteItem, VoteManager manager);
}