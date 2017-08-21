package org.gogoup.utilities.play.commons;

public class DefaultErrorResultFactory extends ErrorResultFactory {

    @Override
    protected ErrorResponse getErrorResponse(Throwable exception) {
        return ErrorResponse.toErrorResponse(exception);
    }
}
