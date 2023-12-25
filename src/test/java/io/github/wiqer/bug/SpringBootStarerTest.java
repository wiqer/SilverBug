package io.github.wiqer.bug;

import io.github.wiqer.bug.manage.SpringInstanceManageFactory;
import io.github.wiqer.bug.start.BugStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ：SpringBootStarerTest
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  16:43
 * @description：
 * @modified By：
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringBootStarerTest.class, StartUpApplication.class})
public class SpringBootStarerTest {


    @SpringBootApplication(scanBasePackages = "io.github.wiqer.bug")
    static class TestConfiguration {
    }
    @Test
    public void testForClass() {
        BugStarter.run(SpringBootStarerTest.class);
    }
}
