package com.duelit.model;

import java.util.Locale;

public class Tournament {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public enum TournamentType {REGULAR, OPEN, PUBLIC}

    public enum TournamentStatus {
        CREATED, TIMEOUT, ENDED, STARTED, CANCELLED;

        public static TournamentStatus getStatus(String parse) {
            switch (parse.toUpperCase(Locale.ENGLISH)) {
                case "TIMEDOUT":
                case "TIMEOUT":
                    return TIMEOUT;
                default:
                    return Tournament.TournamentStatus.valueOf(parse.toUpperCase(Locale.ENGLISH));
            }
        }
    }

}
