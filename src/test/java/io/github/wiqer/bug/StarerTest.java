package io.github.wiqer.bug;

import io.github.wiqer.bug.start.BugStarter;
import org.junit.Test;

import java.util.Collections;

/**
 * ：StarerTest
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  15:15
 * description：默认
 * modified By：llf.lilanfeng
 */
public class StarerTest {

    @Test
    public void testForClass() {
        BugStarter.run(StarerTest.class);
    }

    @Test
    public void testForName() {
        BugStarter.run(Collections.singleton("io.github.wiqer.bug.test.ability"));
    }
}
