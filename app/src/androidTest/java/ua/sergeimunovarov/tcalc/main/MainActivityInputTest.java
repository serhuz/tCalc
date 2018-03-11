package ua.sergeimunovarov.tcalc.main;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.pm.ActivityInfo;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.history.db.EntryDao;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onView;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityInputTest {

    @Rule
    public final DaggerMockRule mDaggerMockRule = new DaggerMockRule();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Mock
    ApplicationPreferences mApplicationPreferences;

    @Mock
    EntryDao mEntryDao;


    @Before
    public void setUp() {
        when(mApplicationPreferences.loadVibrationPreference()).thenReturn(false);
        when(mApplicationPreferences.loadVibrationDurationPreference()).thenReturn(ApplicationPreferences.Defaults.DEFAULT_DURATION);
        when(mApplicationPreferences.loadFormatPreference()).thenReturn(ApplicationPreferences.FormatConstants.HMS);

        ApplicationPreferences.ResultFormatPref formatPref = mock(ApplicationPreferences.ResultFormatPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<String> observer = invocation.getArgument(1);
            observer.onChanged("HH:MM:SS");
            return null;
        }).when(formatPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getResultFormat()).thenReturn(formatPref);

        when(mEntryDao.getAll()).thenReturn(new MutableLiveData<>());
    }


    @After
    public void tearDown() {
        reset(mApplicationPreferences);
    }


    @Test
    public void inputOldFormatPortrait() {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);

        ApplicationPreferences.LayoutPref layoutPref = mock(ApplicationPreferences.LayoutPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<Integer> observer = invocation.getArgument(1);
            observer.onChanged(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);
            return null;
        }).when(layoutPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getLayout()).thenReturn(layoutPref);

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
    public void inputOldFormatLandscape() {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);

        ApplicationPreferences.LayoutPref layoutPref = mock(ApplicationPreferences.LayoutPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<Integer> observer = invocation.getArgument(1);
            observer.onChanged(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);
            return null;
        }).when(layoutPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getLayout()).thenReturn(layoutPref);

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
    public void inputNewFormatPortrait() {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_NEW);

        ApplicationPreferences.LayoutPref layoutPref = mock(ApplicationPreferences.LayoutPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<Integer> observer = invocation.getArgument(1);
            observer.onChanged(ApplicationPreferences.LayoutConstants.LAYOUT_NEW);
            return null;
        }).when(layoutPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getLayout()).thenReturn(layoutPref);

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
    public void inputNewFormatLandscape() {
        when(mApplicationPreferences.loadLayoutPreference()).thenReturn(ApplicationPreferences.LayoutConstants.LAYOUT_NEW);

        ApplicationPreferences.LayoutPref layoutPref = mock(ApplicationPreferences.LayoutPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<Integer> observer = invocation.getArgument(1);
            observer.onChanged(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);
            return null;
        }).when(layoutPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getLayout()).thenReturn(layoutPref);

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
    public void filterInputInPortraitMode() {
        ApplicationPreferences.LayoutPref layoutPref = mock(ApplicationPreferences.LayoutPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<Integer> observer = invocation.getArgument(1);
            observer.onChanged(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);
            return null;
        }).when(layoutPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getLayout()).thenReturn(layoutPref);

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onEditText().withId(R.id.input).perform().typeText("abc123+1");

        onEditText().withId(R.id.input).check().matches(withText("123+1"));
    }


    @Test
    public void filterInputInLandscapeMode() {
        ApplicationPreferences.LayoutPref layoutPref = mock(ApplicationPreferences.LayoutPref.class);
        doAnswer((Answer<Void>) invocation -> {
            Observer<Integer> observer = invocation.getArgument(1);
            observer.onChanged(ApplicationPreferences.LayoutConstants.LAYOUT_OLD);
            return null;
        }).when(layoutPref).observe(any(LifecycleOwner.class), any(Observer.class));
        when(mApplicationPreferences.getLayout()).thenReturn(layoutPref);

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onEditText().withId(R.id.input).perform().typeText("abc123+1");

        onEditText().withId(R.id.input).check().matches(withText("123+1"));
    }
}
