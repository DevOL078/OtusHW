package ru.otus.salamandra.app.store;

import java.util.UUID;

public class SessionStore {

    private static SessionStore instance = new SessionStore();

    private String userLogin;
    private String baseDirPath;
    private final String APP_ID = UUID.randomUUID().toString();

    private SessionStore() {}

    public static SessionStore getInstance() {
        return instance;
    }

    public synchronized void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public synchronized String getUserLogin() {
        return userLogin;
    }

    public synchronized void setBaseDirPath(String baseDirPath) {
        this.baseDirPath = baseDirPath;
    }

    public synchronized String getBaseDirPath() {
        return baseDirPath;
    }

    public String getAppId() {
        return APP_ID;
    }

}
