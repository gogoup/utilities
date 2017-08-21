package org.gogoup.utilities.play.commons;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

public abstract class ErrorResultFactory {

    public Result generate(Throwable exception) {
        ErrorResponse response = getErrorResponse(exception);
        JsonNode responseJson = Json.toJson(response);
        Result result;
        if (response.getCode() == ErrorResponse.DEFAULT_ERROR_CODE) {
            result = Results.internalServerError(responseJson);
        } else if (response.getCode() == ErrorResponse.NO_SUCH_ENTITY_ERROR_CODE) {
            result = Results.notFound(responseJson);
        } else if (response.getCode() == ErrorResponse.AUTHENTICATION_FAILED_ERROR_CODE) {
            result = Results.unauthorized(responseJson);
        } else {
            result = Results.badRequest(responseJson);
        }
        return result;
    }

    protected abstract ErrorResponse getErrorResponse(Throwable exception);

}
