package io.github.wiqer.bug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ：StartUpApplication
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  17:46
 * description：默认
 * modified By：llf.lilanfeng
 */
@SpringBootApplication(scanBasePackages = "io.github.wiqer.bug")
public class StartUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartUpApplication.class, args);
    }
}
