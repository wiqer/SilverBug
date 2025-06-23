package io.github.wiqer.bug.current;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.wiqer.bug.utils.Assert;

/**
 * ：ConcurrentService
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  11:07
 * description：默认
 * modified By：llf.lilanfeng
 */
@Slf4j
public class ConcurrentService implements AutoCloseable {
    private final ExecutorService executorService;
    private final String name;
    private final ThreadPoolMonitor monitor;
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final int maxConcurrentTasks;

    public ConcurrentService(ExecutorService executorService, String name) {
        this.executorService = executorService;
        this.name = name;
        this.monitor = new ThreadPoolMonitor(executorService, name);
        if (executorService instanceof ThreadPoolExecutor) {
            this.maxConcurrentTasks = ((ThreadPoolExecutor) executorService).getMaximumPoolSize() * 2;
        } else {
            this.maxConcurrentTasks = Runtime.getRuntime().availableProcessors() * 2;
        }
    }
    public ConcurrentService(String name) {
        this(createDefaultExecutor(name), name);
    }
    public ConcurrentService(String name, int coreSize, int maxSize, int queueSize, long keepAliveTime) {
        this(createCustomExecutor(name, coreSize, maxSize, queueSize, keepAliveTime), name);
    }
    private static ExecutorService createDefaultExecutor(String name) {
        return new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new NamedThreadFactory(name),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    private static ExecutorService createCustomExecutor(String name, int coreSize, int maxSize, int queueSize, long keepAliveTime) {
        return new ThreadPoolExecutor(
            coreSize,
            maxSize,
            keepAliveTime, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(queueSize),
            new NamedThreadFactory(name),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    public <T> void fetchConsumerConcurrentList(Consumer<T> readFunction, List<T> list, int batchSize, long timeout, TimeUnit unit) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        final CountDownLatch latch = new CountDownLatch(list.size());
        final AtomicInteger failedTasks = new AtomicInteger(0);
        for (T param : list) {
            if (activeTasks.get() >= maxConcurrentTasks) {
                log.warn("{} too many active tasks, waiting for available slot", name);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for task slot", e);
                }
            }
            activeTasks.incrementAndGet();
            executorService.submit(() -> {
                try {
                    readFunction.accept(param);
                } catch (Throwable e) {
                    failedTasks.incrementAndGet();
                    log.error("{} task execution failed: {}", name, e.getMessage(), e);
                } finally {
                    activeTasks.decrementAndGet();
                    latch.countDown();
                }
            });
        }
        try {
            boolean finished = latch.await(timeout, unit);
            if (finished) {
                if (failedTasks.get() > 0) {
                    log.warn("{} finished with {} failed tasks", name, failedTasks.get());
                } else {
                    log.info("{} finished successfully", name);
                }
            } else {
                log.warn("{} timeout after {} {}", name, timeout, unit);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for tasks completion", e);
        }
    }
    public <T,R> List<R> fetchConcurrentList(Function<T,R> readFunction, List<T> list) {
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Future<R>> futures = new ArrayList<>();
        for (T param : list) {
            if (activeTasks.get() >= maxConcurrentTasks) {
                log.warn("{} too many active tasks, waiting for available slot", name);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for task slot", e);
                }
            }
            activeTasks.incrementAndGet();
            Future<R> future = executorService.submit(() -> {
                try {
                    return readFunction.apply(param);
                } finally {
                    activeTasks.decrementAndGet();
                }
            });
            futures.add(future);
        }
        List<R> resultList = Collections.synchronizedList(new ArrayList<>());
        for (Future<R> future : futures) {
            try {
                R result = future.get();
                if(result != null) {
                    resultList.add(result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} interrupted while waiting for result", name, e);
            } catch (ExecutionException e) {
                log.error("{} execution failed", name, e);
            }
        }
        return new ArrayList<>(resultList);
    }
    public <T,R> List<R> fetchConcurrentList(Function<List<T>,List<R>> readFunction, List<T> list, int batchSize, long timeout, TimeUnit unit) {
        if(batchSize < 1 || CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Future<List<R>>> futures = new ArrayList<>();
        for (List<T> param : ListUtils.partition(list, batchSize)) {
            if (activeTasks.get() >= maxConcurrentTasks) {
                log.warn("{} too many active tasks, waiting for available slot", name);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for task slot", e);
                }
            }
            activeTasks.incrementAndGet();
            Future<List<R>> future = executorService.submit(() -> {
                try {
                    return readFunction.apply(param);
                } catch (Exception e) {
                    log.error("{} process batch failed: {}", name, e.getMessage(), e);
                    return Collections.emptyList();
                } finally {
                    activeTasks.decrementAndGet();
                }
            });
            futures.add(future);
        }
        List<R> resultList = Collections.synchronizedList(new ArrayList<>());
        for (Future<List<R>> future : futures) {
            try {
                List<R> result = future.get(timeout, unit);
                if(CollectionUtils.isNotEmpty(result)){
                    resultList.addAll(result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} interrupted while waiting for result", name, e);
            } catch (ExecutionException e) {
                log.error("{} execution failed", name, e);
            } catch (TimeoutException e) {
                log.error("{} timeout while waiting for result", name, e);
            }
        }
        return new ArrayList<>(resultList);
    }
    public <T,R,U> List<R> fetchConcurrentList(BiFunction<List<T>,U,List<R>> readBiFunction, List<T> list,U conditionParam, int batchSize, long timeout, TimeUnit unit) {
        if(batchSize < 1 || CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Future<List<R>>> futures = new ArrayList<>();
        for (List<T> param : ListUtils.partition(list, batchSize)) {
            if (activeTasks.get() >= maxConcurrentTasks) {
                log.warn("{} too many active tasks, waiting for available slot", name);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for task slot", e);
                }
            }
            activeTasks.incrementAndGet();
            Future<List<R>> future = executorService.submit(() -> {
                try {
                    return readBiFunction.apply(param, conditionParam);
                } catch (Exception e) {
                    log.error("{} process batch failed: {}", name, e.getMessage(), e);
                    return Collections.emptyList();
                } finally {
                    activeTasks.decrementAndGet();
                }
            });
            futures.add(future);
        }
        List<R> resultList = Collections.synchronizedList(new ArrayList<>());
        for (Future<List<R>> future : futures) {
            try {
                List<R> result = future.get(timeout, unit);
                if(CollectionUtils.isNotEmpty(result)){
                    resultList.addAll(result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} interrupted while waiting for result", name, e);
            } catch (ExecutionException e) {
                log.error("{} execution failed", name, e);
            } catch (TimeoutException e) {
                log.error("{} timeout while waiting for result", name, e);
            }
        }
        return new ArrayList<>(resultList);
    }
    public <T,R,U> Map<String,R> fetchConcurrentStringKeyMap(BiFunction<List<T>,U,Map<String,R>> readBiFunction, List<T> list, U conditionParam, int batchSize, long timeout, TimeUnit unit) {
        if(batchSize < 1 || CollectionUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        list = list.stream().distinct().collect(Collectors.toList());
        List<Future<Map<String,R>>> futures = new ArrayList<>();
        for (List<T> param : ListUtils.partition(list, batchSize)) {
            if (activeTasks.get() >= maxConcurrentTasks) {
                log.warn("{} too many active tasks, waiting for available slot", name);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for task slot", e);
                }
            }
            activeTasks.incrementAndGet();
            Future<Map<String,R>> future = executorService.submit(() -> {
                try {
                    return readBiFunction.apply(param, conditionParam);
                } catch (Exception e) {
                    log.error("{} process batch failed: {}", name, e.getMessage(), e);
                    return Collections.emptyMap();
                } finally {
                    activeTasks.decrementAndGet();
                }
            });
            futures.add(future);
        }
        Map<String,R> resultList = new HashMap<>();
        for (Future<Map<String,R>> future : futures) {
            try {
                Map<String,R> result = future.get(timeout, unit);
                if(MapUtils.isNotEmpty(result)){
                    resultList.putAll(result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} interrupted while waiting for result", name, e);
            } catch (ExecutionException e) {
                log.error("{} execution failed", name, e);
            } catch (TimeoutException e) {
                log.error("{} timeout while waiting for result", name, e);
            }
        }
        return resultList;
    }
    public Map<Callable<?>, Object> fetchCallableReturnResultMap(long timeout, TimeUnit unit, Callable<?>... callables) {
        Assert.notNull(callables , "can't newInstance by callables");
        int capacity = computeArrayListCapacity(callables.length);
        ArrayList<Callable<?>> list = new ArrayList<>(capacity);
        Collections.addAll(list, callables);
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        Map<Future<?>, Callable<?>> futureMap = new HashMap<>();
        for (Callable<?> callable : list) {
            Future<?> future = executorService.submit(callable);
            futureMap.put(future, callable);
        }
        Map<Callable<?>, Object> resultMap = new HashMap<>();
        for (Map.Entry<Future<?>, Callable<?>> entry: futureMap.entrySet()) {
            Future<?> future = entry.getKey();
            try {
                Object result = future.get(timeout, unit);
                if(result != null) {
                    resultMap.put(entry.getValue(), result);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} interrupted while waiting for result", name, e);
            } catch (ExecutionException e) {
                log.error("{} execution failed", name, e);
            } catch (TimeoutException e) {
                log.error("{} timeout while waiting for result", name, e);
            }
        }
        return resultMap;
    }
    public <T> T getResultFromMap(Map<Callable<?>, Object> resultMap, Callable<T> callable){
        Object res =  resultMap.get(callable);
        return  res == null ? null : (T)res;
    }
    static int computeArrayListCapacity(int arraySize) {
        Assert.checkNonnegative(arraySize, "arraySize");
        return arraySize;
    }
    @Override
    public void close() {
        monitor.stop();
        executorService.shutdown();
    }
    private static class ThreadPoolMonitor {
        private final ExecutorService executorService;
        private final String name;
        private final ScheduledExecutorService scheduler;
        public ThreadPoolMonitor(ExecutorService executorService, String name) {
            this.executorService = executorService;
            this.name = name;
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
            startMonitoring();
        }
        private void startMonitoring() {
            scheduler.scheduleAtFixedRate(() -> {
                if (executorService instanceof ThreadPoolExecutor) {
                    ThreadPoolExecutor tpe = (ThreadPoolExecutor) executorService;
                    log.info("[{}] PoolSize={}, Active={}, QueueSize={}, Completed={}", name, tpe.getPoolSize(), tpe.getActiveCount(), tpe.getQueue().size(), tpe.getCompletedTaskCount());
                }
            }, 0, 30, TimeUnit.SECONDS);
        }
        public void stop() {
            scheduler.shutdownNow();
        }
    }
}
