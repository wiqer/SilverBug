package io.github.wiqer.bug.current.lock;

public class LockTimeoutException extends RuntimeException {
    public LockTimeoutException(String message) { super(message); }
    public LockTimeoutException(String message, Throwable cause) { super(message, cause); }
} 