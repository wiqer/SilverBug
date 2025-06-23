package io.github.wiqer.bug.current.lock;

public class LockProperties {
    private boolean fair = true;
    private long timeout = 5000;
    private int maxLocks = 10000;
    private long cleanupInterval = 60000;
    private LockStrategy strategy = LockStrategy.FAIR;
    private boolean deadlockDetection = false;
    private long deadlockDetectionInterval = 30000;
    private long lockTimeout = 300000;
    public LockProperties() {}
    public LockProperties(boolean fair, long timeout, int maxLocks, long cleanupInterval, LockStrategy strategy, long lockTimeout) {
        this.fair = fair;
        this.timeout = timeout;
        this.maxLocks = maxLocks;
        this.cleanupInterval = cleanupInterval;
        this.strategy = strategy;
        this.lockTimeout = lockTimeout;
    }
    public boolean isFair() { return fair; }
    public void setFair(boolean fair) { this.fair = fair; }
    public long getTimeout() { return timeout; }
    public void setTimeout(long timeout) { this.timeout = timeout; }
    public int getMaxLocks() { return maxLocks; }
    public void setMaxLocks(int maxLocks) { this.maxLocks = maxLocks; }
    public long getCleanupInterval() { return cleanupInterval; }
    public void setCleanupInterval(long cleanupInterval) { this.cleanupInterval = cleanupInterval; }
    public LockStrategy getStrategy() { return strategy; }
    public void setStrategy(LockStrategy strategy) { this.strategy = strategy; }
    public boolean isDeadlockDetection() { return deadlockDetection; }
    public void setDeadlockDetection(boolean deadlockDetection) { this.deadlockDetection = deadlockDetection; }
    public long getDeadlockDetectionInterval() { return deadlockDetectionInterval; }
    public void setDeadlockDetectionInterval(long deadlockDetectionInterval) { this.deadlockDetectionInterval = deadlockDetectionInterval; }
    public long getLockTimeout() { return lockTimeout; }
    public void setLockTimeout(long lockTimeout) { this.lockTimeout = lockTimeout; }
    public static LockProperties createReadPreferred() { LockProperties props = new LockProperties(); props.setStrategy(LockStrategy.READ_PREFERRED); return props; }
    public static LockProperties createWritePreferred() { LockProperties props = new LockProperties(); props.setStrategy(LockStrategy.WRITE_PREFERRED); return props; }
    public static LockProperties createHighPerformance() { LockProperties props = new LockProperties(); props.setFair(false); props.setStrategy(LockStrategy.READ_PREFERRED); return props; }
    public static LockProperties createMemoryOptimized() { LockProperties props = new LockProperties(); props.setMaxLocks(1000); props.setCleanupInterval(30000); props.setLockTimeout(60000); return props; }
    @Override
    public String toString() { return "LockProperties{fair=" + fair + ", timeout=" + timeout + ", maxLocks=" + maxLocks + ", cleanupInterval=" + cleanupInterval + ", strategy=" + strategy + ", deadlockDetection=" + deadlockDetection + ", deadlockDetectionInterval=" + deadlockDetectionInterval + ", lockTimeout=" + lockTimeout + "}"; }
} 