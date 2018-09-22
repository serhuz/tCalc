/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import org.hamcrest.Matcher;

import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;


public final class CustomViewActions {

    private CustomViewActions() {
        throw new AssertionError();
    }


    public static ViewAction setText(String text) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(EditText.class));
            }


            @Override
            public String getDescription() {
                return "Set text to edit text";
            }


            @Override
            public void perform(UiController uiController, View view) {
                ((EditText) view).setText(text);
            }
        };
    }


    public static ViewAction setProgress(int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(SeekBar.class);
            }


            @Override
            public String getDescription() {
                return "Set SeekBar progress";
            }


            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
            }
        };
    }


    public static ViewAction swipeSeekBar(int progress) { // https://stackoverflow.com/a/39827232
        return actionWithAssertions(new GeneralSwipeAction(
                Swipe.SLOW,
                new SeekBarThumbCoordinatesProvider(0),
                new SeekBarThumbCoordinatesProvider(progress),
                Press.PINPOINT));
    }


    private static class SeekBarThumbCoordinatesProvider implements CoordinatesProvider {

        private final int mProgress;


        public SeekBarThumbCoordinatesProvider(int progress) {
            mProgress = progress;
        }


        @Override
        public float[] calculateCoordinates(View view) {
            if (!(view instanceof SeekBar)) {
                throw new PerformException.Builder()
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new RuntimeException("SeekBar expected")).build();
            }
            SeekBar seekBar = (SeekBar) view;
            int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
            double progress = mProgress == 0 ? seekBar.getProgress() : mProgress;
            int xPosition = (int) (seekBar.getPaddingLeft() + width * progress / seekBar.getMax());
            float[] xy = getVisibleLeftTop(seekBar);
            return new float[]{xy[0] + xPosition, xy[1] + 10};
        }


        private static float[] getVisibleLeftTop(View view) {
            int[] xy = new int[2];
            view.getLocationOnScreen(xy);
            return new float[]{(float) xy[0], (float) xy[1]};
        }
    }
}
