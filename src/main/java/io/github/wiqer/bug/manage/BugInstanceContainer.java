package io.github.wiqer.bug.manage;

import io.github.wiqer.bug.level.BugAbility;
import io.github.wiqer.bug.utils.Assert;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ：BugContainer
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  11:39
 * description：默认
 * modified By：llf.lilanfeng
 */
public class BugInstanceContainer<K extends BugAbility> {

    final static Map<String, List<? extends BugAbility>> BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP = new ConcurrentHashMap<>(1024);

    final static Map<Class<? extends BugAbility>, BugAbility> BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP = new ConcurrentHashMap<>(1024);

    final static Map<Class<? extends BugAbility>, List<? extends BugAbility>> BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP
            = new ConcurrentHashMap<>(1024);

    final static Map<Class<? extends BugAbility>, List<? extends BugAbility>> BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP
            = new ConcurrentHashMap<>(1024);
    static synchronized <T extends BugAbility> void add(T ability,InstanceSourceEnum sourceEnum){
        String key = ability.getAbilityKey();
        if(InstanceSourceEnum.SPRING.equals(sourceEnum)) {
            List<T> bugAbilityList = (List<T>) BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.getOrDefault(key, new ArrayList<>());
            bugAbilityList.add(ability);
            BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.put(key,bugAbilityList);
        }else {
            List<T> bugAbilityList = (List<T>) BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.getOrDefault(key, new ArrayList<>());
            if(bugAbilityList.stream().anyMatch(a -> a.getClass().isAssignableFrom(ability.getClass()))){
                return;
            }
            bugAbilityList.add(ability);
            BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.put(key,bugAbilityList);
        }
        BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.put(ability.getClass(),ability);
    }



    static  Map<String, List<? extends BugAbility>> getBugAbilityOfScopeAndSceneMap(){
        return BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP;
    }
    public static BugAbility getFirst(Class<? extends BugAbility> bugAbilityType){
        if (bugAbilityType.isInterface() || Modifier.isAbstract(bugAbilityType.getModifiers())) {
            throw new IllegalArgumentException("传入的类型不能是接口或抽象类");
        }
        return getAbilityByClass(bugAbilityType).stream().findFirst().orElse(null);
    }

    public static  <T> BugAbility getFirst(Class<? extends BugAbility> bugAbilityType,T abilityReq){
        if (bugAbilityType.isInterface() || Modifier.isAbstract(bugAbilityType.getModifiers())) {
            throw new IllegalArgumentException("传入的类型不能是接口或抽象类");
        }
        return getAbilityByClass(bugAbilityType,abilityReq).stream().findFirst().orElse(null);
    }

    /**
     * 获取继承当前类下的所有能力实现
     * @param bugAbilityType
     * @return
     */
    private static<K extends BugAbility> List<K>  getSubAbilityByClass(Class<K> bugAbilityType){
        List<K> bugAbilityList = (List<K>) BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.get(bugAbilityType);
        if(bugAbilityList != null){
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
            bugAbilityList = (List<K>) BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.get(temp.getAbilityKey());
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
    public  static <T> List<? extends BugAbility> getSubAbilityByClass(Class<? extends BugAbility> bugAbilityType,T abilityReq){
        if (bugAbilityType.isInterface() || Modifier.isAbstract(bugAbilityType.getModifiers())) {
            throw new IllegalArgumentException("传入的类型不能是接口或抽象类");
        }
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
    private static <K extends BugAbility> List<K> getAbilityByClass(Class<K> bugAbilityType){
        List<K> bugAbilityList = (List<K>) BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.get(bugAbilityType);
        if(bugAbilityList != null){
            if(CollectionUtils.isEmpty(bugAbilityList)){
                return Collections.emptyList();
            }
            return bugAbilityList;
        }
        synchronized (BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP){
            BugAbility temp = getAbilityTempByClass(bugAbilityType);
            if(temp == null){
                return Collections.emptyList();
            }
            bugAbilityList = (List<K>) BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.get(temp.getAbilityKey());
            bugAbilityList = bugAbilityList.stream().filter(a -> temp.getClass().isAssignableFrom(a.getClass()) || temp.getClass().equals(a.getClass())).collect(Collectors.toList());
            synchronized (BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP){
                BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.put(bugAbilityType, bugAbilityList);
            }
        }
        bugAbilityList = bugAbilityList.stream().sorted(Comparator.comparing(BugAbility::priority)).collect(Collectors.toList());
        return bugAbilityList;
    }
    /**
     * 获取当前类和继承当前类下的所有能力实现，使用唯一能力kay匹配
     * @param bugAbilityType
     * @return
     */
    public  static <T,K extends BugAbility> List<K> getAbilityByClass(Class<K> bugAbilityType,T abilityReq){
        if (bugAbilityType.isInterface() || Modifier.isAbstract(bugAbilityType.getModifiers())) {
            throw new IllegalArgumentException("传入的类型不能是接口或抽象类");
        }
        List<K> bugAbilityList =  getAbilityByClass(bugAbilityType);
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
        return temp;
    }
}
