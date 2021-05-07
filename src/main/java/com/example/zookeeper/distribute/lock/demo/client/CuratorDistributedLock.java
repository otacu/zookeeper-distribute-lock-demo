package com.example.zookeeper.distribute.lock.demo.client;

import com.example.zookeeper.distribute.lock.demo.pojo.LockInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public class CuratorDistributedLock {

    public CuratorDistributedLock(CuratorFramework zookeeperClient, LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        //Curator提供的InterProcessMutex是分布式锁的实现。通过acquire获得锁，并提供超时机制，release方法用于释放锁。
        lock = new InterProcessMutex(zookeeperClient, lockInfo.getLockname());
    }

    private LockInfo lockInfo;
    private InterProcessMutex lock;

    public boolean lock() {

        try {
            if (lock.acquire(1, TimeUnit.SECONDS))
            {
                System.out.println(lockInfo.getLockOwner() + " has the lock:"+lockInfo.getLockname());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean tryLock() {
        return false;
    }

    public boolean unlock() {
        try {

            lock.release();

            return true;

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }
}
