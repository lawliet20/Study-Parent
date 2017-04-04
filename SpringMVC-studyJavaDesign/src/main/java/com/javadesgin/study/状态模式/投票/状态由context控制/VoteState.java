package com.javadesgin.study.状态模式.投票.状态由context控制;

public interface VoteState {

    /**
     * 处理状态对应的行为
     *
     * @param user     投票人
     * @param voteItem 投票项
     * @param manager  投票上下文，用来实现状态对应的功能处理的时候，可以回调上下文的数据
     */
    public void vote(String user, String voteItem, VoteManager manager);
}