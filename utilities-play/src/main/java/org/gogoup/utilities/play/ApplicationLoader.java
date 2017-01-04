package org.gogoup.utilities.play;

import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

import java.util.Date;

/**
 * Created by ruisun on 2016-04-16.
 */
public class ApplicationLoader extends GuiceApplicationLoader {

    private static final Logger LOG = LoggerFactory.getLogger("loader");

    @Override
    public GuiceApplicationBuilder builder(play.ApplicationLoader.Context context) {
        String env = System.getProperty("env");
        if (StringUtils.isBlank(env)) {
            env = System.getenv("APP_ENV");
        }
        if (StringUtils.isBlank(env)) {
            env = "dev";
        }
        LOG.debug("Environment: {}", env);
        String configPath = String.format("application.%s.conf", env);
        LOG.debug("Configuration: {}", configPath);
        LOG.debug("Starting at: {}", new Date(System.currentTimeMillis()));
        Configuration cfg = new Configuration(ConfigFactory.load(configPath));
        return initialBuilder
                .in(context.environment())
                .loadConfig(cfg.withFallback(context.initialConfiguration()))
                .overrides(overrides(context));
    }

}
