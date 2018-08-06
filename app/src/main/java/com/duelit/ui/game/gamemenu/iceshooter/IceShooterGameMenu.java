package com.duelit.ui.game.gamemenu.iceshooter;

import android.os.Bundle;

import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.game.gamemenu.GameMenu;

/**
 * Created by romin on 26/4/2016.
 */
public class IceShooterGameMenu extends GameMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getGameTutorialLayout() {
        return R.layout.tutorial_game_ice_shooter;
    }

    @Override
    protected Game getGame() {
        return new Game(Game.ICE_SHOOTER_ID);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.game_menu_ice_shooter;
    }

    @Override
    protected int getGameLargeThumbnail() {
        return 0;
    }
}
