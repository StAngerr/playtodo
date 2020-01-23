package models;

import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;

public class UserIcon {
    private String path;
    private String provider = "local";;

    public UserIcon() {}
    public UserIcon(String path) {
        this.path = path;
    }
    public UserIcon(String path, String provider) {
        this.path = path;
        this.provider = provider;
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
