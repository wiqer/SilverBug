package io.github.wiqer.bug.ability.impl;

import io.github.wiqer.bug.level.TrunkLevel;

/**
 * ：TrunkLevelTestAbility
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  15:17
 * description：默认
 * modified By：llf.lilanfeng
 */
public class TrunkLevelTestA3Ability extends TrunkLevel {
    @Override
    public <T> boolean match(T req) {
        return false;
    }

    @Override
    public int priority() {
        return super.priority();
    }

    @Override
    public String actionScope() {
        return "test-a";
    }
}
