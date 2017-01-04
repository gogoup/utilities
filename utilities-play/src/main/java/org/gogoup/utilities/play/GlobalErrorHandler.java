package org.gogoup.utilities.play;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.http.HttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by ruisun on 2016-04-16.
 */
public class GlobalErrorHandler implements HttpErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @Inject
    public GlobalErrorHandler(Configuration configuration) {
    }

    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        if(statusCode == play.mvc.Http.Status.NOT_FOUND) {
            // move your implementation of `GlobalSettings.onHandlerNotFound` here
        }
        if(statusCode == play.mvc.Http.Status.BAD_REQUEST) {
            // move your implementation of `GlobalSettings.onBadRequest` here
        }
        logError(request, statusCode, new RuntimeException(message));
        return CompletableFuture.completedFuture(
                Results.status(statusCode, "A client error occurred: " + message)
        );
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        logError(request, 500, exception);
        return CompletableFuture.completedFuture(
                Results.internalServerError("A server error occurred: " + exception.getMessage())
        );
    }

    private void logError(Http.RequestHeader request, int statusCode, Throwable exception) {
        LOG.error("client={} method={} request={} status={}",
                request.remoteAddress(),
                request.method(),
                request.uri(),
                statusCode,
                exception);
    }
}
