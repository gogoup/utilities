package org.gogoup.utilities.play;

import com.google.inject.Provider;
import org.gogoup.utilities.play.auth.APIKeyAuthHandler;
import org.gogoup.utilities.play.auth.AuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ruisun on 2017-01-01.
 */
public  class ConfigBasedAuthHandlerProvider implements Provider<List<AuthHandler>> {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigBasedAuthHandlerProvider.class);

    private List<AuthHandler> authHandlerHandlers;

    public ConfigBasedAuthHandlerProvider(Configuration configuration, ClassLoader classLoader) {
        List<String> handlers = configuration.getStringList("auth.handlers");
        this.authHandlerHandlers = new ArrayList<>(handlers.size());
        this.authHandlerHandlers.add(new APIKeyAuthHandler(configuration.getString("apiKey")));
        if (null != handlers) {
            loadAdditionalAuthHandler(handlers, classLoader);
        }
        this.authHandlerHandlers = Collections.unmodifiableList(authHandlerHandlers);
    }

    private void loadAdditionalAuthHandler(List<String> handlers, ClassLoader classLoader) {
        try {
            for (String handler: handlers) {
                LOG.info("Registering auth handler: {}", handler);
                authHandlerHandlers.add(
                        (AuthHandler) classLoader.loadClass(handler).newInstance());
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
