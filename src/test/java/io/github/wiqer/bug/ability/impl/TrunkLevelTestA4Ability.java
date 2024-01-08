package io.github.wiqer.bug.ability.impl;

import io.github.wiqer.bug.level.TrunkLevel;
import org.springframework.stereotype.Service;

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
public class TrunkLevelTestA4Ability extends TrunkLevel {
    @Override
    public <T> boolean match(T req) {
        return false;
    }

    @Override
    public int priority() {
        return -99;
    }

    @Override
    public String actionScope() {
        return "test-a";
    }
}
