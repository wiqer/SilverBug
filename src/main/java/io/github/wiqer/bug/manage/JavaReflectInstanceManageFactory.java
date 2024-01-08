package io.github.wiqer.bug.manage;

import io.github.wiqer.bug.level.BugAbility;
import io.github.wiqer.bug.utils.ClassUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Modifier;
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

    public static void scanAllClassesOnPackageNames(String packageName){
        Set<Class<?>>  classSet = ClassUtils.getClassSet(packageName);
        if(CollectionUtils.isEmpty(classSet)){
            return;
        }
        classSet = ClassUtils.getClassSetBySuper(BugAbility.class, classSet);
        if(CollectionUtils.isEmpty(classSet)){
            return;
        }
        for(Class<?> clz : classSet){
            try {
                if(clz.isInterface() || Modifier.isAbstract(clz.getModifiers())){
                    return;
                }
                try {
                    clz.getConstructor();
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(clz.getName()+" cannot be instantiated!", e);
                }
                BugAbility per = (BugAbility) clz.newInstance();
                if(per instanceof BugAbility){
                    BugInstanceContainer.add(per, InstanceSourceEnum.JAVA);
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
