package com.duelit.ui.game.gamemenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duelit.BuildConfig;
import com.duelit.R;
import com.duelit.model.Game;
import com.duelit.ui.BaseActivity;
import com.duelit.ui.game.playgame.PlayGameActivity;
import com.duelit.ui.game.playgame.UnityTestActivity;
import com.duelit.utils.ViewFinder;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by romin on 3/3/2016.
 * generic activity for a game menu presentation
 */
public abstract class GameMenu extends BaseActivity {
    public static final String GAME_BUTTON_TAG = "GAMEMODE";
    //use this in your tutorial views that you want to add the action to close the tutorial
    public static final String TUTORIAL_CLOSE_TAG = "CLOSE";
    //this is used to programmatically change the tutorial views that have this tag
    private static final String VIEW_TAG_FILL_PARENT = "VIEW_TAG_FILL_PARENT";
    private static final String TEST_SCENES_TAG = "UNITYTESTSCENES";
    private final int DEFAULT_LAYOUT = R.layout.game_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getGameTheme());
        super.onCreate(savedInstanceState);
        int layout = getActivityLayout();
        setContentView(layout);
        if (layout == DEFAULT_LAYOUT) {
            ImageView gameThumbnail = (ImageView) findViewById(R.id.img_game_menu_large);
            gameThumbnail.setImageResource(getGameLargeThumbnail());
        }

        setToolbar();

        final ViewGroup activityView = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        ArrayList<View> menuButtons = ViewFinder.getViewsByTagContains(activityView, GAME_BUTTON_TAG);
        for (View v : menuButtons) {
            String tag = (String) v.getTag();
            String finalTag = tag.split(":")[1];
            GameModes mode = GameModes.valueOf(finalTag.toUpperCase(Locale.ENGLISH));
            switch (mode) {
                case CHALLENGE:
                    break;
                case PRACTICE:
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Track event in Keen.io
                            startPracticeMode();
                        }
                    });
                    break;
                case TOURNAMENT:
                    break;
            }
        }

        View btnHowToPlay = ViewFinder.getAnyViewByTagContains(activityView, "HOW_TO_PLAY");
        if (btnHowToPlay != null) {
            btnHowToPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Track event in Keen.io
                    showGameTutorial();
                }
            });
        }

        View btnTestScenes = ViewFinder.getAnyViewByTagContains(activityView, TEST_SCENES_TAG);
        if (btnTestScenes != null) {
            if (BuildConfig.DEBUG) {
                btnTestScenes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToUnityTestActivity();
                    }
                });
            } else {
                btnTestScenes.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.trackScreenView(getString(R.string.game_menu));
    }

    private void setToolbar() {
        TextView txtToolbarCredits = (TextView) findViewById(R.id.toolbar_title);
        if (txtToolbarCredits != null) {
            setBackToolbar();
        }
        View btnLeaderBoard = findViewById(R.id.btn_toolbar_leaderboard);
        if (btnLeaderBoard != null) {
            btnLeaderBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Track event in Keen.io
                    goToLeaderBoardActivity();
                }
            });
        }
    }

    protected void goToLeaderBoardActivity() {

    }

    private void showGameTutorial() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (findViewById(R.id.game_menu_tutorial_container) != null) {
            final ViewGroup tutorialContainer = (ViewGroup) findViewById(R.id.game_menu_tutorial_container);

            //Log.d("screenwidth", "tutorialContainer " + ViewFinder.differentDensityAndScreenSize(this));
            int tutorialLayout = getGameTutorialLayout();
            if (tutorialLayout > 0) {
                final ViewGroup inflatedTutorial = (ViewGroup) getLayoutInflater().inflate(tutorialLayout, tutorialContainer);
                ArrayList<View> closingViews = ViewFinder.getViewsByTagContains(inflatedTutorial, TUTORIAL_CLOSE_TAG);
                for (View view :
                        closingViews) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tutorialContainer.removeAllViews();
                        }
                    });
                }
                /*int parentWidth = inflatedTutorial.getMeasuredWidth() - inflatedTutorial.getPaddingLeft() - inflatedTutorial.getPaddingRight();
                ArrayList<View> fillParentViews = ViewFinder.getViewsByTagContains(inflatedTutorial, VIEW_TAG_FILL_PARENT);
                for (View v :
                        fillParentViews) {
                    Log.d("layout", parentWidth + "");
                    v.setMinimumWidth(parentWidth);
                    v.requestLayout();
                }*/
            }
        }
    }

    /**
     * @return the layout for this game's tutorial, which will be added on top of the game menu, -1 otherwise
     */
    protected abstract
    @LayoutRes
    int getGameTutorialLayout();


    private void startPracticeMode() {
        //we send game mode practice here
        goToPlayGameActivity(getGame(), PlayGameActivity.GameMode.TIMER);
    }

    private void goToUnityTestActivity() {
        Intent i = new Intent(this, UnityTestActivity.class);
        startActivity(i);
    }

    protected abstract Game getGame();

    /*
     * override this to provide a different layout
     * the layout provided MUST contain the three game modes buttons, identified by TAG =GAME_BUTTON_TAG +  CHALLENGE, TOURNAMENT, PRACTICE
     */
    @LayoutRes
    protected int getActivityLayout() {
        return DEFAULT_LAYOUT;
    }

    /**
     * override this to customize the activity style
     * called before super.onCreate()
     *
     * @return activity style
     */
    @StyleRes
    protected int getGameTheme() {
        return R.style.AppTheme;
    }

    /**
     * these modes are primarily used for displaying buttons
     * each button click will call showMode with the corresponding {@link GameMenu.GameModes}
     *
     * @return the modes that are supported for this game
     */
    protected GameModes[] getGameModes() {
        return new GameModes[]{GameModes.CHALLENGE, GameModes.TOURNAMENT, GameModes.PRACTICE};
    }

    /**
     * this will be called if no custom layout is passed
     *
     * @return the image to be used as main image
     */
    @DrawableRes
    protected abstract int getGameLargeThumbnail();

    public enum GameModes {CHALLENGE, TOURNAMENT, PRACTICE}

}
