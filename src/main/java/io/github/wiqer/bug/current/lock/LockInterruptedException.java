package io.github.wiqer.bug.current.lock;

public class LockInterruptedException extends RuntimeException {
    public LockInterruptedException(String message) { super(message); }
    public LockInterruptedException(String message, Throwable cause) { super(message, cause); }
} 