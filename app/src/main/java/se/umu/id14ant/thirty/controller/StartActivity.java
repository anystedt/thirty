package se.umu.id14ant.thirty.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import se.umu.id14ant.thirty.R;

/**
 * Activity that displays the home screen of the game.
 * @author Anna Nystedt.
 */
public class StartActivity extends AppCompatActivity {

    /**
     * Called on creation of the activity.
     * @param savedInstanceState instance state saved before destruction of activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /**
     * Creates a new intent and starts the play-activity, when the user starts a new game.
     * @param view the current view.
     */
    public void startGame(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
}
