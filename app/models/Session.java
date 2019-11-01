package models;

import models.dto.SessionJwtDTO;
import net.minidev.json.JSONObject;

public class Session {
    private String jwt;

    private String sessionId;
    private String expiration = "expire test";
    private User user;

    public Session() { }

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }
    public Session(String jwt, String sessionId) {
        this.jwt = jwt;
        this.sessionId = sessionId;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public SessionJwtDTO getDataForJwt() {
        return new SessionJwtDTO(this.sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getExpiration() {
        return expiration;
    }

    public String getJwt() {
        return jwt;
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put("token", jwt);
        if (user != null) {
            json.put("user", user.asJson());
        }
        return json;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}