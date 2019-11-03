package models.dto;

// Do this class needed ??? it is used to store simplified session object into cache
public class SessionJwtDTO {
    public String sessionId;
    public String userId;
    public long expiration;

    public SessionJwtDTO(String sessionId, String userId, long expiration) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expiration = expiration;
    }
}
