package io.github.wiqer.bug.cache;

import io.github.wiqer.bug.StartUpApplication;
import io.github.wiqer.bug.ability.impl.TrunkLevelTestA1Ability;
import io.github.wiqer.bug.manage.BugInstanceContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ：SpringBootStarerTest
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  16:43
 * @description：默认
 * @modified By：llf.lilanfeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringBootServiceTest.class, StartUpApplication.class})
public class SpringBootServiceTest {

    @Test
    public void testForService() {
        List<TrunkLevelTestA1Ability> list = BugInstanceContainer.getAbilityByClass(TrunkLevelTestA1Ability.class,null);

        for (TrunkLevelTestA1Ability ability :  list){
            System.out.println(ability.getCache().getClass());
        }
    }
}
