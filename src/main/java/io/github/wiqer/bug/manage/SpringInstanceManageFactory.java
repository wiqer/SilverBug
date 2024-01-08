package io.github.wiqer.bug.manage;

import io.github.wiqer.bug.level.BugAbility;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ：TestEntity
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  12:36
 * description：默认
 * modified By：llf.lilanfeng
 */

@Component
public class SpringInstanceManageFactory implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BugAbility> beansOfTypeMap = applicationContext.getBeansOfType(BugAbility.class);
        beansOfTypeMap.values().forEach(a -> BugInstanceContainer.add(a, InstanceSourceEnum.SPRING));
    }
}

