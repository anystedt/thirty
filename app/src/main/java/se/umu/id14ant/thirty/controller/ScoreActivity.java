package se.umu.id14ant.thirty.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import se.umu.id14ant.thirty.R;
import se.umu.id14ant.thirty.model.Die;
import se.umu.id14ant.thirty.model.Game;
import se.umu.id14ant.thirty.model.ImageHandler;

/**
 * Activity that displays the score activity of the game.
 */
public class ScoreActivity extends AppCompatActivity {

    private static final String EXTRA_GAME_SCORE = "se.umu.id14ant.thirty.mScore";
    private static final int LOW = 1;

    private Game mGame;
    private int mScore;
    private Spinner mSpinner;

    /**
     * Creates the score screen and initializes the necessary attributes.
     * @param savedInstanceState instance state saved before destruction of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Get the game state from previous activity.
        mGame = getIntent().getParcelableExtra(EXTRA_GAME_SCORE);
        setCurrentRound();

        //Initialize spinner options and add a listener.
        mSpinner = findViewById(R.id.score_spinner);
        addItemsToSpinner();
        addListenerToSpinner();

        //For each die change the state to unsaved to avoid showing gray dices and display the images.
        for (Die die: mGame.getDices()) {
            if(die.isSaved()){
                die.toggleSaved();
            }
            ImageHandler.setDieImage(findViewById(android.R.id.content), die);
        }

        //Change the button text if the current round was the last one.
        if(mGame.getCurrentRound() == 10){
            Button nextButton = findViewById(R.id.next_button);
            nextButton.setText(R.string.get_result);
        }
    }

    /**
     * Creates an adapter and add items to spinner using the string array declared as a
     * string resource.
     */
    private void addItemsToSpinner(){
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.score_array)) {

            //Disable the default-value and all used scoring methods.
            @Override
            public boolean isEnabled(int position) {
                return position != 0 && !mGame.methodIsUsed(position - 1);
            }

            //Change the color of the used scoring method to distinguish these from the selectable ones.
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if(position == 0){
                    mTextView.setTextColor(Color.LTGRAY);
                } else{
                    if (mGame.methodIsUsed(position-1)) {
                        mTextView.setTextColor(Color.LTGRAY);
                    } else {
                        mTextView.setTextColor(Color.BLACK);
                    }
                }

                return mView;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    /**
     * Adds a listener to the spinner to act upon the users choice.
     */
    private void addListenerToSpinner(){
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateScore(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Does nothing
            }
        });
    }

    /**
     * Displays the current round in the view.
     */
    private void setCurrentRound(){
        TextView textViewRound = findViewById(R.id.current_round);
        textViewRound.setText(String.format(getResources().getString(R.string.current_round), mGame.getCurrentRound()));
    }

    /**
     * Calculates the score of the chosen method and displays the score in the view.
     * @param position the selected position
     */
    private void calculateScore(int position){
        if(position == LOW){
            mScore = mGame.calculateLowScore();
            showScore(mScore);
        } else if(position > LOW){
            mScore = mGame.calculateScore(position+2);
            showScore(mScore);
        }
    }

    /**
     * Displays the score in the view.
     * @param score the score.
     */
    private void showScore(int score){
        TextView textView =  findViewById(R.id.chosen_score);
        textView.setText(String.format(getResources().getString(R.string.show_score), score));
        textView.setVisibility(View.VISIBLE);
    }

    /**
     * Updates the game state and if there is rounds left, sends the state back to the play
     * activity. Otherwise starts the result activity.
     * @param view the current view.
     */
    public void nextRound(View view){
        int methodIndex = mSpinner.getSelectedItemPosition();
        String scoringMethod = mSpinner.getSelectedItem().toString();

        //If the user has chosen a valid scoring method update game state, otherwise display a toast with feedback.
        if(methodIndex >= LOW){
            methodIndex = convertMethodIndex(methodIndex);
            mGame.addScore(mScore, scoringMethod);
            mGame.addUsedMethod(methodIndex);

            //If the current round was the last, start the result activity, otherwise send result to play activity.
            if(mGame.getCurrentRound() == Game.NUMBER_OF_ROUNDS){
                Intent intent = ResultActivity.newIntent(ScoreActivity.this, mGame);
                startActivity(intent);
                finish();
            } else{
                mGame.nextRound();
                setResult();
            }
        } else {
            Toast.makeText(this, R.string.choose_method_toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends the result to the play activity, containing the updated game state.
     */
    private void setResult(){
        Intent intent = PlayActivity.newIntent(ScoreActivity.this, mGame);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Converts the spinner index to match the value of the scoring method.
     * @param position the selected position in the spinner.
     * @return the value of the corresponding scoring method.
     */
    private int convertMethodIndex(int position){
        return position-1;
    }

    /**
     * Returns a new intent containing the given game state.
     * @param packageContext the current context.
     * @param game the game state.
     * @return the intent containing the game state.
     */
    public static Intent newIntent(Context packageContext, Game game){
        Intent i = new Intent(packageContext, ScoreActivity.class);
        i.putExtra(EXTRA_GAME_SCORE, game);
        return i;
    }
}
