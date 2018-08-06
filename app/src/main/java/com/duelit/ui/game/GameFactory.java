package com.duelit.ui.game;

import android.util.Log;

import com.duelit.ui.game.gamemenu.cups.CupsGameMenu;
import com.duelit.ui.game.gamemenu.penalty.PenaltyGameMenu;
import com.duelit.ui.game.gamemenu.seablocks.SeaBlocksGameMenu;
import com.duelit.ui.game.gamemenu.trivia.TriviaGameMenu;
import com.duelit.ui.game.playgame.PlayGameActivity;

import java.util.ArrayList;
import java.util.List;

import com.duelit.model.Game;
import com.duelit.ui.game.gamemenu.fruitsmash.FruitSmashGameMenu;
import com.duelit.ui.game.gamemenu.iceshooter.IceShooterGameMenu;

/**
 * Created by romin on 3/3/2016.
 * invoke this class with an enum to launch the corresponding game activity
 */
public class GameFactory {

    private static final String TAG = "GameFactory";

    /**
     * for now
     * game tag can be an id or a game type
     *
     * @param gameTag
     * @return
     */
    public static Games getGameType(String gameTag) {
        try {
            int i = Integer.parseInt(gameTag);
            switch (i) {
                case Game.FRUIT_SMASH_ID:
                    return Games.FRUIT_SMASH;
                case Game.PENALTY_KICKOFF_ID:
                    return Games.PENALTY;
                case Game.ICE_SHOOTER_ID:
                    return Games.ICE_SHOOTER;
                case Game.SEA_BLOCKS_ID:
                    return Games.SEA_BLOCKS;
                case Game.CUPS_ID:
                    return Games.CUPS;
                case Game.TRIVIA_ID:
                    return Games.TRIVIA;
            }
            return null;
        } catch (NumberFormatException ex) {
        }

        try {
            return Games.valueOf(gameTag);
        } catch (Exception ex) {
        }

        return null;
    }

    public enum Games {PENALTY, FRUIT_SMASH, ICE_SHOOTER, SEA_BLOCKS, CUPS, TRIVIA}

    /**
     * @return activity to start as this {@param gameId} game
     * @throws Exception when this {@param gameId} doesn't exist
     */
    public static Class getGameActivity(Games gameId) throws Exception {
        switch (gameId) {
            case PENALTY:
                return PenaltyGameMenu.class;
            case FRUIT_SMASH:
                return FruitSmashGameMenu.class;
            case SEA_BLOCKS:
                return SeaBlocksGameMenu.class;
            case ICE_SHOOTER:
                return IceShooterGameMenu.class;
            case TRIVIA:
                return TriviaGameMenu.class;
            case CUPS:
                return CupsGameMenu.class;
        }
        throw new Exception("Coming soon!");
    }

    public static String getGameUnityScene(int gameId, PlayGameActivity.GameMode gameMode) {
        switch (gameMode) {
            case PRACTICE:
                switch (gameId) {
                    case Game.FRUIT_SMASH_ID:
                        return "candy_practice";
                    case Game.PENALTY_KICKOFF_ID:
                        return "practice";
                    case Game.ICE_SHOOTER_ID:
                        return "bubble_practice_mode";
                    case Game.SEA_BLOCKS_ID:
                        return "Tetris_practice_mode";
                    case Game.CUPS_ID:
                        return "Beer_pong_practice_mode";
                    case Game.TRIVIA_ID:
                        return "Trivia_practice_mode";
                    default:
                        Log.e(TAG, "Couldn't find scene for gameId " + gameId);
                        return "";
                }
            case TIMER:
                switch (gameId) {
                    case Game.FRUIT_SMASH_ID:
                        return "candy_timer_mode";
                    case Game.PENALTY_KICKOFF_ID:
                        return "timer_mode";
                    case Game.ICE_SHOOTER_ID:
                        return "bubble_timer_mode";
                    case Game.SEA_BLOCKS_ID:
                        return "Tetris_timer_mode";
                    case Game.CUPS_ID:
                        return "Beer_pong_timer_mode";
                    case Game.TRIVIA_ID:
                        return "Trivia_timer_mode";
                    default:
                        Log.e(TAG, "Couldn't find scene for gameId " + gameId);
                        return "";
                }
        }
        return "";
    }

    /**
     * @return all known app games
     */
    public static List<Game> getAllPossibleGames(){
        List<Game> games =new ArrayList<>();
        games.add(new Game(Game.FRUIT_SMASH_ID));
        games.add(new Game(Game.PENALTY_KICKOFF_ID));
        games.add(new Game(Game.ICE_SHOOTER_ID));
        games.add(new Game(Game.SEA_BLOCKS_ID));
        games.add(new Game(Game.CUPS_ID));
        games.add(new Game(Game.TRIVIA_ID));

        return games;
    }
}
