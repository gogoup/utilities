package org.gogoup.utilities.play.auth;

import play.mvc.Http;

/**
 * Created by ruisun on 2017-01-01.
 */
public class APIKeyAuthHandler implements AuthHandler {

    private String apiKey;

    public APIKeyAuthHandler(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return "APIKey";
    }

    @Override
    public boolean verify(Http.Context ctx) {
        Http.Request request = ctx.request();
        String token = AuthHelper.readToken(request);
        return token.equals(apiKey);
    }
}
