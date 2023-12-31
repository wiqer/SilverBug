package io.github.wiqer.bug.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * ：Assert
 *
 * @author ：李岚峰、lilanfeng、
 * date ：Created in 25 / 2023/12/25  13:50
 * description：默认
 * modified By：llf.lilanfeng
 */
public class Assert {
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void notEmpty(Object[] array, String message) {
        if (Objects.isNull(array)) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }
    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return messageSupplier != null ? (String)messageSupplier.get() : null;
    }
    public  static int checkNonnegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        } else {
            return value;
        }
    }

}
