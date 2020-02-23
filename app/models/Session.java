package models;

import models.dto.SessionJwtDTO;
import net.minidev.json.JSONObject;

import java.util.Date;

public class Session {
    private String jwt;
    private String sessionId;
    private String userId;
    private long expiration;
    private User user;

    public Session(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expiration = new Date().getTime();
    }

    public Session(String sessionId, String userId, long expiration) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expiration = expiration;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public SessionJwtDTO getDataForJwt() {
        return new SessionJwtDTO(this.sessionId, this.userId, expiration);
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getExpiration() {
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

    public String getUserId() {
        return userId;
    }
}