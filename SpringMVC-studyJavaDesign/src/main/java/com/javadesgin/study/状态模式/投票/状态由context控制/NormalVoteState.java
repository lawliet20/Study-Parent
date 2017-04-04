package com.javadesgin.study.状态模式.投票.状态由context控制;

public class NormalVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        manager.getMapVote().put(user, voteItem);
        System.out.println("恭喜你投票成功！");
    }
}