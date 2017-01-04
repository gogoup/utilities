package org.gogoup.utilities.play;

import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

import java.util.Date;

/**
 * Created by ruisun on 2016-04-16.
 */
public class ApplicationLoader extends GuiceApplicationLoader {

    @Override
    public GuiceApplicationBuilder builder(play.ApplicationLoader.Context context) {
        String env = System.getProperty("env");
        if (StringUtils.isBlank(env)) {
            env = System.getenv("APP_ENV");
        }
        if (StringUtils.isBlank(env)) {
            env = "dev";
        }
        System.out.println("Environment: " + env);
        String configPath = String.format("application.%s.conf", env);
        System.out.println("Configuration: " + configPath);
        System.out.println("Starting at: " + new Date(System.currentTimeMillis()));
        Configuration cfg = new Configuration(ConfigFactory.load(configPath));
        return initialBuilder
                .in(context.environment())
                .loadConfig(cfg.withFallback(context.initialConfiguration()))
                .overrides(overrides(context));
    }

}
