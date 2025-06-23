package io.github.wiqer.bug.current.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class LockInfo {
    private final ReentrantReadWriteLock lock;
    private final AtomicInteger readCount;
    private volatile long lastAccessTime;
    private volatile boolean markedForRemoval;
    private final Set<Thread> holdingThreads = ConcurrentHashMap.newKeySet();
    private final Set<Thread> waitingThreads = ConcurrentHashMap.newKeySet();
    public LockInfo(ReentrantReadWriteLock lock) {
        this.lock = lock;
        this.readCount = new AtomicInteger(0);
        this.lastAccessTime = System.currentTimeMillis();
        this.markedForRemoval = false;
    }
    public ReentrantReadWriteLock getLock() { return lock; }
    public AtomicInteger getReadCount() { return readCount; }
    public long getLastAccessTime() { return lastAccessTime; }
    public void setLastAccessTime(long lastAccessTime) { this.lastAccessTime = lastAccessTime; }
    public boolean isMarkedForRemoval() { return markedForRemoval; }
    public void setMarkedForRemoval(boolean markedForRemoval) { this.markedForRemoval = markedForRemoval; }
    public void incrementReadCount() { readCount.incrementAndGet(); lastAccessTime = System.currentTimeMillis(); holdingThreads.add(Thread.currentThread()); }
    public void decrementReadCount() { int count = readCount.decrementAndGet(); holdingThreads.remove(Thread.currentThread()); if (count <= 0) { markedForRemoval = true; } }
    public void recordLockAcquisition(Thread thread) { holdingThreads.add(thread); lastAccessTime = System.currentTimeMillis(); }
    public void recordLockRelease(Thread thread) { holdingThreads.remove(thread); }
    public void recordThreadWaiting(Thread thread) { waitingThreads.add(thread); }
    public void recordThreadStoppedWaiting(Thread thread) { waitingThreads.remove(thread); }
    public boolean hasActiveHoldingThreads() { return holdingThreads.stream().anyMatch(Thread::isAlive); }
    public boolean hasActiveWaitingThreads() { return waitingThreads.stream().anyMatch(Thread::isAlive); }
    public void cleanupDeadThreads() { holdingThreads.removeIf(thread -> !thread.isAlive()); waitingThreads.removeIf(thread -> !thread.isAlive()); }
    public int getHoldingThreadCount() { return holdingThreads.size(); }
    public int getWaitingThreadCount() { return waitingThreads.size(); }
    @Override
    public String toString() { return "LockInfo{readCount=" + readCount.get() + ", lastAccessTime=" + lastAccessTime + ", markedForRemoval=" + markedForRemoval + ", holdingThreads=" + holdingThreads.size() + ", waitingThreads=" + waitingThreads.size() + "}"; }
} 