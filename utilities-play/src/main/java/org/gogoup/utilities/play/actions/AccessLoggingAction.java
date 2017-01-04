package org.gogoup.utilities.play.actions;

import akka.stream.Materializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.gogoup.utilities.play.commons.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by ruisun on 2016-06-11.
 */
public class AccessLoggingAction extends Action.Simple {

    private static final Logger LOG = LoggerFactory.getLogger("access");

    private final Materializer mat;
    @Inject
    private @Named("appName") String appName;
    @Inject
    private @Named("appVersion") String appVersion;
    @Inject
    private @Named("accessLoggingIncludeBody") Boolean includeBody;

    @Inject
    public AccessLoggingAction(Materializer mat) {
        this.mat = mat;
    }

    public CompletionStage<Result> call(Http.Context ctx) {
        MDC.put("app_name", appName);
        MDC.put("app_version", appVersion);
        final Http.Request request = ctx.request();
        long startAt = System.currentTimeMillis();
        CompletionStage<Result> stage;
        try {
            stage = delegate.call(ctx);
            return stage.whenComplete((result, throwable) -> {
                final boolean isSuccessful = throwable == null;
                if (isSuccessful) {
                    logInfo(request, result, startAt);
                }
            });
        } catch (RuntimeException e) {
            Result result = processBusinessException(e);
            if (result.status() == 500) {
                logError(request, result, startAt, e);
            } else if (result.status() >= 400 && result.status() <= 499) {
                logWarn(request, result, startAt, e);
            } else {
                logInfo(request, result, startAt);
            }
            return CompletableFuture.completedFuture(result);
        }

    }

    private Result processBusinessException(Throwable exception) {
        ErrorResponse response = new ErrorResponse(exception);
        return getResult(response);
    }

    private Result getResult(ErrorResponse response) {
        JsonNode responseJson = Json.toJson(response);
        Result result;
        if (response.getCode() == ErrorResponse.ERROR_INTERNAL_SERVER_ERROR) {
            result = Results.internalServerError(responseJson);
        } else if (response.getCode() == ErrorResponse.ERROR_NO_SUCH_ENTITY) {
            result = Results.notFound(responseJson);
        } else if (response.getCode() == ErrorResponse.AUTHENTICATION_FAILED) {
            result = Results.unauthorized(responseJson);
        } else {
            result = Results.badRequest(responseJson);
        }
        return result;
    }

    private void logError(Http.Request request, Result result, long startAt, Throwable exception) {
        long duration = System.currentTimeMillis() - startAt;
        akka.util.ByteString body = play.core.j.JavaResultExtractor.getBody(result, 10000l, mat);
        Object requestBody = getRequestBody(request);
        LOG.error("client={} method={} request={} bytes={} status={} duration={} request_body={} response_body={}",
                getClientIp(request),
                request.method(),
                request.uri(),
                body.size(),
                result.status(),
                duration,
                requestBody,
                body.decodeString("UTF-8"),
                exception);
    }

    private void logWarn(Http.Request request, Result result, long startAt, Throwable exception) {
        long duration = System.currentTimeMillis() - startAt;
        akka.util.ByteString body = play.core.j.JavaResultExtractor.getBody(result, 10000l, mat);
        Object requestBody = getRequestBody(request);
        LOG.warn("client={} method={} request={} bytes={} status={} duration={} request_body={} response_body={}",
                getClientIp(request),
                request.method(),
                request.uri(),
                body.size(),
                result.status(),
                duration,
                requestBody,
                body.decodeString("UTF-8"),
                exception);
    }

    private void logInfo(Http.Request request, Result result, long startAt) {
        long duration = System.currentTimeMillis() - startAt;

        akka.util.ByteString body = play.core.j.JavaResultExtractor.getBody(result, 10000l, mat);
        if (includeBody) {
            Object requestBody = getRequestBody(request);
            LOG.info("client={} method={} request={} bytes={} status={} duration={} request_body={} response_body={}",
                    getClientIp(request),
                    request.method(),
                    request.uri(),
                    body.size(),
                    result.status(),
                    duration,
                    requestBody,
                    body.decodeString("UTF-8"));
        } else {
            LOG.info("client={} method={} request={} bytes={} status={} duration={}",
                    getClientIp(request),
                    request.method(),
                    request.uri(),
                    body.size(),
                    result.status(),
                    duration);
        }

    }

    private Object getRequestBody(Http.Request request) {
        if (null != request.body().asJson()) {
            JsonNode node = request.body().asJson();
            maskSensitiveInfo(node);
            return node;
        }
        if (null != request.body().asXml()) {
            return request.body().asXml();
        }
        if (null != request.body().asText()) {
            return request.body().asText();
        }
        if (null != request.body().asFormUrlEncoded()) {
            JsonNode node = Json.toJson(request.body().asFormUrlEncoded());
            maskSensitiveInfo(node);
            return node;
        }
        return "";
    }

    private void maskSensitiveInfo(JsonNode parentNode) {
        if (parentNode.isArray()) {
            Iterator<JsonNode> iterator = parentNode.elements();
            while (iterator.hasNext()) {
                JsonNode childNode = iterator.next();

                if (childNode.isObject() || childNode.isArray()) {
                    maskSensitiveInfo(childNode);
                }
            }
        }
        if (parentNode.isObject()) {
            Iterator<String> iterator = parentNode.fieldNames();
            while (iterator.hasNext()) {
                String nodeName = iterator.next();
                JsonNode childNode = parentNode.path(nodeName);
                if (isSensitiveFieldName(nodeName)) {
                    if (childNode.isObject()) {
                        ((ObjectNode)childNode).put(nodeName, "******");
                    } else {
                        ((ObjectNode) parentNode).put(nodeName, "******");
                    }
                }
                if (childNode.isObject() || childNode.isArray()) {
                    maskSensitiveInfo(childNode);
                }
            }
        }
    }

    private boolean isSensitiveFieldName(String name) {
        return (name.equalsIgnoreCase("password"));
    }

    private String getClientIp(Http.Request request) {
        if (null != request.getHeader("X-Forwarded-For")) {
            return request.getHeader("X-Forwarded-For");
        }
        return request.remoteAddress();
    }
}