package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.api.mvc.Request;
import play.mvc.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class AuthController {

    @BodyParser.Of(BodyParser.Json.class)
    public Result login() {
        JsonNode json = request().body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();

        if (username == null || username.trim().isEmpty()) {
            return badRequest("no username");
        }

        if (password == null || password.isEmpty()) {
            return badRequest("no password");
        }


        return ok("Created " + username);
    }
}
