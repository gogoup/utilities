package org.gogoup.utilities.play.commons;

import org.gogoup.utilities.misc.NoSuchEntityFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruisun on 2016-05-19.
 */
public class ErrorResponse {

    public static final int ERROR_INTERNAL_SERVER_ERROR = -1;
    public static final int ERROR_NO_SUCH_ENTITY = 400000;
    public static final int AUTHENTICATION_FAILED = 400401;

    private int code;
    private Map<String, Object> details;
    private String message;

    public ErrorResponse(Throwable exception) {
        this.code = ErrorResponse.getCode(exception);
        this.details = getDetails(exception);
        if (ErrorResponse.ERROR_INTERNAL_SERVER_ERROR == code) {
            this.message = "Our system is facing an issue to complete your request";
        } else {
            this.message = exception.getMessage();
        }
    }

    private Map<String, Object> getDetails(Throwable exception) {
        Map<String, Object> details;
        details = new HashMap<>();
        return Collections.unmodifiableMap(details);
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

    private static int getCode(Throwable exception) {
        int code = ERROR_INTERNAL_SERVER_ERROR;
        if (NoSuchEntityFoundException.class.equals(exception.getClass())) {
            code = ErrorResponse.ERROR_NO_SUCH_ENTITY;
        } else if (AuthenticationFailedException.class.equals(exception.getClass())) {
            code = AUTHENTICATION_FAILED;
        }
        return code;
    }

}
