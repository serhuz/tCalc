package ua.sergeimunovarov.tcalc.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.cosenonjaviste.daggermock.InjectFromComponent;
import ua.sergeimunovarov.tcalc.ApplicationPreferences;
import ua.sergeimunovarov.tcalc.DaggerMockRule;
import ua.sergeimunovarov.tcalc.R;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cortado.Cortado.onButton;
import static cortado.Cortado.onEditText;
import static cortado.Cortado.onTextView;
import static cortado.Cortado.onView;


@RunWith(AndroidJUnit4.class)
public class MainActivityEvaluationTest {

    @Rule
    public final DaggerMockRule daggerMockRule = new DaggerMockRule();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @SuppressWarnings("WeakerAccess")
    @InjectFromComponent
    ApplicationPreferences mApplicationPreferences;


    @Before
    public void setUp() throws Exception {
        mApplicationPreferences.getPreferences().edit().clear().apply();
    }


    @Test
    public void performEvaluation1() throws Exception {
        onView().withId(R.id.input).perform().typeText("2+2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 4"));
    }


    @Test
    public void performEvaluation2() throws Exception {
        onView().withId(R.id.input).perform().typeText("2+2*2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 6"));
    }


    @Test
    public void performEvaluation3() throws Exception {
        onView().withId(R.id.input).perform().typeText("(2+2)*2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 8"));
    }


    @Test
    public void performEvaluation4() throws Exception {
        onView().withId(R.id.input).perform().typeText("2/0");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("Error (/0)"));
    }


    @Test
    public void performEvaluation5() throws Exception {
        onView().withId(R.id.input).perform().typeText("2*0");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 0"));
    }


    @Test
    public void performEvaluation6() throws Exception {
        onView().withId(R.id.input).perform().typeText("2*(-1)");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= -2"));
    }


    @Test
    public void performEvaluation7() throws Exception {
        onView().withId(R.id.input).perform().typeText("2/(-1)");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= -2"));
    }


    @Test
    public void performEvaluation8() throws Exception {
        onView().withId(R.id.input).perform().typeText("(-1)/2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= -0.5"));
    }


    @Test
    public void performEvaluation9() throws Exception {
        onView().withId(R.id.input).perform().typeText("(-1)/2+0.5");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 0"));
    }


    @Test
    public void performEvaluation10() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:10*2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 00:20:00"));
    }


    @Test
    public void performEvaluation11() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:10/2");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 00:05:00"));
    }


    @Test
    public void performEvaluation12() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:10*2+0:05");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 00:25:00"));
    }


    @Test
    public void performEvaluation13() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:10*2+4*0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 01:00:00"));
    }


    @Test
    public void performEvaluation14() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:10-0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 00:00:00"));
    }


    @Test
    public void performEvaluation15() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:10-0:20");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= -00:10:00"));
    }


    @Test
    public void performEvaluation16() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:20/0:20");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 1"));
    }


    @Test
    public void performEvaluation17() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:20/0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("= 2"));
    }


    @Test
    public void performEvaluation18() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:20*0:10");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("Error (T*T)"));
    }


    @Test
    public void performEvaluation19() throws Exception {
        onView().withId(R.id.input).perform().typeText("0:20+1");
        onView().withId(R.id.btn_eq).perform().click();
        onView().withId(R.id.result).check().matches(withText("Error (T+V)"));
    }


    @Test
    public void preformEvaluation20() throws Exception {
        onEditText().withId(R.id.input).perform().typeText("0:00:10/3");
        onButton().withId(R.id.btn_eq).perform().click();
        onTextView().withId(R.id.result).check().matches(withText("= 00:00:03.333"));
    }


    @Test
    public void preformEvaluation21() throws Exception {
        onEditText().withId(R.id.input).perform().typeText("0:00:9/3");
        onButton().withId(R.id.btn_eq).perform().click();
        onTextView().withId(R.id.result).check().matches(withText("= 00:00:03"));
    }
}
