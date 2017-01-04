package org.gogoup.utilities.play;

import com.google.inject.Provider;
import org.gogoup.utilities.play.auth.APIKeyAuthHandler;
import org.gogoup.utilities.play.auth.AuthHandler;
import play.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ruisun on 2017-01-01.
 */
public  class AuthHandlerProvider implements Provider<List<AuthHandler>> {

    private List<AuthHandler> authHandlerHandlers;

    public AuthHandlerProvider(Configuration configuration) {
        this.authHandlerHandlers = new ArrayList<>(5);
        this.authHandlerHandlers.add(new APIKeyAuthHandler(configuration.getString("apiKey")));
        List<String> handlers = configuration.getStringList("auth.handlers");
        if (null != handlers) {
            loadAdditionalAuthHandler(handlers);
        }
        this.authHandlerHandlers = Collections.unmodifiableList(authHandlerHandlers);
    }

    private void loadAdditionalAuthHandler(List<String> handlers) {
        try {
            for (String handler: handlers) {
                authHandlerHandlers.add((AuthHandler) Class.forName(handler).newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthHandler> get() {
        return authHandlerHandlers;
    }
}
