package controllers;

import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(Config configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    protected CompletionStage<Result> onDevServerError(Http.RequestHeader var1, UsefulException var2) {
        return CompletableFuture.completedFuture(Results.status(500, views.html.errorPage.render()));
    }
}
