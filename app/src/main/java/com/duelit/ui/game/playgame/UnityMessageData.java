package com.duelit.ui.game.playgame;

/**
 * Created by Romina on 7/1/16.
 * used to communicate with Unity in ChangeScene
 */
public class UnityMessageData {
    public static final String BUILD_VARIANT_RELEASE = "Release";
    public static final String BUILD_VARIANT_DEBUG = "Debug";

    public static final String URL_RELEASE = "https://api.duelit.com/";
    public static final String URL_DEBUG = "http://dev.api.duelit.com/";
    private String scene;
    private String gameID;
    private String tournamentID;
    private String updateTime;

    private String url;
    private String token;

    //
    //

    public UnityMessageData() {
    }

    public UnityMessageData(String scene) {
        this.scene = scene;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(String tournamentID) {
        this.tournamentID = tournamentID;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
