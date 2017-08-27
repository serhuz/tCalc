package ua.sergeimunovarov.tcalc.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onView;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void performEvaluation() throws Exception {
        onView().withId(R.id.input).perform().typeText("2+2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 4"));
    }
}
