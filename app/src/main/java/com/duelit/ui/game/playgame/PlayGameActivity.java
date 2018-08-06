package com.duelit.ui.game.playgame;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.duelit.BuildConfig;
import com.duelit.R;
import com.duelit.SessionManager;
import com.duelit.model.Game;
import com.duelit.model.Tournament;
import com.duelit.ui.game.GameFactory;
import com.duelit.utils.SurfaceTextureHelper;
import com.google.gson.Gson;
import com.rokk3r.penal.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;

public class PlayGameActivity extends UnityPlayerActivity {
    private static final int UPDATE_TIME = 2;
    private static final String TAG = "PlayGameActivity";
    /**
     * identifier. use it to pass the game id, to load the scene
     */
    public static String BUNDLE_GAME = "BUNDLE_GAME";
    public static String BUNDLE_GAME_MODE = "BUNDLE_GAME_MODE";
    public static String BUNDLE_TOURNAMENT = "BUNDLE_TOURNAMENT_ID";
    public static String BUNDLE_SCENE_MODE = "BUNDLE_SCENE_MODE";
    /**
     * use this for testing purposes only!
     * used in UnityTestActivity
     */
    public static String BUNDLE_GAME_SCENE = "BUNDLE_GAME_SCENE";
    //uses because we need to identify the received event using Message in mHandler
    private final int UNITY_EVENT_TOTAL_POINTS = 22;
    private final int UNITY_EVENT_ANY = 44;
    private final int UNITY_EVENT_SCENE_LOAD = 66;
    private final int UNITY_EVENT_UPDATE_POINTS = 88;
    //just a constant with the word code that unity sends when a scene is changed
    private final String UNITY_CODE_CHANGE_SCENE = "ChangeScene";
    private SurfaceTextureHelper textureHelper;
    private int gameId;
    private Game game;
    private Handler mHandler;
    private Tournament tournament;
    private View progressView;
    private View containerView;
    private PlayGameMode gameMode;
    private SceneMode sceneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpUnityPlayer();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_game);

        setUpProgress();

        String jsonUnityData = getUnityData();
        Log.d(TAG, "JSON - " + jsonUnityData);

        UnityPlayer.UnitySendMessage("MainMenuController", "ChangeScene", jsonUnityData);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                onUnityResult(message);
            }
        };

        sceneMode = getSceneMode();

        setUpViews();
    }

    private SceneMode getSceneMode() {
        SceneMode sceneMode;
        try {
            sceneMode = (SceneMode) getIntent().getExtras().get(BUNDLE_SCENE_MODE);
            if (sceneMode == null)
                sceneMode = SceneMode.PRACTICE;
        } catch (Exception ex) {
            sceneMode = SceneMode.PRACTICE;
        }
        return sceneMode;
    }

    private String getUnityData() {
        String unityScene = String.valueOf(getIntent().getExtras().get(BUNDLE_GAME_SCENE));
        game = getIntent().getExtras().getParcelable(BUNDLE_GAME);

        UnityMessageData data = new UnityMessageData();
        tournament = (Tournament) getIntent().getExtras().get(BUNDLE_TOURNAMENT);
        //decide which gameMode we are on
        if (!TextUtils.isEmpty(unityScene) && !unityScene.equals("null")) {
            gameMode = PlayGameMode.TEST_SCENES;
        } else {
            GameMode gameMode = GameMode.valueOf(String.valueOf(getIntent().getExtras().get(BUNDLE_GAME_MODE)));
            if (gameMode.equals(GameMode.PRACTICE)) {
                this.gameMode = PlayGameMode.PRACTICE;
            } else {
                this.gameMode = PlayGameMode.CHALLENGE;
            }

            gameId = game.getId();
            unityScene = GameFactory.getGameUnityScene(gameId, gameMode);

            data.setGameID(String.valueOf(gameId));

            if (tournament == null) {
                data.setTournamentID("1");
            } else {
                data.setTournamentID(String.valueOf(tournament.getId()));
            }
        }

        data.setScene(unityScene);
        data.setUpdateTime(String.valueOf(UPDATE_TIME));
        data.setUrl(BuildConfig.DEBUG ? UnityMessageData.URL_DEBUG : UnityMessageData.URL_RELEASE);
        //TODO doesn't unity check for the token??
        data.setToken("");

        return new Gson().toJson(data);
    }

    private void setUpViews() {
        Log.d(TAG, "setUpViews");
        ImageView btnExit = (ImageView) findViewById(R.id.btn_exit_play_game);
        switch (sceneMode) {
            case PRACTICE:
                Log.d(TAG, "Practice");
                btnExit.setVisibility(View.VISIBLE);
                btnExit.setBackgroundColor(Color.TRANSPARENT);
                Log.d(TAG, "setUpProgress.btnExit");
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                break;
            case CHALLENGE:
                btnExit.setVisibility(View.GONE);
                break;
        }

    }

    private int getBackButtonImg() {
        //return new Game(gameId).getDrawableResource(Game.GameResource.BACK_BUTTON);
        return R.drawable.btn_back_penalty;
    }

    private void setUpProgress() {
        progressView = findViewById(R.id.progress_activity_play_game);
        containerView = findViewById(R.id.container_activity_play_game);

        progressView.setVisibility(View.VISIBLE);
        containerView.setVisibility(View.INVISIBLE);

        // Add the Unity view
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.container_unity_play_game_activity);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout.getLayoutParams();
        layout.addView(mUnityPlayer.getView(), 0, lp);
    }

    private void setUpUnityPlayer() {
        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        boolean trueColor8888 = false;
        mUnityPlayer.init(glesMode, trueColor8888);
    }

    private void onUnityResult(Message message) {
        switch (message.what) {
            case UNITY_EVENT_SCENE_LOAD:
                String event = (String) message.obj;
                if (event != null && event.equals(UNITY_CODE_CHANGE_SCENE)) {
                    progressView.setVisibility(View.GONE);
                    containerView.setVisibility(View.VISIBLE);
                    if (textureHelper != null) {
                        textureHelper.onDestroy();
                    }

                    ViewGroup overlayContainer = (ViewGroup) findViewById(R.id.game_ranking_overlay_container);
                    if (tournament != null) {
                        overlayContainer.setVisibility(View.VISIBLE);
                    } else {
                        overlayContainer.setVisibility(View.GONE);
                    }
                }
                break;
            case UNITY_EVENT_TOTAL_POINTS:
                int points = Integer.parseInt(String.valueOf(message.obj));
                onGameFinished(points);
                break;
            case UNITY_EVENT_UPDATE_POINTS:
                int newPoints = Integer.parseInt(String.valueOf(message.obj));
                updatePoints(newPoints);
                break;
        }
    }

    private void updatePoints(int newPoints) {
        if (tournament != null) {
            Log.d(TAG, "updatePoins " + newPoints);
        }
    }

    private void onGameFinished(int points) {
        if (tournament != null) {
            finish();
        } else {
            //we're on playing gameMode, just go back
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textureHelper != null) {
            textureHelper.onDestroy();
        }
    }

    @Override
    protected boolean shouldHandleBackPressed() {
        //let go back in case we are not playing a tournament
        return tournament != null;
    }

    //////UNITY BRIDGE
    public void TotalPoints(String points) {
        Log.d(TAG, "PlayGameActivity.TotalPoints points=" + points);

        // call it from the worker thread:
        Message message = mHandler.obtainMessage(UNITY_EVENT_TOTAL_POINTS, points);
        message.sendToTarget();
    }

    public void UpdatePoints(String points) {
        Log.d(TAG, "PlayGameActivity.UpdatePoints points=" + points);

        // call it from the worker thread:
        Message message = mHandler.obtainMessage(UNITY_EVENT_UPDATE_POINTS, points);
        message.sendToTarget();
    }

    //public void OnUnityEvent(String event) {
    public void SceneLoad(String event) {
        Log.d(TAG, "PlayGameActivity.OnUnityEvent " + event);

        // call it from the worker thread:
        Message message = mHandler.obtainMessage(UNITY_EVENT_SCENE_LOAD, event);
        message.sendToTarget();
    }

    public enum GameMode {PRACTICE, TIMER}

    private enum PlayGameMode {TEST_SCENES, PRACTICE, CHALLENGE}

    /**
     * used to decide over the activity layout
     * challenge enables overlay, practice enables back button
     * default is practice
     */
    public enum SceneMode {
        CHALLENGE, PRACTICE
    }

}
