package io.github.wiqer.bug.level;

/**
 * ：Bug
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:08
 * @description：
 * @modified By：
 */
public interface BugLevel extends BugAbility{
    default LevelTypeEnum level(){
        return LevelTypeEnum.BUG;
    }
}
