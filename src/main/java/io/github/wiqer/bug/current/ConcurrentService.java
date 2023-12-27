package io.github.wiqer.bug.current;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ：ConcurrentService
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:07
 * @description：
 * @modified By：
 */
@Slf4j
public class ConcurrentService {
    private final ExecutorService executorService;

    private final String name;

    public ConcurrentService(ExecutorService executorService,String name) {
        this.executorService = executorService;
        this.name = name;
    }

    public <T,R> List<R> fetchConcurrentList(Function<T,R> readFunction,List<T> list) {
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Future<R>> futures = new ArrayList<>();
        for (T param : list) {
            Future<R> future = executorService.submit(() -> readFunction.apply(param));
            futures.add(future);
        }

        List<R> resultList = new ArrayList<>();
        for (Future<R> future : futures) {
            try {
                R result = future.get();
                if(result != null) {
                    resultList.add(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(name, e);
            }
        }
        if(CollectionUtils.isEmpty(resultList)){
            return Collections.emptyList();
        }
        resultList = new ArrayList<>(resultList);
        return resultList;
    }

    public <T,R> List<R> fetchConcurrentList(Function<List<T>,List<R>> readFunction,List<T> list,int batchSize) {
        if(batchSize < 1 || CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Future<List<R>>> futures = new ArrayList<>();
        for (List<T> param : Lists.partition(list, batchSize)) {
            Future<List<R>> future = executorService.submit(() -> readFunction.apply(param));
            futures.add(future);
        }

        List<R> resultList = new ArrayList<>();
        for (Future<List<R>> future : futures) {
            try {
                List<R> result = future.get();
                if(CollectionUtils.isNotEmpty(result)){
                    resultList.addAll(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(name, e);
            }
        }
        if(CollectionUtils.isEmpty(resultList)){
            return Collections.emptyList();
        }
        return new ArrayList<>(resultList);
    }

    public <T,R,U> List<R> fetchConcurrentList(BiFunction<List<T>,U,List<R>> readBiFunction, List<T> list,U conditionParam, int batchSize) {
        if(batchSize < 1 || CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<Future<List<R>>> futures = new ArrayList<>(list.size()/batchSize);
        for (List<T> param : Lists.partition(list, batchSize)) {
            Future<List<R>> future = executorService.submit(() -> readBiFunction.apply(param, conditionParam));
            futures.add(future);
        }

        List<R> resultList = new ArrayList<>(list.size());
        for (Future<List<R>> future : futures) {
            try {
                List<R> result = future.get();
                if(CollectionUtils.isNotEmpty(result)){
                    resultList.addAll(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(name, e);
            }
        }
        if(CollectionUtils.isEmpty(resultList)){
            return Collections.emptyList();
        }
        return new ArrayList<>(resultList);
    }

    public <T,R,U> Map<String,R> fetchConcurrentStringKeyMap(BiFunction<List<T>,U,Map<String,R>> readBiFunction, List<T> list, U conditionParam, int batchSize) {
        if(batchSize < 1 || CollectionUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        list = list.stream().distinct().collect(Collectors.toList());
        List<Future<Map<String,R>>> futures = new ArrayList<>(list.size()/batchSize + 1);
        for (List<T> param : Lists.partition(list, batchSize)) {
            Future<Map<String,R>> future = executorService.submit(() -> readBiFunction.apply(param, conditionParam));
            futures.add(future);
        }

        Map<String,R> resultList = new HashMap<>(list.size());
        for (Future<Map<String,R>> future : futures) {
            try {
                Map<String,R> result = future.get();
                if(MapUtils.isNotEmpty(result)){
                    resultList.putAll(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(name, e);
            }
        }
        if(MapUtils.isEmpty(resultList)){
            return Collections.emptyMap();
        }
        return resultList;
    }
}
