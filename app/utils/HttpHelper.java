package utils;

import com.fasterxml.jackson.databind.JsonNode;
import enums.UserRoles;
import models.Credentials;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
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

    public Credentials getLoginData(Http.Request request) throws Exception {
        JsonNode json = request.body().asJson();
        if (json == null) {
            throw new Exception("No request body or bad data format.");
        }
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        return new Credentials(username, password);
    }

    public Credentials getRegistrationData(Http.Request request)  throws Exception {
        JsonNode json = request.body().asJson();
        if (json == null) {
            throw new Exception("No request body or bad data format.");
        }
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        String confirmPassword = json.findPath("confirmPassword").textValue();
        return new Credentials(username, password, confirmPassword);
    }

    // move user creation out of here
    public User getUserFromRequest(Http.Request request) throws Exception {
        JsonNode json = request.body().asJson();
        if (json == null) {
            throw new Exception("No request body or bad data format.");
        }

        String username = json.findPath("username").textValue();
        String email = json.findPath("email").textValue();
        String age = json.findPath("age").textValue();
        String role = json.findPath("role").textValue();
        String password = json.findPath("password").textValue();
        if (password == null) {
            password =  RandomStringUtils.random(15);
        }
        return new User(username, password, email, Integer.parseInt(age), UserRoles.valueOf(role));
    }
}
