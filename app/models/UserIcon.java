package models;

import net.minidev.json.JSONObject;
import play.libs.Files;

public class UserIcon {
    private String path;
    private String provider = "local";
    private String fileName;
    private Files.TemporaryFile ref;

    public UserIcon(String path, String fileName, Files.TemporaryFile ref) {
        this.fileName = fileName;
        this.path = path;
        this.ref = ref;
    }

    public UserIcon(String path) {
        this.path = path;
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        json.put("path", this.path);
        json.put("provider", provider);
        return json;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFileName() {
        return fileName;
    }

    public Files.TemporaryFile getRef() {
        return ref;
    }
}
