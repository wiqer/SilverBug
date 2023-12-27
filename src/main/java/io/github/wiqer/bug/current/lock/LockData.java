package io.github.wiqer.bug.current.lock;

import lombok.Data;

/**
 * ：LockService
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 25 / 2023/12/25  11:07
 * @description：
 * @modified By：
 */
@Data
public class LockData<T> {

    public T data;

    public String lockId;
}
