package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnit4.class)
public class ConverterTest {

    public static final String DAY_STRING = "d. ";

    @Test
    public void testToMillis() throws Exception {
        assertThat(Converter.toMillis("1:0")).isEqualTo(3600000);
        assertThat(Converter.toMillis("1:0:0")).isEqualTo(3600000);
        assertThat(Converter.toMillis("0:0.500")).isEqualTo(500);
        assertThat(Converter.toMillis("0:0:0.500")).isEqualTo(500);
        assertThat(Converter.toMillis("0:0:0.005")).isEqualTo(5);
        assertThat(Converter.toMillis("0:0:0.050")).isEqualTo(50);
    }

    @Test
    public void testFormatDurationMs() throws Exception {
        assertThat(Converter.formatDurationMs(500)).isEqualTo("00:00.500");
        assertThat(Converter.formatDurationMs(-500)).isEqualTo("-00:00.500");
        assertThat(Converter.formatDurationMs(3600000)).isEqualTo("60:00");
        assertThat(Converter.formatDurationMs(-3600000)).isEqualTo("-60:00");
    }

    @Test
    public void testFormatDurationHms() throws Exception {
        assertThat(Converter.formatDurationHms(500)).isEqualTo("00:00:00.500");
        assertThat(Converter.formatDurationHms(-500)).isEqualTo("-00:00:00.500");
        assertThat(Converter.formatDurationHms(3600000)).isEqualTo("01:00:00");
        assertThat(Converter.formatDurationHms(-3600000)).isEqualTo("-01:00:00");
    }

    @Test
    public void testFormatDurationHmsMod() throws Exception {
        assertThat(Converter.formatDurationHmsMod(-3600000))
                .isEqualTo("23:00:00");
        assertThat(Converter.formatDurationHmsMod(Converter.MILLIS_IN_DAY + 3600000))
                .isEqualTo("01:00:00");
    }

    @Test
    public void testFormatDurationDhms() throws Exception {
        assertThat(Converter.formatDurationDhms(DAY_STRING, 500)).isEqualTo("0d. 00:00:00.500");
        assertThat(Converter.formatDurationDhms(DAY_STRING, -500)).isEqualTo("-0d. 00:00:00.500");
        assertThat(Converter.formatDurationDhms(DAY_STRING, 3600000)).isEqualTo("0d. 01:00:00");
        assertThat(Converter.formatDurationDhms(DAY_STRING, -3600000)).isEqualTo("-0d. 01:00:00");
    }
}
