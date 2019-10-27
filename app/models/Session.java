package models;

import models.dto.SessionJwtDTO;

public class Session {
    private String jwt;

    private String sessionId;
    private String expiration = "expire test";

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
}