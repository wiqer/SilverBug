package com.aixuexi.io.github.wiqer.bug.level;

/**
 * ：Branch
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:07
 * @description：
 * @modified By：
 */
public abstract class BranchLevel implements BugAbility{
    public LevelTypeEnum level(){
        return LevelTypeEnum.BRANCH;
    }
}
