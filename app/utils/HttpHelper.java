package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.Credentials;
import play.mvc.Http;

public class HttpHelper {
    private static HttpHelper instance;

    private HttpHelper() {}

    public static HttpHelper getInstance() {
        if (instance == null) {
            instance = new HttpHelper();
        }
        return instance;
    }

    public Credentials getLoginData(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        return new Credentials(username, password);
    }

    public Credentials getRegistrationData(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        String confirmPassword = json.findPath("confirmPassword").textValue();
        return new Credentials(username, password, confirmPassword);
    }
}
