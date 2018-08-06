package com.duelit.ui.game.gamemenu.seablocks;

import android.os.Bundle;

import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.game.gamemenu.GameMenu;

/**
 * Created by romin on 26/4/2016.
 */
public class SeaBlocksGameMenu extends GameMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getGameTutorialLayout() {
        return R.layout.tutorial_game_sea_blocks;
    }

    @Override
    protected Game getGame() {
        return new Game(Game.SEA_BLOCKS_ID);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.game_menu_sea_blocks;
    }

    @Override
    protected int getGameLargeThumbnail() {
        return 0;
    }
}
