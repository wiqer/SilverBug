package io.github.wiqer.bug.manage;

import io.github.wiqer.bug.level.BugAbility;
import io.github.wiqer.bug.utils.Assert;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    final static Map<String, List<BugAbility>> BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP = new ConcurrentHashMap<>(1024);

    final static Map<Class<? extends BugAbility>, BugAbility> BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP = new ConcurrentHashMap<>(1024);

    final static Map<Class<? extends BugAbility>, List<BugAbility>> BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP
            = new ConcurrentHashMap<>(1024);

    final static Map<Class<? extends BugAbility>, List<BugAbility>> BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP
            = new ConcurrentHashMap<>(1024);
    static synchronized void add(BugAbility ability,InstanceSourceEnum sourceEnum){
        String key = ability.getAbilityKey();
        if(InstanceSourceEnum.SPRING.equals(sourceEnum)) {
            List<BugAbility> bugAbilityList = BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.getOrDefault(key, new ArrayList<>());
            bugAbilityList.removeIf(a -> a.getClass().isAssignableFrom(ability.getClass()));
            bugAbilityList.add(ability);
            BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.put(key,bugAbilityList);
        }else {
            List<BugAbility> bugAbilityList = BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.getOrDefault(key, new ArrayList<>());
            if(bugAbilityList.stream().anyMatch(a -> a.getClass().isAssignableFrom(ability.getClass()))){
                return;
            }
            bugAbilityList.add(ability);
            BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.put(key,bugAbilityList);
        }
        BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.put(ability.getClass(),ability);
    }



    static  Map<String, List<BugAbility>> getBugAbilityOfScopeAndSceneMap(){
        return BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP;
    }
    static BugAbility getFirst(BugAbility ability){
        String key = ability.getAbilityKey();
        return BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.get(key).stream().findFirst().orElse(null);
    }

    /**
     * 获取继承当前类下的所有能力实现
     * @param bugAbilityType
     * @return
     */
    private static List<? extends BugAbility> getSubAbilityByClass(Class<? extends BugAbility> bugAbilityType){
        List<BugAbility> bugAbilityList = BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.get(bugAbilityType);
        if(bugAbilityType != null){
            if(CollectionUtils.isEmpty(bugAbilityList)){
                return Collections.emptyList();
            }
            return bugAbilityList;
        }
        synchronized (BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP){
            BugAbility temp = getAbilityTempByClass(bugAbilityType);
            if(temp == null){
                return Collections.emptyList();
            }
            bugAbilityList = BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.get(temp.getAbilityKey());
            bugAbilityList = bugAbilityList.stream().filter(a -> temp.getClass().isAssignableFrom(a.getClass())).collect(Collectors.toList());
            synchronized (BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP){
                BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.put(bugAbilityType, bugAbilityList);
            }
        }
        bugAbilityList = bugAbilityList.stream().sorted(Comparator.comparing(BugAbility::priority)).collect(Collectors.toList());
        return bugAbilityList;
    }
    /**
     * 获取继承当前类下的所有能力实现
     * @param bugAbilityType
     * @return
     */
    private  static <T> List<? extends BugAbility> getSubAbilityByClass(Class<? extends BugAbility> bugAbilityType,T abilityReq){
        List<? extends BugAbility> bugAbilityList =  getSubAbilityByClass(bugAbilityType);
        if(CollectionUtils.isEmpty(bugAbilityList)){
            return Collections.emptyList();
        }
        return bugAbilityList.stream().filter(a -> a.match(abilityReq)).collect(Collectors.toList());
    }
    /**
     * 获取当前类和继承当前类下的所有能力实现，使用唯一能力kay匹配
     * @param bugAbilityType
     * @return
     */
    private static List<? extends BugAbility> getAbilityByClass(Class<? extends BugAbility> bugAbilityType){
        List<BugAbility> bugAbilityList = BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.get(bugAbilityType);
        if(bugAbilityType != null){
            if(CollectionUtils.isEmpty(bugAbilityList)){
                return Collections.emptyList();
            }
            return new LinkedList<>(bugAbilityList);
        }
        synchronized (BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP){
            BugAbility temp = getAbilityTempByClass(bugAbilityType);
            if(temp == null){
                return Collections.emptyList();
            }
            bugAbilityList = BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.get(temp.getAbilityKey());
            bugAbilityList = bugAbilityList.stream().filter(a -> temp.getClass().isAssignableFrom(a.getClass())).collect(Collectors.toList());
            synchronized (BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP){
                BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.put(bugAbilityType, bugAbilityList);
            }
        }
        bugAbilityList = bugAbilityList.stream().sorted(Comparator.comparing(BugAbility::priority)).collect(Collectors.toList());
        return new LinkedList<>(bugAbilityList);
    }
    /**
     * 获取当前类和继承当前类下的所有能力实现，使用唯一能力kay匹配
     * @param bugAbilityType
     * @return
     */
    private  static <T> List<? extends BugAbility> getAbilityByClass(Class<? extends BugAbility> bugAbilityType,T abilityReq){
        List<? extends BugAbility> bugAbilityList =  getAbilityByClass(bugAbilityType);
        if(CollectionUtils.isEmpty(bugAbilityList)){
            return Collections.emptyList();
        }
        return bugAbilityList.stream().filter(a -> a.match(abilityReq)).collect(Collectors.toList());
    }

    private static BugAbility getAbilityTempByClass(Class<? extends BugAbility> bugAbilityType) {
        BugAbility temp = BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.get(bugAbilityType);
        if(temp == null){
            try {
                if (bugAbilityType.isInterface()) {
                    throw new IllegalArgumentException("BugAbility clazz , bugAbilityType must be not interface");
                }
                temp = bugAbilityType.newInstance();
                Assert.notNull(temp, "can't newInstance by" + bugAbilityType);
                BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.put(bugAbilityType,temp);
                return temp;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
