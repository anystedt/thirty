package se.umu.id14ant.thirty.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representing the state of the game, containing all important information about the current game.
 * @author Anna Nystedt.
 */
public class Game implements Parcelable {

    private static final int NUMBER_OF_DICES = 6;
    private static final int NUMBER_OF_THROWS = 3;
    public static final int NUMBER_OF_ROUNDS = 10;

    private int mCurrentRound;
    private int mCurrentScore;
    private int mThrowsLeft;

    private Die[] mDices;
    private List<Score> mScores;
    private boolean[] mUsedSumMethods;

    /**
     * Creates a game state and initializes the necessary attributes.
     */
    public Game(){
        mCurrentRound = 1;
        mThrowsLeft = NUMBER_OF_THROWS-1;

        mUsedSumMethods = new boolean[NUMBER_OF_ROUNDS];
        mScores = new ArrayList<>();
        mDices = new Die[NUMBER_OF_DICES];

        for(int i = 0; i<NUMBER_OF_DICES; i++){
            mDices[i] = new Die();
            mDices[i].setId("dice"+(i+1));
        }
    }

    /**
     * Returns the list of dices.
     * @return the list of dices.
     */
    public Die[] getDices(){
        return mDices;
    }

    /**
     * Returns true if there are throws left, otherwise returns false. If there are throws left all
     * dices that is not saved get rolled.
     * @return true if the dices were rolled, otherwise false.
     */
    public boolean rollDices(){
        if(mThrowsLeft > 0){
            for (Die die: mDices) {
                //Continue to next if the die is saved.
                if(die.isSaved()){
                    continue;
                }
                die.roll();
            }

            mThrowsLeft -= 1;
            return true;
        } else{
            return false;
        }

    }

    /**
     * Increments the current round and reset number of throws.
     */
    public void nextRound(){
        mCurrentRound++;
        mThrowsLeft = NUMBER_OF_THROWS;
    }

    /**
     * Returns the number of the current round.
     * @return the current round.
     */
    public int getCurrentRound(){
        return mCurrentRound;
    }

    /**
     * Adds a score to the list of scores.
     * @param score the score to be added.
     * @param scoringMethod the method used for calculating score.
     */
    public void addScore(int score, String scoringMethod){
        mScores.add(new Score(mCurrentRound, score, scoringMethod));
    }

    /**
     * Sets the value of the given index to true, to show that the method has been used.
     * @param i the index of the used method.
     */
    public void addUsedMethod(int i){
        mUsedSumMethods[i] = true;
    }

    /**
     * Returns true if the given method is used, otherwise returns false.
     * @param i the given index.
     * @return true if the method is used, otherwise false.
     */
    public boolean methodIsUsed(int i){
        return mUsedSumMethods[i];
    }

    /**
     * Returns the number of throws left on the current round.
     * @return the number of throws left.
     */
    public int getThrowsLeft(){
        return mThrowsLeft;
    }

    /**
     * Returns the list containing all scores.
     * @return the list with scores.
     */
    public List<Score> getScores(){
        return mScores;
    }

    /**
     * Returns the total sum of all scores.
     * @return the total sum.
     */
    public int getResult(){
        int sum = 0;
        for (Score score: mScores) {
            sum+=score.getScore();
        }
        return sum;
    }

    /**
     * Calculates and returns the score for the scoring method low.
     * @return the score.
     */
    public int calculateLowScore(){
        int score = 0;
        for (Die die: mDices) {
            if(die.getValue() <= 3){
                score += die.getValue();
            }
        }
        return score;
    }

