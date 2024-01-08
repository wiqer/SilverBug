package io.github.wiqer.bug.level;

/**
 * ：Leaf
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:08
 * @description：默认
 * @modified By：llf.lilanfeng
 */
public abstract class LeafLevel implements BugAbility {
    public LevelTypeEnum level(){
        return LevelTypeEnum.LEAF;
    }
}
