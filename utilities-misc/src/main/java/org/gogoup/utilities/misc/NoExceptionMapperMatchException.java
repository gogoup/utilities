package org.gogoup.utilities.misc;

public class NoExceptionMapperMatchException extends RuntimeException {

    private Throwable throwable;

    public NoExceptionMapperMatchException(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}

