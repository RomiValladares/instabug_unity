package com.duelit.ui.game.playgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.duelit.R;
import com.duelit.ui.BaseActivity;
import com.duelit.ui.game.GameFactory;

public class UnityTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unity_test);

        ViewGroup btnLayout = (ViewGroup) findViewById(R.id.container_unity_test);

        for (GameFactory.Games dir : GameFactory.Games.values()) {
            Button button = new Button(this);

            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnLayout.addView(button);
            button.setTag(dir.name());
            button.setText("Start " + dir.name() + " unity game");
            button.setTextColor(ContextCompat.getColor(this, R.color.colorLightGray));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tagGames = String.valueOf(v.getTag());
                    GameFactory.Games valueOf = GameFactory.Games.valueOf(tagGames);
                    try {
                        Class unityScene = GameFactory.getGameActivity(valueOf);
                        Intent i = new Intent(UnityTestActivity.this, unityScene);
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
