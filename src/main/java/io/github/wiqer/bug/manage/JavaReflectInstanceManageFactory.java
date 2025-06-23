package io.github.wiqer.bug.manage;

import io.github.wiqer.bug.level.BugAbility;
import io.github.wiqer.bug.utils.ClassUtils;
import org.apache.commons.collections4.CollectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ：JavaReflectInstanceManageFactory
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  13:13
 * description：默认
 * modified By：llf.lilanfeng
 */
public class JavaReflectInstanceManageFactory {

    public static void scanAllClassesOnPackageNames(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return;
        }
        try {
            Set<Class<?>> classSet = ClassUtils.getClassSet(packageName);
            if (CollectionUtils.isEmpty(classSet)) {
                return;
            }
            classSet = ClassUtils.getClassSetBySuper(BugAbility.class, classSet);
            if (CollectionUtils.isEmpty(classSet)) {
                return;
            }
            List<BugAbility> instances = new ArrayList<>();
            for (Class<?> clz : classSet) {
                try {
                    if (validateClass(clz)) {
                        BugAbility instance = createInstance(clz);
                        instances.add(instance);
                    }
                } catch (Exception e) {
                    // ignore single instance error
                }
            }
            if (!instances.isEmpty()) {
                BugInstanceContainer.addBatch(instances, InstanceSourceEnum.JAVA);
            }
        } catch (Exception e) {
            // ignore scan error
        }
    }

    private static boolean validateClass(Class<?> clz) {
        if (clz.isInterface()) {
            return false;
        }
        if (Modifier.isAbstract(clz.getModifiers())) {
            return false;
        }
        try {
            clz.getDeclaredConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static BugAbility createInstance(Class<?> clz) throws Exception {
        try {
            return (BugAbility) clz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("无法创建 " + clz.getName() + " 的实例", e);
        }
    }
}
