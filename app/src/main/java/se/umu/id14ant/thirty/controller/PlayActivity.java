package se.umu.id14ant.thirty.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import se.umu.id14ant.thirty.R;
import se.umu.id14ant.thirty.model.Die;
import se.umu.id14ant.thirty.model.Game;
import se.umu.id14ant.thirty.model.ImageHandler;

/**
 * Activity that displays the play screen of the game.
 */
public class PlayActivity extends AppCompatActivity {

    private static final String KEY_GAME = "game";
    private static final String EXTRA_GAME_PLAY = "se.umu.id14ant.thirty.play";
    private static final int SCORE_REQUEST = 0;

    private Game mGame;

    /**
     * Creates the play screen and initializes necessary attributes.
     * @param savedInstanceState instance state saved before destruction of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //Get saved game state if there is one, otherwise create a new game state.
        if(savedInstanceState != null){
            mGame = savedInstanceState.getParcelable(KEY_GAME);
        } else {
            mGame = new Game();
        }

        setCurrentRound();
        showDices();
    }

    /**
     * Saves the game state if the activity gets destroyed.
     * @param savedInstanceState the instance state used for storing the game state.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(KEY_GAME, mGame);
    }

    /**
     * Throws the dices in the game and displays the new values. If there is no throws the user
     * gets noted by a toast.
     * @param view the current view.
     */
    public void throwDices(View view){
        boolean throwsLeft = mGame.rollDices();
        if(throwsLeft){
            showDices();
        } else{
            Toast.makeText(this, R.string.no_throws, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Changes the images of the dices and updates number of throws left.
     */
    private void showDices(){
        View v = findViewById(android.R.id.content);
        for(Die die: mGame.getDices()){
            ImageHandler.setDieImage(v, die);
        }

        setThrowsLeft();
    }

    /**
     * Toggles the state of the given die and change the color of the die to correspond to the
     * new state.
     * @param view the clicked button.
     */
    public void saveDie(View view){
        Die [] dices = mGame.getDices();
        switch (view.getId()) {
            case R.id.dice1:
                dices[0].toggleSaved();
                ImageHandler.setDieImage(view, dices[0]);
                break;
            case R.id.dice2:
                dices[1].toggleSaved();
                ImageHandler.setDieImage(view, dices[1]);
                break;
            case R.id.dice3:
                dices[2].toggleSaved();
                ImageHandler.setDieImage(view, dices[2]);
                break;
            case R.id.dice4:
                dices[3].toggleSaved();
                ImageHandler.setDieImage(view, dices[3]);
                break;
            case R.id.dice5:
                dices[4].toggleSaved();
                ImageHandler.setDieImage(view, dices[4]);
                break;
            case R.id.dice6:
                dices[5].toggleSaved();
                ImageHandler.setDieImage(view, dices[5]);
                break;
        }
    }

    /**
     * Displays the current round on the screen.
     */
    private void setCurrentRound(){
        TextView textView = findViewById(R.id.current_round);
        textView.setText(String.format(getResources().getString(R.string.current_round), mGame.getCurrentRound()));
    }

    /**
     * Displays the number of throws left on the current round.
     */
    private void setThrowsLeft(){
        TextView textView =  findViewById(R.id.throws_left);
        textView.setText(String.format(getResources().getString(R.string.throws_left), mGame.getThrowsLeft()));
    }

    /**
     * Creates a new intent and starts the score activity.
     * @param view the current view.
     */
    public void getScore(View view){
        Intent intent = ScoreActivity.newIntent(PlayActivity.this, mGame);
        startActivityForResult(intent, SCORE_REQUEST);
    }

    /**
     * Returns a intent connected to the score activity, and that contains the current game state.
     * @param packageContext the given context.
     * @param game the current game state.
     * @return the intent.
     */
    public static Intent newIntent(Context packageContext, Game game){
        Intent i = new Intent(packageContext, ScoreActivity.class);
        i.putExtra(EXTRA_GAME_PLAY, game);
        return i;
    }

    /**
     * Receives the result from the score activity when the user has selected scoring method.
     * @param requestCode the request code of the result.
     * @param resultCode the result code of the result.
     * @param data the intent sent with the result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCORE_REQUEST) {
            if (resultCode == RESULT_OK) {
                mGame = data.getParcelableExtra(EXTRA_GAME_PLAY);
                throwDices(null);
                setCurrentRound();
            }
        }
    }
}
