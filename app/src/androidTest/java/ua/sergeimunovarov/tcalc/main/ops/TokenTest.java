package ua.sergeimunovarov.tcalc.main.ops;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class TokenTest {

    @Test
    public void equals() {
        Token first = Token.create(Type.VALUE, "-1");
        Token second = Token.create(Type.VALUE, "-1");

        assertThat(first).isEqualTo(second);
    }
}
