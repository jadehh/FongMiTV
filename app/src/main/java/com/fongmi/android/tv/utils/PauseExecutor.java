package com.fongmi.android.tv.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PauseExecutor extends ThreadPoolExecutor {

    private final ReentrantLock pauseLock;
    private final Condition condition;
    private boolean isPaused;

    public PauseExecutor(int corePoolSize) {
        super(corePoolSize, corePoolSize, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        pauseLock = new ReentrantLock();
        condition = pauseLock.newCondition();
    }


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) condition.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            condition.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}

