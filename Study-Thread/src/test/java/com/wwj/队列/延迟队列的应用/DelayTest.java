package com.wwj.队列.延迟队列的应用;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列demo
 */
public class DelayTest {

    static class DelayAlarm implements Delayed {

        public String id;
        public long daltime;
        public long delay;
        public long time;

        public DelayAlarm(String id) {
            this.id = id;
        }

        public DelayAlarm(String id, long daltime, long delay) {
            this.id = id;
            this.daltime = daltime;
            this.delay = delay;
            this.time = this.daltime + this.delay;
        }

        /**
         * 预期时间在前的大:return 1
         */
        @Override
        public int compareTo(Delayed o) {
            long result = ((DelayAlarm) o).getTime() - this.getTime();
            if (result < 0)
                return 1;
            if (result > 0)
                return -1;
            return 0;
        }

        /**
         * 预期时间-当前时间
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time * 1000L - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * 根据流水号删除
         */
        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            DelayAlarm a = (DelayAlarm) o;
            if (null == this.getId() || null == a.getId())
                return false;
            return this.id.equals(a.getId());
        }

        public String getId() {
            return id;
        }

        public long getDaltime() {
            return daltime;
        }

        public long getDelay() {
            return delay;
        }

        public long getTime() {
            return time;
        }

        public String toString() {
            return "{id:" + id + ", daltime:" + daltime + ", delay:" + delay + " , time:" + time + "}";
        }
    }

    //缓存队列
    public static DelayQueue<DelayAlarm> queue = new DelayQueue<DelayAlarm>();
    //
    public static volatile boolean stop;

    public static void push(DelayAlarm a) {
        if (!queue.offer(a)) {
            System.out.println("/- push error.");
        }
    }

    static class Consumer extends Thread {

        public void run() {
            while (!stop) {
                try {
                    DelayAlarm a = queue.take();
                    System.out.println(System.currentTimeMillis() / 1000L + "----" + a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Consumer().start();
        long daltime = System.currentTimeMillis() / 1000;
        DelayAlarm a = new DelayAlarm("001", daltime, 1L);
        DelayAlarm b = new DelayAlarm("002", daltime, 2L);
        DelayAlarm c = new DelayAlarm("003", daltime, 3L);
        DelayAlarm d = new DelayAlarm("004", daltime, 4L);
        DelayAlarm f = new DelayAlarm("005", daltime, 10L);
        push(a);
        push(b);
        push(c);
        push(d);
        push(f);
        //移除003的告警
        DelayAlarm g = new DelayAlarm("003");
        if (queue.contains(g)) {
            queue.remove(g);
        }
    }
}
