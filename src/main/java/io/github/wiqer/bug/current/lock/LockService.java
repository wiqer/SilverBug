package io.github.wiqer.bug.current.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ：LockService
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  11:07
 * description：默认
 * modified By：llf.lilanfeng
 */
@Service
@Slf4j
public class LockService {
    private final Map<String, ReentrantReadWriteLock> cacheLockMap = new ConcurrentHashMap<>();
    @Bean("cacheLockMap")
    public Map<String, ReentrantReadWriteLock> cacheLockMap() {
        return cacheLockMap;
    }

    public <T> Lock getLockKey(LockData<T> lockData, Consumer<T> consumer){
        String id = lockData.getLockId();
        ReentrantReadWriteLock lock = cacheLockMap.get(id);
        if(lock == null){
            synchronized (cacheLockMap){
                lock = cacheLockMap.get(id);
                if(lock == null){
                    lock = new ReentrantReadWriteLock(true);
                    cacheLockMap.put(id, lock);
                    lock.writeLock().lock();
                    try {
                        consumer.accept(lockData.getData());
                    }finally {
                        lock.writeLock().unlock();
                        cacheLockMap.remove(id);
                    }
                    return null;
                }
            }
        }
        return lock.readLock();
    }

    /**
     * 但线程补全
     * @param data
     * @param readFunction
     * @param writeConsumer
     * @return
     * @param <T>
     */
    public  <T> T singleThreadedWrite(LockData<T> data, Function<T, T> readFunction, Consumer<T> writeConsumer) {
        T res = null;
        Lock lock = getLockKey(data, writeConsumer);
        if(lock == null){
            return readFunction.apply(data.getData());
        }
        if(lock instanceof ReentrantReadWriteLock.ReadLock){
            lock.lock();
            try {
                res = readFunction.apply(data.getData());
            }
            finally {
                lock.unlock();
            }
        }
        return res;
    }
}
