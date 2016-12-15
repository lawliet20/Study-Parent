package com.javadesgin.study.状态模式.投票.状态由具体状态对象控制;

public class RepeatVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        System.out.println("请不要重复投票。");
        if (manager.getMapVoteCount().get(user) >= 4) {
            manager.getMapState().put(user, new SpiteVoteState());
        }
    }
}