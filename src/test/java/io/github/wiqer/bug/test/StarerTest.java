package io.github.wiqer.bug.test;

/**
 * ：StarerTest
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  15:15
 * @description：
 * @modified By：
 */
public class StarerTest {

    @Test
    public void test()
    {
        Expression lexer = Expression.parse("home.person.getAge()");
        assertEquals(person.age, ((Number) lexer.calculate(vars)).intValue());
    }
}