    /**
     * Calculates and returns the score for all scoring methods (excl. Low)
     * @param chosenMethod the chosen scoring method.
     * @return the score.
     */
    public int calculateScore(int chosenMethod){
        mCurrentScore = 0;

        //Make a copy of the current list of dices and create a empty list used as a trashcan.
        List<Die> tempDices = new ArrayList<>(Arrays.asList(mDices));
        List<Die> tempRemove = new ArrayList<>();

        //Sum and remove all dices that matches the chosen method or has a value greater than the chosen method.
        for (Die die:tempDices){
            int value = die.getValue();
            if(value == chosenMethod){
                mCurrentScore += value;
                tempRemove.add(die);
            } else if(value > chosenMethod){
                tempRemove.add(die);
            }
        }

        //Remove the dices placed in the list of items to remove.
        tempDices.removeAll(tempRemove);
        tempRemove.clear();

        //Sum and remove all pair of dices that matches the chosen method.
        for(int i = 0; i < tempDices.size()-1; i++){
            for(int j = i+1; j < tempDices.size(); j++ ){
                int[] diceNumbers = {i, j};
                //If the dices already has been used, skip.
                if(dicesHasBeenUsed(tempRemove, tempDices, diceNumbers)){
                    continue;
                }
                //Calculate the score of the dices, and possibly update remove-list with used dices.
                tempRemove = calculateScoreOfDices(tempRemove, tempDices, diceNumbers , chosenMethod);
            }
        }

        //Remove the used dices.
        tempDices.removeAll(tempRemove);
        tempRemove.clear();

        //Sum and remove all triplet of mDices that matches the chosen method.
        for(int i = 0; i < tempDices.size()-2; i++){
            for(int j = i+1; j < tempDices.size()-1; j++){
                for(int k = j+1; k < tempDices.size(); k++){
                    int[] diceNumbers = {i, j, k};
                    if(dicesHasBeenUsed(tempRemove, tempDices, diceNumbers)){
                        continue;
                    }
                    tempRemove = calculateScoreOfDices(tempRemove, tempDices, diceNumbers , chosenMethod);
                }
            }
        }

        tempDices.removeAll(tempRemove);
        tempRemove.clear();

        //Sum and remove all quadruplet of mDices that matches the chosen method.
        for(int i = 0; i < tempDices.size()-3; i++){
            for(int j = i+1; j < tempDices.size()-2; j++){
                for(int k = j+1; k <tempDices.size()-1; k++){
                    for(int l = k+1; l < tempDices.size(); l++){

                        int[] diceNumbers = {i, j, k, l};
                        if(dicesHasBeenUsed(tempRemove, tempDices, diceNumbers)){
                            continue;
                        }
                        tempRemove = calculateScoreOfDices(tempRemove, tempDices, diceNumbers , chosenMethod);
                    }
                }
            }
        }

        tempDices.removeAll(tempRemove);
        tempRemove.clear();

        //Sum and remove all quint of mDices that matches the chosen method.
        for(int i = 0; i < tempDices.size()-4; i++){
            for(int j = i+1; j < tempDices.size()-3; j++){
                for(int k = j+1; k <tempDices.size()-2; k++){
                    for(int l = k+1; l < tempDices.size()-1; l++){
                        for(int m = l+1; m < tempDices.size(); m++){

                            int[] diceNumbers = {i, j, k, l, m};
                            if(dicesHasBeenUsed(tempRemove, tempDices, diceNumbers)){
                                continue;
                            }
                            tempRemove = calculateScoreOfDices(tempRemove, tempDices, diceNumbers , chosenMethod);
                        }

                    }
                }
            }
        }

        tempDices.removeAll(tempRemove);
        tempRemove.clear();

        //If all dices are left, try and sum all of them to reach the given sum.
        if(tempDices.size() == NUMBER_OF_DICES){
            int value = 0;
            for (Die die: tempDices) {
                value += die.getValue();
            }
            if(value == chosenMethod){
                mCurrentScore += value;
            }
        }

        return mCurrentScore;
    }

    /**
     * Calculates the score of the given dices and updates current score. Returns an updated list containing dices to
     * remove from the temporary list.
     * @param tempRemove list containing the used dices that should be removed.
     * @param tempDices list containing all unused dices.
     * @param diceNumbers list containing indexes of the dices to use.
     * @param chosenMethod the chosen scoring method.
     * @return an updated list of the dices that should be removed.
     */
    private List<Die> calculateScoreOfDices(List<Die> tempRemove, List<Die> tempDices, int[] diceNumbers, int chosenMethod){
        int value = getSum(tempDices, diceNumbers);
        if(value == chosenMethod){
            mCurrentScore += value;
            tempRemove = addToRemoveList(tempRemove,tempDices, diceNumbers);
        }

        return tempRemove;
    }

    /**
     * Returns true if no of the given dices is used, otherwise returns false.
     * @param usedDices a list containing the used dices.
     * @param dices a list of all the dices.
     * @param diceNumbers a list of the indexes of the current dices.
     * @return true of no dice is used, otherwise false.
     */
    private boolean dicesHasBeenUsed(List<Die> usedDices, List<Die> dices, int[] diceNumbers){
        for (int i:diceNumbers) {
            if(usedDices.contains(dices.get(i))){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the sum of the given dices.
     * @param tempDices a list containing all unused dices.
     * @param diceNumbers a list containing indexes of the dices to calculate.
     * @return the sum of the given dices values.
     */
    private int getSum(List<Die> tempDices, int[] diceNumbers){
        int value = 0;
        for (int i: diceNumbers) {
            value += tempDices.get(i).getValue();
        }

        return value;
    }

    /**
     * Adds the given dices to the list of used dices.
     * @param tempRemove a list containing the used dices.
     * @param tempDices a list containing all unused dices.
     * @param diceNumbers a list containing the indexes of the dices to mark as used.
     * @return the updated list of used dices.
     */
    private List<Die> addToRemoveList(List<Die> tempRemove, List<Die> tempDices, int[] diceNumbers){
        for (int i: diceNumbers) {
            tempRemove.add(tempDices.get(i));
        }

        return tempRemove;
    }

    /**
     * {@inheritDoc}
     *
     * Auto-generated by Android Studio.
     */
    protected Game(Parcel in) {
        mCurrentRound = in.readInt();
        mCurrentScore = in.readInt();
        mThrowsLeft = in.readInt();
        mDices = in.createTypedArray(Die.CREATOR);
        mScores = in.createTypedArrayList(Score.CREATOR);
        mUsedSumMethods = in.createBooleanArray();
    }

    /**
     * {@inheritDoc}
     *
     * Auto-generated by Android Studio.
     */
    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    /**
     * {@inheritDoc}
     *
     * Auto-generated by Android Studio.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * Auto-generated by Android Studio.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCurrentRound);
        dest.writeInt(mCurrentScore);
        dest.writeInt(mThrowsLeft);
        dest.writeTypedArray(mDices, flags);
        dest.writeTypedList(mScores);
        dest.writeBooleanArray(mUsedSumMethods);
    }
}
