package com.aixuexi.io.github.wiqer.bug.level;

/**
 * 负责贫血模型能力承接
 */
public interface BugAbility {

    /**
     * 能力匹配接口
     * @return
     */
    boolean match();

    LevelTypeEnum level();

    /**
     * 优先级，越小，优先级越高
     * @return
     */
    int priority();
}
