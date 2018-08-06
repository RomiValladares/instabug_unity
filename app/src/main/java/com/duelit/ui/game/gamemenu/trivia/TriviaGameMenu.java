package com.duelit.ui.game.gamemenu.trivia;

import android.os.Bundle;

import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.game.gamemenu.GameMenu;

/**
 * Created by romin on 26/4/2016.
 */
public class TriviaGameMenu extends GameMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getGameTutorialLayout() {
        return R.layout.tutorial_game_trivia;
    }

    @Override
    protected Game getGame() {
        return new Game(Game.TRIVIA_ID);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.game_menu_trivia;
    }

    @Override
    protected int getGameLargeThumbnail() {
        return 0;
    }
}
