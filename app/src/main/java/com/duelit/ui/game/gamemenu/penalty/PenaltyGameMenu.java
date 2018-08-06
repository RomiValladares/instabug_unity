package com.duelit.ui.game.gamemenu.penalty;

import android.os.Bundle;

import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.game.gamemenu.GameMenu;

/**
 * Created by romin on 3/3/2016.
 */
public class PenaltyGameMenu extends GameMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getGameTutorialLayout() {
        return R.layout.tutorial_game_penalty_kickoff;
    }

    @Override
    protected Game getGame() {
        return new Game(Game.PENALTY_KICKOFF_ID);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.game_menu_penalty;
    }

    @Override
    protected int getGameLargeThumbnail() {
        return 0;
    }

}
