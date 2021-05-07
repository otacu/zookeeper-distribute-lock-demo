package com.example.zookeeper.distribute.lock.demo.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class Test1 {

    @Autowired
    ZookeeperClient zookeeperClient;

    @Test
    public void test() throws InterruptedException {
        int countNum = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(countNum);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < countNum; i++) {

            new Thread(new TestCuratorRunnable(countDownLatch,"子线程" + (i+100), zookeeperClient.getClient())).start();
        }

        System.out.println("主线程阻塞,等待所有子线程执行完成");
        countDownLatch.await();
        System.out.println("所有线程执行完成!");
        long end = System.currentTimeMillis();
        System.out.println("----------所有线程执行完成!------时间花费："+(end -begin)/1000 +"s");
    }
}
