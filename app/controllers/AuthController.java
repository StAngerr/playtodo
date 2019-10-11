package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Credentials;
import play.cache.AsyncCacheApi;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static play.mvc.Results.*;

public class AuthController {

    private AsyncCacheApi cache;

    @Inject
    public void StoreToCache(AsyncCacheApi cache) {
        this.cache = cache;
    }

    public Result login(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        String validationResult = validate(username, password);

        if (validationResult != null) {
            return badRequest(validationResult);
        } else {
            CompletionStage<Optional<Credentials>> stage = cache.getOptional("account-" + username);
            try {
                Optional<Credentials> result = stage.toCompletableFuture().get();

                if (result.isPresent()) {
                    Credentials creds = result.get();
                    return ok(creds.username);
                } else {
                    return ok("Not found");
                }
            } catch (InterruptedException | ExecutionException e) {
                return ok(e.getMessage());
            }
        }
    }

    public Result register(Http.Request request) {
        JsonNode json = request.body().asJson();
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
