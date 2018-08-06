package com.duelit.model;

/**
 * Created by romin on 3/3/2016.
 */
public class Session {
    private boolean isFacebookSession;
    private User user;
    //session token
    private String token;
    //notification or device token
    private String deviceToken;
    private String firebaseToken;

    public Session(String firebaseToken, String token, User user) {
        this.firebaseToken = firebaseToken;
        this.token = token;
        this.user = user;
    }

    public String getSessionToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Session{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }

    public boolean isFacebookSession() {
        return isFacebookSession;
    }

    public void setIsFacebookSession(boolean isFacebookSession) {
        this.isFacebookSession = isFacebookSession;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getToken() {
        return token;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}