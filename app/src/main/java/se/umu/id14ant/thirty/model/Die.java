package se.umu.id14ant.thirty.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;


/**
 * Representing a die that can be thrown during the game.
 * @author Anna Nystedt.
 */
public class Die implements Parcelable {

    private String mId;
    private boolean mIsSaved;
    private int mValue;
    private Random mRandom = new Random();

    /**
     * Create a new die instance with random value.
     */
    public Die(){
        roll();
        mIsSaved = false;
    }

    /**
     * Sets the id of the die. The id should match the id of a dice in the layout.
     * @param id the id.
     */
    public void setId(String id){
        mId = id;
    }

    /**
     * Returns the id of the die.
     * @return the id.
     */
    public String getId(){
        return mId;
    }

    /**
     * Returns the current state of the die.
     * @return true if the die i saved, otherwise false.
     */
    public boolean isSaved(){
        return mIsSaved;
    }

    /**
     * Changes the state of the die to the opposite of its current state.
     */
    public void toggleSaved(){
        mIsSaved = !mIsSaved;
    }

    /**
     * Rolls the die to change its value.
     */
    public void roll(){
        mValue = mRandom.nextInt(6)+1;
    }

    /**
     * Returns the value of the die.
     * @return the value.
     */
    public int getValue(){
        return mValue;
    }

    /**
     * {@inheritDoc}
     *
     * Auto-generated by Android Studio.
     */
    protected Die(Parcel in) {
        mId = in.readString();
        mIsSaved = in.readByte() != 0;
        mValue = in.readInt();
        mRandom = new Random();
    }

    /**
     * {@inheritDoc}
     *
     * Auto-generated by Android Studio.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeByte((byte) (mIsSaved ? 1 : 0));
        dest.writeInt(mValue);
    }

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
     * Auto-generated class by Android Studio.
     */
    public static final Creator<Die> CREATOR = new Creator<Die>() {
        @Override
        public Die createFromParcel(Parcel in) {
            return new Die(in);
        }

        @Override
        public Die[] newArray(int size) {
            return new Die[size];
        }
    };
}
