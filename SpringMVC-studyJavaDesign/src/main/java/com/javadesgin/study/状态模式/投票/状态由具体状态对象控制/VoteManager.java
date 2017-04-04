package com.javadesgin.study.状态模式.投票.状态由具体状态对象控制;

import java.util.HashMap;
import java.util.Map;

public class VoteManager {
          /**
           * 记录当前每个用户对应的状态处理对象，每个用户当前的状态是不同的
           * Map<String, VoteState>对应Map<用户名，当前对应的状态处理对象>
           */
          private Map<String, VoteState> mapState = new HashMap<String, VoteState>();
          /**
           * 记录用户投票的结果，Map<String,String>对应Map<用户名，投票的选项>
           */
          private Map<String, String> mapVote = new HashMap<String, String>();
          /**
           * 记录用户投票次数，Map<String,Integer> 对应Map<用户名，投票的次数>
           */
          private Map<String, Integer> mapVoteCount = new HashMap<String, Integer>();
          /**
           * 获取记录用户投票结果的Map
           * @return 记录用户投票结果的Map
           */
          public Map<String, String> getMapVote() {
                    return mapVote;
          }
          /**
           * 获取记录当前每个用户对应的状态处理对象
           * @return
           */
          public Map<String, VoteState> getMapState() {
                    return mapState;
          }
          /**
           * 获取记录每个用户对应的投票次数的Map
           * @return
           */
          public Map<String, Integer> getMapVoteCount() {
                    return mapVoteCount;
          }
 
          public void vote(String user, String voteItem){
                    Integer oldVoteCount = mapVoteCount.get(user);
                    if(oldVoteCount == null){
                             oldVoteCount = 0;
                    }
                   oldVoteCount++;
                   mapVoteCount.put(user, oldVoteCount);
  
                   VoteState state = mapState.get(user);
                   if(state == null){
                           state = new NormalVoteState();
                   }
                   state.vote(user, voteItem, this);
          } 
}