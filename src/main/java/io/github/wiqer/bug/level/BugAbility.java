package io.github.wiqer.bug.level;

/**
 * 负责贫血模型能力承接
 */
public interface BugAbility {

    /**
     * 能力匹配接口
     * @return
     */
    <T> boolean match(T req);

    LevelTypeEnum level();

    /**
     * 优先级，越小，优先级越高
     * @return
     */
    default int priority(){
        return Integer.MAX_VALUE;
    }

    /**
     * 能力作用域
     * 作用域尽量不要使用文字以外符号
     * @return
     */
    String actionScope();

    /**
     * 能力作用场景
     * @return
     */
    default String actionScene() {
        return "Almighty";
    }

    default String getAbilityKey() {
        String  key = "Scope:"+ actionScope() + "->Scene:" + actionScene();
        return key;
    }
}
