/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main;

import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onImageButton;
import static cortado.Cortado.onView;
import static ua.sergeimunovarov.tcalc.CustomMatchers.hasCurrentText;


@RunWith(AndroidJUnit4.class)
public class MainActivityStateTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void dataSurvivesScreenRotation() {
        String expression = "2+2";
        onView().withId(R.id.input).perform().typeText(expression);
        onView().withId(R.id.btn_eq).perform().click();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onEditText().withId(R.id.input).check().matches(withText(expression));
        onView().withId(R.id.result).check().matches(hasCurrentText("= 4"));
    }


    @Test
    public void outputFormatSurvivesScreenRotation() {
        onView().withId(R.id.indicator_format).perform().click();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView().withText(R.string.title_dialog_format).check().matches(isDisplayed());
    }


    @Test
    public void insertCurrentTimeSurvivesScreenRotation() {
        onImageButton().withId(R.id.btn_menu).perform().click();
        onView().withText(R.string.insert_current_time).perform().click();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView().withText(R.string.select_time_format).check().matches(isDisplayed());
    }
}
