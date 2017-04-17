package org.gogoup.utilities.play;

import com.google.inject.Provider;
import org.gogoup.utilities.play.auth.AuthHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ruisun on 2017-01-01.
 */
public  class AuthHandlerProvider implements Provider<List<AuthHandler>> {

    private List<AuthHandler> authHandlerHandlers;

    public AuthHandlerProvider(AuthHandler...handlers) {
        this.authHandlerHandlers = new ArrayList<>(handlers.length);
        this.authHandlerHandlers = Collections.unmodifiableList(Arrays.asList(handlers));
    }

    @Override
    public List<AuthHandler> get() {
        return authHandlerHandlers;
    }
}
