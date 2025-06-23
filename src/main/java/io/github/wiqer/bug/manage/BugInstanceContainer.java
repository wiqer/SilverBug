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
    private static final int INITIAL_CAPACITY = 1024;
    private static final Map<String, List<? extends BugAbility>> BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private static final Map<Class<? extends BugAbility>, BugAbility> BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private static final Map<Class<? extends BugAbility>, List<? extends BugAbility>> BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private static final Map<Class<? extends BugAbility>, List<? extends BugAbility>> BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP = new ConcurrentHashMap<>(INITIAL_CAPACITY);

    static synchronized <T extends BugAbility> void add(T ability, InstanceSourceEnum sourceEnum) {
        Assert.notNull(ability, "Ability instance cannot be null");
        String key = ability.getAbilityKey();
        @SuppressWarnings("unchecked")
        List<T> abilityList = (List<T>) BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.computeIfAbsent(key, k -> new ArrayList<>());
        if (InstanceSourceEnum.SPRING.equals(sourceEnum)) {
            abilityList.add(ability);
        } else {
            if (abilityList.stream().noneMatch(a -> a.getClass().isAssignableFrom(ability.getClass()))) {
                abilityList.add(ability);
            }
        }
        BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.forEach((k, list) ->
                list.sort(Comparator.comparingInt(BugAbility::priority))
        );
        BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.clear();
        BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.clear();
        BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.put(key, abilityList);
        BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.put(ability.getClass(), ability);
        refreshAbilityListOfClassTypeMap();
    }

    static synchronized <T extends BugAbility> void addBatch(List<T> abilities, InstanceSourceEnum sourceEnum) {
        if (CollectionUtils.isEmpty(abilities)) {
            return;
        }
        for (T ability : abilities) {
            Assert.notNull(ability, "Ability instance cannot be null");
            String key = ability.getAbilityKey();
            @SuppressWarnings("unchecked")
            List<T> abilityList = (List<T>) BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.computeIfAbsent(key, k -> new ArrayList<>());
            if (InstanceSourceEnum.SPRING.equals(sourceEnum)) {
                abilityList.add(ability);
            } else {
                if (abilityList.stream().noneMatch(a -> a.getClass().isAssignableFrom(ability.getClass()))) {
                    abilityList.add(ability);
                }
            }
            BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.put(ability.getClass(), ability);
        }
        BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP.forEach((k, list) ->
                list.sort(Comparator.comparingInt(BugAbility::priority))
        );
        BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.clear();
        BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.clear();
        refreshAbilityListOfClassTypeMap();
    }

    private static synchronized void refreshAbilityListOfClassTypeMap() {
        BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.clear();
        BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.clear();
        List<BugAbility> allAbilities = new ArrayList<>(BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.values());
        Set<Class<?>> allAbilityTypes = new HashSet<>();
        for (BugAbility ability : allAbilities) {
            Set<Class<?>> parentTypes = getAllParentTypes(ability.getClass());
            allAbilityTypes.addAll(parentTypes);
        }
        for (Class<?> type : allAbilityTypes) {
            if (BugAbility.class.isAssignableFrom(type)) {
                @SuppressWarnings("unchecked")
                Class<? extends BugAbility> key = (Class<? extends BugAbility>) type;
                List<BugAbility> list = new ArrayList<>();
                for (BugAbility ability : allAbilities) {
                    if (type.isAssignableFrom(ability.getClass())) {
                        list.add(ability);
                    }
                }
                list.sort(Comparator.comparingInt(BugAbility::priority));
                BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.put(key, list);
            }
        }
        for (Class<?> type : allAbilityTypes) {
            if (BugAbility.class.isAssignableFrom(type)) {
                @SuppressWarnings("unchecked")
                Class<? extends BugAbility> key = (Class<? extends BugAbility>) type;
                List<BugAbility> subList = new ArrayList<>();
                for (BugAbility ability : allAbilities) {
                    if (type != ability.getClass() && type.isAssignableFrom(ability.getClass())) {
                        subList.add(ability);
                    }
                }
                subList.sort(Comparator.comparingInt(BugAbility::priority));
                BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.put(key, subList);
            }
        }
    }

    private static Set<Class<?>> getAllParentTypes(Class<?> clazz) {
        Set<Class<?>> result = new HashSet<>();
        if (clazz == null || clazz == Object.class) {
            return result;
        }
        result.add(clazz);
        for (Class<?> iface : clazz.getInterfaces()) {
            result.addAll(getAllParentTypes(iface));
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            result.addAll(getAllParentTypes(superClass));
        }
        return result;
    }

    static Map<String, List<? extends BugAbility>> getBugAbilityOfScopeAndSceneMap() {
        return Collections.unmodifiableMap(BUG_ABILITY_OF_SCOPE_AND_SCENE_MAP);
    }

    public static BugAbility getFirst(Class<? extends BugAbility> abilityType) {
        return getAbilityByClass(abilityType).stream().findFirst().orElse(null);
    }

    public static <T> BugAbility getFirst(Class<? extends BugAbility> abilityType, T abilityReq) {
        return getAbilityByClass(abilityType, abilityReq).stream().findFirst().orElse(null);
    }

    private static <K extends BugAbility> List<K> getSubAbilityByClass(Class<K> abilityType) {
        @SuppressWarnings("unchecked")
        List<K> abilityList = (List<K>) BUG_SUB_ABILITY_LIST_OF_CLASS_TYPE_MAP.get(abilityType);
        if (abilityList != null) {
            return CollectionUtils.isEmpty(abilityList) ? Collections.emptyList() : abilityList;
        }
        return Collections.emptyList();
    }

    public static <T, K extends BugAbility> List<K> getSubAbilityByClass(Class<K> abilityType, T abilityReq) {
        List<K> abilityList = getSubAbilityByClass(abilityType);
        return CollectionUtils.isEmpty(abilityList) ? Collections.emptyList() :
                abilityList.stream().filter(a -> a.match(abilityReq)).collect(Collectors.toList());
    }

    private static <K extends BugAbility> List<K> getAbilityByClass(Class<K> abilityType) {
        @SuppressWarnings("unchecked")
        List<K> abilityList = (List<K>) BUG_ABILITY_LIST_OF_CLASS_TYPE_MAP.get(abilityType);
        if (abilityList != null) {
            return CollectionUtils.isEmpty(abilityList) ? Collections.emptyList() : abilityList;
        }
        return Collections.emptyList();
    }

    public static <T, K extends BugAbility> List<K> getAbilityByClass(Class<K> abilityType, T abilityReq) {
        List<K> abilityList = getAbilityByClass(abilityType);
        return CollectionUtils.isEmpty(abilityList) ? Collections.emptyList() :
                abilityList.stream().filter(a -> a.match(abilityReq)).collect(Collectors.toList());
    }

    private static BugAbility getAbilityTempByClass(Class<? extends BugAbility> abilityType) {
        BugAbility temp = BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.get(abilityType);
        if (temp == null) {
            try {
                if (abilityType.isInterface()) {
                    throw new IllegalArgumentException("BugAbility clazz , bugAbilityType must be not interface");
                }
                temp = abilityType.newInstance();
                Assert.notNull(temp, "can't newInstance by" + abilityType);
                BUG_ABILITY_TEMP_OF_CLASS_TYPE_MAP.put(abilityType, temp);
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
