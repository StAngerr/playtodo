package controllers;

import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;

import javax.inject.Inject;
import javax.inject.Provider;

public class ErrorHandler extends DefaultHttpErrorHandler {
    @Inject
    public ErrorHandler(Config configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }
}