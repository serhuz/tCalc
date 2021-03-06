/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.content.pm.ActivityInfo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ua.sergeimunovarov.tcalc.R;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onButton;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onImageButton;
import static cortado.Cortado.onTextView;
import static cortado.Cortado.onView;
import static ua.sergeimunovarov.tcalc.CustomMatchers.hasCurrentText;
import static ua.sergeimunovarov.tcalc.CustomViewActions.setText;


@RunWith(AndroidJUnit4.class)
public class MainActivityStateTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void dataSurvivesScreenRotation() {
        String expression = "2+2";
        onEditText().withId(R.id.input).perform(setText(expression));
        onButton().withId(R.id.btn_eq).perform().click();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onEditText().withId(R.id.input).check().matches(withText(expression));
        onView().withId(R.id.result).check().matches(hasCurrentText("= 4"));
    }


    @Test
    public void outputFormatSurvivesScreenRotation() {
        onView().withId(R.id.indicator_format).perform().click();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onTextView().withText(R.string.title_dialog_format).check().matches(isDisplayed());
    }


    @Test
    public void insertCurrentTimeSurvivesScreenRotation() {
        onImageButton().withId(R.id.btn_menu).perform().click();
        onTextView().withText(R.string.insert_current_time).perform().click();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onTextView().withText(R.string.select_time_format).check().matches(isDisplayed());
    }
}
