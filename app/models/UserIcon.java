package models;

import net.minidev.json.JSONObject;

public class UserIcon {
    private String path;
    private String provider = "local";;

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
}
