package org.gogoup.utilities.play.commons;

import org.gogoup.utilities.misc.NoSuchEntityFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruisun on 2016-05-19.
 */
public class ErrorResponse {

    public static final int DEFAULT_ERROR_CODE = -1;
    public static final int NO_SUCH_ENTITY_ERROR_CODE = 400000;
    public static final int AUTHENTICATION_FAILED_ERROR_CODE = 400401;

    private int code;
    private Map<String, Object> details;
    private String message;

    public ErrorResponse(int code, String message) {
        this(code, getEmptyDetails(), message);
    }

    public ErrorResponse(int code, Map<String, Object> details, String message) {
        this.code = code;
        this.details = Collections.unmodifiableMap(details);
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public Map<String, Object> getDetails() {
        return details;
    }
    public String getMessage() {
        return message;
    }

    private static Map<String, Object> getEmptyDetails() {
        Map<String, Object> details;
        details = new HashMap<>();
        return Collections.unmodifiableMap(details);
    }

    public static Throwable getThrowable(Class<?> clazz, Throwable exception) {
        Throwable next = exception;
        while (null != next) {
            if (clazz.equals(next.getClass())) {
                return next;
            }
            next = next.getCause();
        }
        return null;
    }

    public static ErrorResponse toErrorResponse(Throwable exception) {
        Throwable throwable;
        throwable = getThrowable(NoSuchEntityFoundException.class, exception);
        if (null != throwable) {
            return new ErrorResponse(
                    ErrorResponse.NO_SUCH_ENTITY_ERROR_CODE,
                    getEmptyDetails(),
                    exception.getMessage());
        }
        throwable = getThrowable(AuthenticationFailedException.class, exception);
        if (null != throwable) {
            return new ErrorResponse(
                    ErrorResponse.AUTHENTICATION_FAILED_ERROR_CODE,
                    getEmptyDetails(),
                    exception.getMessage());
        }
        return new ErrorResponse(
                ErrorResponse.DEFAULT_ERROR_CODE,
                getEmptyDetails(),
                "Our system is facing an issue to complete your request");
    }

}
