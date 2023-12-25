package io.github.wiqer.bug.start;

import io.github.wiqer.bug.utils.Assert;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.Set;

/**
 * ：BugStarter
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:38
 * @description：
 * @modified By：
 */
public class BugStarter {

    public static void run(Set<String> packageNameList) {
        Assert.notNull(packageNameList, "packageNameList must not be null");
        Assert.notEmpty(packageNameList, "packageNameList must not be null");

    }
    public static void  run(Class<?> tClass){
        String pageName= tClass.getName().substring(0,tClass.getName().lastIndexOf("."));
        run(Collections.singleton(pageName));
    }
}
