package io.github.wiqer.bug.ability.impl.branch;

import io.github.wiqer.bug.ability.impl.TrunkLevelTestA1Ability;
import io.github.wiqer.bug.cache.TestCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ：TrunkLevelTestAbility
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  15:17
 * @description：
 * @modified By：
 */
@Service
public class TrunkLevelTestA1A2Ability extends TrunkLevelTestA1Ability {

    @Resource
    @Qualifier("a2Cache")
    private TestCache a2Cache;
    @Override
    public <T> boolean match(T req) {
        return true;
    }

    @Override
    public int priority() {
        return super.priority();
    }

    @Override
    public String actionScope() {
        return "test-a";
    }

    public TestCache getCache(){
        return a2Cache;
    }
}
