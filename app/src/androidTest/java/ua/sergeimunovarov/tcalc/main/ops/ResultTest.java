package ua.sergeimunovarov.tcalc.main.ops;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class ResultTest {

    @Test
    public void equals() {
        Result first = Result.create(Result.ResultType.RESULT_OK_VALUE, "2", "1+1");
        Result second = Result.create(Result.ResultType.RESULT_OK_VALUE, "2", "1+1");

        assertThat(first).isEqualTo(second);
    }


    @Test
    public void parcel() {
        Result expected = Result.create(Result.ResultType.RESULT_OK_VALUE, "1", "0+1");

        Parcel parcel = Parcel.obtain();
        expected.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Result actual = AutoValue_Result.CREATOR.createFromParcel(parcel);

        assertThat(actual).isEqualTo(expected);
    }
}
