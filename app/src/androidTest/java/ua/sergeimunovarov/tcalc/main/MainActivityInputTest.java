package ua.sergeimunovarov.tcalc.main;

import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onView;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class MainActivityInputTest {

    @Rule
    public final DaggerMockRule mDaggerMockRule = new DaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Mock
    ApplicationPreferences mApplicationPreferences;


    @Before
    public void setUp() throws Exception {
        when(mApplicationPreferences.loadVibrationPreference()).thenReturn(false);
        when(mApplicationPreferences.loadVibrationDurationPreference()).thenReturn(ApplicationPreferences.Defaults.DEFAULT_DURATION);
        when(mApplicationPreferences.loadFormatPreference()).thenReturn(ApplicationPreferences.FormatConstants.HMS);
    }


    @After
    public void tearDown() throws Exception {
        reset(mApplicationPreferences);
    }


    @Test
    public void inputOldFormatPortrait() throws Exception {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView().withId(R.id.btn_0).perform(click());
        onView().withId(R.id.btn_1).perform(click());
        onView().withId(R.id.btn_2).perform(click());
        onView().withId(R.id.btn_3).perform(click());
        onView().withId(R.id.btn_4).perform(click());
        onView().withId(R.id.btn_5).perform(click());
        onView().withId(R.id.btn_6).perform(click());
        onView().withId(R.id.btn_7).perform(click());
        onView().withId(R.id.btn_8).perform(click());
        onView().withId(R.id.btn_9).perform(click());
        onView().withId(R.id.btn_dot).perform(click());
        onView().withId(R.id.btn_plus).perform(click());
        onView().withId(R.id.btn_minus).perform(click());
        onView().withId(R.id.btn_multiply).perform(click());
        onView().withId(R.id.btn_divide).perform(click());
        onView().withId(R.id.btn_par).perform(click());

        onView().withId(R.id.input).check().matches(withText("0123456789.+-*/()"));
    }


    @Test
    public void inputOldFormatLandscape() throws Exception {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView().withId(R.id.btn_0).perform(click());
        onView().withId(R.id.btn_1).perform(click());
        onView().withId(R.id.btn_2).perform(click());
        onView().withId(R.id.btn_3).perform(click());
        onView().withId(R.id.btn_4).perform(click());
        onView().withId(R.id.btn_5).perform(click());
        onView().withId(R.id.btn_6).perform(click());
        onView().withId(R.id.btn_7).perform(click());
        onView().withId(R.id.btn_8).perform(click());
        onView().withId(R.id.btn_9).perform(click());
        onView().withId(R.id.btn_dot).perform(click());
        onView().withId(R.id.btn_plus).perform(click());
        onView().withId(R.id.btn_minus).perform(click());
        onView().withId(R.id.btn_multiply).perform(click());
        onView().withId(R.id.btn_divide).perform(click());
        onView().withId(R.id.btn_par1).perform(click());
        onView().withId(R.id.btn_par2).perform(click());

        onView().withId(R.id.input).check().matches(withText("0123456789.+-*/()"));
    }


    @Test
    public void inputNewFormatPortrait() throws Exception {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_NEW);

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView().withId(R.id.btn_0).perform(click());
        onView().withId(R.id.btn_1).perform(click());
        onView().withId(R.id.btn_2).perform(click());
        onView().withId(R.id.btn_3).perform(click());
        onView().withId(R.id.btn_4).perform(click());
        onView().withId(R.id.btn_5).perform(click());
        onView().withId(R.id.btn_6).perform(click());
        onView().withId(R.id.btn_7).perform(click());
        onView().withId(R.id.btn_8).perform(click());
        onView().withId(R.id.btn_9).perform(click());
        onView().withId(R.id.btn_dot).perform(click());
        onView().withId(R.id.btn_plus).perform(click());
        onView().withId(R.id.btn_minus).perform(click());
        onView().withId(R.id.btn_multiply).perform(click());
        onView().withId(R.id.btn_divide).perform(click());
        onView().withId(R.id.btn_par).perform(click());

        onView().withId(R.id.input).check().matches(withText("0123456789.+-*/()"));
    }


    @Test
    public void inputNewFormatLandscape() throws Exception {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_NEW);

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView().withId(R.id.btn_0).perform(click());
        onView().withId(R.id.btn_1).perform(click());
        onView().withId(R.id.btn_2).perform(click());
        onView().withId(R.id.btn_3).perform(click());
        onView().withId(R.id.btn_4).perform(click());
        onView().withId(R.id.btn_5).perform(click());
        onView().withId(R.id.btn_6).perform(click());
        onView().withId(R.id.btn_7).perform(click());
        onView().withId(R.id.btn_8).perform(click());
        onView().withId(R.id.btn_9).perform(click());
        onView().withId(R.id.btn_dot).perform(click());
        onView().withId(R.id.btn_plus).perform(click());
        onView().withId(R.id.btn_minus).perform(click());
        onView().withId(R.id.btn_multiply).perform(click());
        onView().withId(R.id.btn_divide).perform(click());
        onView().withId(R.id.btn_par1).perform(click());
        onView().withId(R.id.btn_par2).perform(click());

        onView().withId(R.id.input).check().matches(withText("0123456789.+-*/()"));
    }


    @Test
    public void filterInputInPortraitMode() throws Exception {
        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onEditText().withId(R.id.input).perform().typeText("abc123+1");

        onEditText().withId(R.id.input).check().matches(withText("123+1"));
    }


    @Test
    public void filterInputInLandscapeMode() throws Exception {
        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onEditText().withId(R.id.input).perform().typeText("abc123+1");

        onEditText().withId(R.id.input).check().matches(withText("123+1"));
    }
}
