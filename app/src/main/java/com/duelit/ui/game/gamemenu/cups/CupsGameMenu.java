package com.duelit.ui.game.gamemenu.cups;

import android.os.Bundle;

import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.game.gamemenu.GameMenu;

/**
 * Created by romin on 26/4/2016.
 */
public class CupsGameMenu extends GameMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getGameTutorialLayout() {
        return R.layout.tutorial_game_cups;
    }

    @Override
    protected Game getGame() {
        return new Game(Game.CUPS_ID);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.game_menu_cups;
    }

    @Override
    protected int getGameLargeThumbnail() {
        return 0;
    }
}
