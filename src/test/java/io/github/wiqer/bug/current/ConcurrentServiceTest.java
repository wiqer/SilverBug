package io.github.wiqer.bug.current;

import io.github.wiqer.bug.StarerTest;
import io.github.wiqer.bug.start.BugStarter;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ：ConcurrentServiceTest
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 08 / 2024/1/8  13:08
 * description：默认
 * modified By：llf.lilanfeng
 */
public class ConcurrentServiceTest {

    @Test
    public void testForClass() {
        ConcurrentService concurrentService
                = new ConcurrentService(Executors.newFixedThreadPool(18),"ConcurrentServiceTest");
        Callable <String> callable1 = () -> {
            // 这里是你的任务1
            return "Result of Task1";
        };
        Callable <String> callable2 = () -> {
            // 这里是你的任务2
            return "Result of Task2";
        };
        Callable <Integer> callable3 = () -> {
            // 这里是你的任务3
            return 3;
        };
        Map<Callable, Object> resultMap = concurrentService.fetchCallableReturnResultMap(callable1,  callable2, callable3);
        String result1 = concurrentService.getResultFromMap(resultMap, callable1);
        System.out.println("callable1 result is -->" + result1);
        String result2 = concurrentService.getResultFromMap(resultMap, callable2);
        System.out.println("callable2 result is -->" + result2);
        Integer result3 = concurrentService.getResultFromMap(resultMap, callable3);
        System.out.println("callable3 result is -->" + result3);

    }
}
