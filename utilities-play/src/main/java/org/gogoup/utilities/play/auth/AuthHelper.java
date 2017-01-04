package org.gogoup.utilities.play.auth;

import org.gogoup.utilities.play.commons.AuthenticationFailedException;
import play.mvc.Http;

/**
 * Created by ruisun on 2017-01-01.
 */
public class AuthHelper {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_QUERY_STRING = "token";

    public static String readToken(Http.Request request) {
        if (!request.hasHeader(AUTH_HEADER)
                && null == request.getQueryString(TOKEN_QUERY_STRING)) {
            throw new AuthenticationFailedException(
                    "To perform this action, you need give either "
                            + AUTH_HEADER + " header or "
                            + TOKEN_QUERY_STRING + " query string");
        }
        if (request.hasHeader(AUTH_HEADER)) {
            return request.getHeader(AUTH_HEADER);
        } else {
            return request.getQueryString(TOKEN_QUERY_STRING);
        }
    }

}
