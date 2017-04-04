package com.javadesgin.study.状态模式.投票.状态由context控制;

public class SpiteVoteState implements VoteState {

    @Override
    public void vote(String user, String voteItem, VoteManager manager) {
        String s = manager.getMapVote().get(user);
        if (s != null) {
            manager.getMapVote().remove(user);
        }
        System.out.println("你有恶意刷票行为，取消投票资格。");
    }
}