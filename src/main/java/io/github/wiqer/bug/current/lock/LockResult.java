package io.github.wiqer.bug.current.lock;

import java.util.concurrent.locks.Lock;

public class LockResult<T> implements AutoCloseable {
    private final Lock lock;
    private final LockInfo lockInfo;
    private final long waitTime;
    private final Thread holdingThread;
    public LockResult(Lock lock, LockInfo lockInfo, long waitTime, Thread holdingThread) {
        this.lock = lock;
        this.lockInfo = lockInfo;
        this.waitTime = waitTime;
        this.holdingThread = holdingThread;
    }
    public Lock getLock() { return lock; }
    public LockInfo getLockInfo() { return lockInfo; }
    public long getWaitTime() { return waitTime; }
    public Thread getHoldingThread() { return holdingThread; }
    @Override
    public void close() {
        if (lock != null) {
            lock.unlock();
            if (holdingThread != null) {
                lockInfo.recordLockRelease(holdingThread);
            }
            if (lock instanceof java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock) {
                lockInfo.decrementReadCount();
            }
        }
    }
    @Override
    public String toString() {
        return "LockResult{lock=" + lock.getClass().getSimpleName() + ", waitTime=" + waitTime + "ms" + ", holdingThread=" + (holdingThread != null ? holdingThread.getName() : "null") + "}";
    }
} 