package com.duelit;

import java.util.Observable;

public class SessionManager extends Observable {
    private static SessionManager instance = null;

    protected SessionManager() {
        // Exists only to defeat instantiation.
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void logout() {
    }
}
