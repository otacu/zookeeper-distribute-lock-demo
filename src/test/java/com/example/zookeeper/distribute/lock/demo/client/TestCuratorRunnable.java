package com.example.zookeeper.distribute.lock.demo.client;

import com.example.zookeeper.distribute.lock.demo.pojo.LockInfo;
import org.apache.curator.framework.CuratorFramework;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TestCuratorRunnable implements Runnable {

    /** 处理main线程阻塞（等待所有子线程） */
    private CountDownLatch countDown;

    /** 线程名字 */
    private String  threadName;

    private CuratorFramework zookeeperClient;


    public TestCuratorRunnable(CountDownLatch countDownLatch, String threadName, CuratorFramework zookeeperClient) {
        this.countDown = countDownLatch;
        this.threadName = threadName;
        this.zookeeperClient = zookeeperClient;
    }

    @Override
    public void run() {
        System.out.println( "[" + threadName + "] Running ! [countDownLatch.getCount() = " + countDown.getCount() + "]." );

        boolean isLock = false;
        CuratorDistributedLock lock = null;
        try {
            LockInfo lockInfo = new LockInfo("/zk-lock-"+new Random().nextInt(10), "person"+ System.currentTimeMillis()+new Random().nextInt(100));
            lock = new CuratorDistributedLock(this.zookeeperClient, lockInfo);

            isLock = lock.lock();
            System.out.println(lockInfo.getLockOwner() + "获取锁结果："+isLock);

            if(isLock){
                //TODO 获取到锁，处理相关业务

                System.out.println(" 获取到锁，处理相关业务"+lockInfo.getLockOwner());
                Thread.sleep(200);  //就按照每个业务处理平均200ms完成
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (lock != null &&  isLock) {
                lock.unlock();
            }
            // 每个独立子线程执行完后,countDownLatch值减1
            countDown.countDown();
        }

    }

}