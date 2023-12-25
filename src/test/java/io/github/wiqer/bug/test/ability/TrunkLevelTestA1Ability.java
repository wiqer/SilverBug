package io.github.wiqer.bug.test.ability;

import io.github.wiqer.bug.level.TrunkLevel;

/**
 * ：TrunkLevelTestAbility
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  15:17
 * @description：
 * @modified By：
 */
public class TrunkLevelTestA1Ability extends TrunkLevel {
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
}
