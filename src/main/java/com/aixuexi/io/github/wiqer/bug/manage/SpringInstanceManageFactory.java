package com.aixuexi.io.github.wiqer.bug.manage;

import com.aixuexi.io.github.wiqer.bug.level.BugAbility;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ：TestEntity
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  12:36
 * @description：
 * @modified By：
 */

@Component
public class SpringInstanceManageFactory implements ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BugAbility> beansOfTypeMap = applicationContext.getBeansOfType(BugAbility.class);
        beansOfTypeMap.values().forEach(a -> BugInstanceContainer.add(a, InstanceSourceEnum.SPRING));
    }
}

