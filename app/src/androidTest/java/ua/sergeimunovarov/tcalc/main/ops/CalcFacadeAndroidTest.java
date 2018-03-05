package ua.sergeimunovarov.tcalc.main.ops;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ua.sergeimunovarov.tcalc.ApplicationPreferences;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class CalcFacadeAndroidTest {

    @Test
    public void testCalculateExpression1() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("0:10+0:10");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK);
        assertThat(result.value()).isEqualTo("00:20:00");
    }


    @Test
    public void testCalculateExpression2() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("2+2");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("4");
    }


    @Test
    public void testCalculateExpression3() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("2*2");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("4");
    }


    @Test
    public void testCalculateExpression4() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("2/2");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("1");
    }


    @Test
    public void testCalculateExpression5() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("2.5*2");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("5");
    }


    @Test
    public void testCalculateExpression6() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("2");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("2");
    }


    @Test
    public void testCalculateExpression7() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("2-2");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("0");
    }


    @Test
    public void testCalculateExpression8() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("(2+2)*0.5");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("2");
    }


    @Test
    public void testCalculateExpression9() {
        CalcFacade facade = new CalcFacade(ApplicationPreferences.FormatConstants.HMS);
        Result result = facade.calculateResult("(2+2)+(0.5*2)");
        assertThat(result.type()).isEqualTo(Result.ResultType.RESULT_OK_VALUE);
        assertThat(result.value()).isEqualTo("5");
    }
}
