package com.wwj.redis.test.秒杀;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;

/**
 * redis实现秒杀活动
 * Created by sherry on 2016/12/21.
 */
public class MiaoSha {
    private Jedis jedis = new Jedis("localhost", 6379);

    public static void main(String[] args) {
        for(int i=0;i<100;i++)
            new Thread() {
                @Override
                public void run() {
                    MiaoSha ms = new MiaoSha();
                    ms.miaoSha();
                }
            }.start();
    }

    public void miaoSha(){
        System.out.println("开始秒杀...");
        //用户id
        int userId = new Random().nextInt(100);
        //奖池金额
        int totalMoney = 1000;
        if(jedis.get("totalMoney") != null){
            totalMoney = Integer.parseInt(jedis.get("totalMoney"))-1;
            System.out.println("现在奖池金额："+totalMoney);
        }else{
            jedis.set("totalMoney",totalMoney+"");
        }
        if(jedis.sismember("user",userId+"")){
            System.out.println(userId+"已经抽过奖了，不要重复参与。");
            return;
        }
        if(totalMoney<=0){
            System.out.println("奖池金额已全部发放完毕。");
            return;
        }
        String res = jedis.watch("totalMoney");
        if(res.equals("OK")){
            Transaction trans = jedis.multi();
            trans.set("totalMoney",totalMoney+"");
            List<Object> list = trans.exec();
            if(null == list || list.isEmpty()){
                System.out.println("抱歉，请下次再来。");
            }else{
                System.out.println(Thread.currentThread().getName()+"->恭喜你 "+userId+" ,你已经抽中大奖。");
                jedis.sadd("user",userId+"");
            }
        }

    }
}
