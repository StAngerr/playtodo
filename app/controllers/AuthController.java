package controllers;

import akka.Done;
import com.fasterxml.jackson.databind.JsonNode;
import models.Credentials;
import play.cache.AsyncCacheApi;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.request;
import static play.mvc.Results.*;

public class AuthController {

    private AsyncCacheApi cache;

    @Inject
    @BodyParser.Of(BodyParser.Json.class)
    public Result login(AsyncCacheApi cache) {
        JsonNode json = request().body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        String validationResult = validate(username, password);

        if (validationResult != null) {
            return badRequest(validationResult);
        } else {
            try {
                CompletionStage<Optional<Credentials>> result = cache.getOptional("account");
                if (result != null) {
                    return ok("result.username");
                } else {
                    return ok("No in cache");
                }

            } catch (RuntimeException e) {
                return ok(e.getMessage());
            }
        }
        // return ok("Created ");
    }

    @Inject
    @BodyParser.Of(BodyParser.Json.class)
    public Result register(AsyncCacheApi cache) {
        JsonNode json = request().body().asJson();
        try {
            Credentials newUser = Json.fromJson(json, Credentials.class);
            cache.set("account-" + newUser.username, newUser);
        } catch (RuntimeException e) {
            return badRequest(e.getMessage());
        }
        return ok("User created");
    }

    private String validate(String username, String password) {
        if ( (username == null || username.trim().isEmpty()) || (password == null || password.isEmpty())) {
            return "bad creds";
        }
        return null;
    }
}
