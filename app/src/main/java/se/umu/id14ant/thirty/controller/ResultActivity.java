package se.umu.id14ant.thirty.controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.umu.id14ant.thirty.R;
import se.umu.id14ant.thirty.model.Score;
import se.umu.id14ant.thirty.model.Game;

/**
 * Activity that displays the result activity.
 */
public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_GAME_RESULT = "se.umu.id14ant.thirty.result";

    /**
     * Creates the result screen and initializes the necessary attributes.
     * @param savedInstanceState instance state saved before destruction of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Gets the game state from the given intent.
        Game game = getIntent().getParcelableExtra(EXTRA_GAME_RESULT);

        //Initializes the recycler view and adapter used for displaying the result for each round.
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ScoreAdapter adapter = new ScoreAdapter(game.getScores());
        RecyclerView.LayoutManager mLayoutManager;

        //If the device has portrait orientation use one column, otherwise use two.
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        } else{
            mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        TextView textView = findViewById(R.id.total_score);
        textView.setText(String.valueOf(game.getResult()));
    }

    /**
     * Returns a new intent containing the given game state. Used by other activities to create
     * intents before starting this activity.
     * @param packageContext the current context.
     * @param game the game state.
     * @return the intent containing the game state.
     */
    public static Intent newIntent(Context packageContext, Game game){
        Intent i = new Intent(packageContext, ResultActivity.class);
        i.putExtra(EXTRA_GAME_RESULT, game);
        return i;
    }

    /**
     * Inner class used for holding the information of a score item.
     */
    private class ScoreHolder extends RecyclerView.ViewHolder {
        private TextView textViewRound;
        private TextView textViewMethod;
        private TextView textViewScore;

        /**
         * Creates a instance that is connected to the correct widgets in the view.
         * @param view
         */
        public ScoreHolder(View view) {
            super(view);
            textViewRound = view.findViewById(R.id.round_title);
            textViewMethod = view.findViewById(R.id.result_method);
            textViewScore = view.findViewById(R.id.round_score);
        }

        /**
         * Initializes the widgets with the correct value from the score item.
         * @param score
         */
        public void bind(Score score){
            textViewRound.setText(String.format(getResources().getString(R.string.current_round), score.getRound()));
            textViewMethod.setText(score.getScoringMethod());
            textViewScore.setText(String.valueOf(score.getScore()));
        }
    }

    /**
     * Inner class used for handling the list of scores.
     */
    private class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder>{
        private List<Score> mScores;

        /**
         * Creates a new instance.
         * @param scores the given list containing the scores.
         */
        public ScoreAdapter(List<Score> scores) {
            mScores = scores;
        }

        /**
         * Creates a new score holder.
         * @param parent the current parent.
         * @param viewType the view type
         * @return the new score holder.
         */
        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.result_item, parent, false);

            return new ScoreHolder(itemView);
        }

        /**
         * Initializes the score holders information based on the given score.
         * @param holder the given score holder.
         * @param position the position of the score to retrieve.
         */
        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Score score = mScores.get(position);
            holder.bind(score);
        }

        /**
         * Returns the number of scores in the list.
         * @return the number of scores.
         */
        @Override
        public int getItemCount() {
            return mScores.size();
        }
    }
}
