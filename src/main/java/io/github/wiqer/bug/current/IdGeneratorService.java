package io.github.wiqer.bug.current;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ：IdGeneratorService
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:07
 * @description：
 * @modified By：
 */
@Service
public class IdGeneratorService {


    private static final int timeMoveBit = 20;
    private static final int rankMoveBit = 12;

    /**
     *  * @date ：Created in 13 / 2023/11/13
     *  千万别动，动了就把业务干废了
     */
    private static final long time = 1699869436004L;


    public static Long getSequentiallyRandom(){
        long random = ThreadLocalRandom.current().nextInt()&0x3ffff;
        return ((System.currentTimeMillis() - time) << timeMoveBit) | random;
    }

    public static Long getSequentiallyRandom(Integer rank){
        int ranking = (rank & 0x3f) << rankMoveBit;
        int random = ThreadLocalRandom.current().nextInt()&0xfff;
        long minTime = ((System.currentTimeMillis() - time) << timeMoveBit);
        return minTime | ranking | random;
    }
}
