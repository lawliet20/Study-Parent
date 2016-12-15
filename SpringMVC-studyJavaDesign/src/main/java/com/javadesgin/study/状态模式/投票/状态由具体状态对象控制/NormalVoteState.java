package com.javadesgin.study.状态模式.投票.状态由具体状态对象控制;

public class NormalVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        manager.getMapVote().put(user, voteItem);
        System.out.println("投票成功！");
        manager.getMapState().put(user, new RepeatVoteState());
    }
}