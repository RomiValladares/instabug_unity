package com.duelit;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import com.duelit.model.Session;
import com.duelit.model.User;
import com.duelit.utils.DateTimeConverter;

/**
 * Created by romin on 3/3/2016.
 * for now this class will save global variables
 */
public class AppState {
    private static final String SESSION_FILE = "playandearn_session_file";
    private static AppState instance = null;

    protected AppState() {
        // Exists only to defeat instantiation.
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    /**
     * returned when signing in, and needed for any request to te server
     */
    private Session session;

    public String getSessionToken() {
        return getSession() == null ? "" : session.getSessionToken();
    }

    public String getFirebaseToken() {
        return getSession() == null ? "" : session.getFirebaseToken();
    }

    public void setSession(Session session) {
        this.session = session;
        saveSession();
    }

    public User getSessionUser() {
        User user = getSession() == null ? null : session.getUser();
        return user;
    }

    void saveSession() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("session", getSessionGsonBuilder().toJson(session));
        editor.commit();
    }

    Session retrieveSession() {
        String strJson = getPreferences().getString("session", "");//second parameter is necessary ie.,Value to return if this preference does not exist.
        if (strJson != null && !strJson.trim().equals("") && !strJson.equals("null")) {
            this.session = getSessionGsonBuilder().fromJson(strJson, Session.class);
        }
        return this.session;
    }

    private SharedPreferences getPreferences() {
        Context context = AppController.getAppContext();
        return context.getSharedPreferences(SESSION_FILE, Context.MODE_PRIVATE);
    }

    public Session getSession() {
        if (session == null) {
            retrieveSession();
        }

        //Log.d("Lifecycle", "getSession session " + session);

        return session;
    }

    public void updateSessionUser(User user) {
        if (session != null) {
            session.setUser(user);
            saveSession();
        }
    }

    public void setDeviceToken(String deviceToken) {
        if (session != null) {
            session.setDeviceToken(deviceToken);
            saveSession();
        }
    }

    public Gson getSessionGsonBuilder() {
        final GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeConverter());
        return builder.create();
    }
}
