package io.github.wiqer.bug.ability.impl;

import io.github.wiqer.bug.ability.CacheManger;
import io.github.wiqer.bug.cache.TestCache;
import io.github.wiqer.bug.level.TrunkLevel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ：TrunkLevelTestAbility
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  15:17
 * @description：默认
 * @modified By：llf.lilanfeng
 */
@Service
public class TrunkLevelTestA1Ability extends TrunkLevel implements CacheManger {

    @Resource
    @Qualifier("aCache")
    private TestCache aCache;
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
        return aCache;
    }
}
