package com.wwj.jdk18新特性;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by sherry on 2017/10/11.
 * 参考网址：http://www.jianshu.com/p/4897ccdcb278
 */
public class CompletableFutureTest {

    @Test
    public void thenApply(){
        String result = CompletableFuture.supplyAsync(() -> "hello").thenApply(s -> s + " world").join();
        System.out.println(result);
    }

    @Test
    public void testThen() throws ExecutionException, InterruptedException {
        final Executor executor = Executors.newFixedThreadPool(10, (Runnable r) -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }
}
