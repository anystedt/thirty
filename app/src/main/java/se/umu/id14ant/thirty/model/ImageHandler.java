package se.umu.id14ant.thirty.model;

import android.view.View;
import android.widget.ImageButton;

/**
 * Handles the displaying of the dices in the view.
 * @author Anna Nystedt.
 */
public class ImageHandler {

    /**
     * Sets the correct image of the given die, so the image correspond with the value and the
     * current state.
     * @param view the current view.
     * @param die the die.
     */
    public static void setDieImage(View view, Die die){
        ImageButton button = getImageButton(view, die.getId());
        if(die.isSaved()){
            button.setImageResource(getFaceImage(view, "grey", die.getValue()));
        } else{
            button.setImageResource(getFaceImage(view, "white", die.getValue()));
        }
    }

    /**
     * Returns the button with the corresponding with the given id.
     * @param view the current view.
     * @param id the id of the button.
     * @return the corresponding button.
     */
    private static ImageButton getImageButton(View view, String id){
        int buttonId = view.getResources().getIdentifier(id, "id", "se.umu.id14ant.thirty");
        return view.findViewById(buttonId);
    }

    /**
     * Returns the correct image corresponding with the given die value.
     * @param view the current view.
     * @param color the color of the image.
     * @param value the value of the die.
     * @return the corresponding image.
     */
    private static int getFaceImage(View view, String color, int value){
        return view.getResources().getIdentifier(color+value, "drawable", "se.umu.id14ant.thirty");
    }
}
