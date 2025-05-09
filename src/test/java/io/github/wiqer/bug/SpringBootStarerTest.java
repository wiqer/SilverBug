package io.github.wiqer.bug;

import io.github.wiqer.bug.start.BugStarter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ：SpringBootStarerTest
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  16:43
 * description：默认
 * modified By：llf.lilanfeng
 */
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
