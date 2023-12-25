package io.github.wiqer.bug.manage;

import io.github.wiqer.bug.level.BugAbility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ：BugContainer
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:39
 * @description：
 * @modified By：
 */
public class BugInstanceContainer {

    final static Map<String, BugAbility> BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP = new ConcurrentHashMap<>(1024);

    static synchronized void add(BugAbility ability,InstanceSourceEnum sourceEnum){
        String key = ability.getAbilityKey();
        if(InstanceSourceEnum.SPRING.equals(sourceEnum)) {
            BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.put(key, ability);
        }else {
            BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.putIfAbsent(key, ability);
        }
    }



    static  Map<String, BugAbility> getBugAbilityOfScopeAndSceneMap(){
        return BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP;
    }
    static BugAbility get(BugAbility ability){
        String key = ability.getAbilityKey();
        return BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.get(key);
    }
}
