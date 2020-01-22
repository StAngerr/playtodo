package models;

public class UserIcon {
    private String path;
    private String provider;

    public UserIcon() {}
    public UserIcon(String path, String provider) {
        this.path = path;
        this.provider = provider;
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
