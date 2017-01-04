package org.gogoup.utilities.play.auth;

import play.mvc.Http;

/**
 * Created by ruisun on 2017-01-01.
 */
public interface AuthHandler {

    public String getName();

    public boolean verify(Http.Context ctx);
}
