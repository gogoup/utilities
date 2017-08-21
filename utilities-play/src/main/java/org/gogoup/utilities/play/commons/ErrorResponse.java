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

    public static String getDefaultMessage(Throwable exception) {
        int code = getDefaultCode(exception);
        if (ErrorResponse.DEFAULT_ERROR_CODE == code) {
            return "Our system is facing an issue to complete your request";
        } else {
            return exception.getMessage();
        }
    }

    public static int getDefaultCode(Throwable exception) {
        int code = DEFAULT_ERROR_CODE;
        if (NoSuchEntityFoundException.class.equals(exception.getClass())) {
            code = ErrorResponse.NO_SUCH_ENTITY_ERROR_CODE;
        } else if (AuthenticationFailedException.class.equals(exception.getClass())) {
            code = AUTHENTICATION_FAILED_ERROR_CODE;
        }
        return code;
    }

    public static ErrorResponse toErrorResponse(Throwable exception) {
        return new ErrorResponse(
                getDefaultCode(exception),
                getEmptyDetails(),
                getDefaultMessage(exception));
    }

}
