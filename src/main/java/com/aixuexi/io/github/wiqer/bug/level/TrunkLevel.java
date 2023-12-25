package com.aixuexi.io.github.wiqer.bug.level;

/**
 * ：DDDTrunk
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:06
 * @description：
 * @modified By：
 */
public abstract class TrunkLevel implements BugAbility {
    public LevelTypeEnum level(){
        return LevelTypeEnum.TRUNK;
    }
}
