package ua.sergeimunovarov.tcalc.main.ops;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnit4.class)
public class ConverterTest {

    public static final String DAY_STRING = "d. ";

    @Test
    public void testToMillis() {
        assertThat(Converter.toMillis("1:0")).isEqualTo(3600000);
        assertThat(Converter.toMillis("1:0:0")).isEqualTo(3600000);
        assertThat(Converter.toMillis("0:0.500")).isEqualTo(500);
        assertThat(Converter.toMillis("0:0:0.500")).isEqualTo(500);
        assertThat(Converter.toMillis("0:0:0.005")).isEqualTo(5);
        assertThat(Converter.toMillis("0:0:0.050")).isEqualTo(50);
    }

    @Test
    public void testFormatDurationMs() {
        assertThat(Converter.formatDurationMs(500)).isEqualTo("00:00.500");
        assertThat(Converter.formatDurationMs(-500)).isEqualTo("-00:00.500");
        assertThat(Converter.formatDurationMs(3600000)).isEqualTo("60:00");
        assertThat(Converter.formatDurationMs(-3600000)).isEqualTo("-60:00");
    }

    @Test
    public void testFormatDurationHms() {
        assertThat(Converter.formatDurationHms(500)).isEqualTo("00:00:00.500");
        assertThat(Converter.formatDurationHms(-500)).isEqualTo("-00:00:00.500");
        assertThat(Converter.formatDurationHms(3600000)).isEqualTo("01:00:00");
        assertThat(Converter.formatDurationHms(-3600000)).isEqualTo("-01:00:00");
    }

    @Test
    public void testFormatDurationHmsMod() {
        assertThat(Converter.formatDurationHmsMod(-3600000))
                .isEqualTo("23:00:00");
        assertThat(Converter.formatDurationHmsMod(Converter.MILLIS_IN_DAY + 3600000))
                .isEqualTo("01:00:00");
    }

    @Test
    public void testFormatDurationDhms() {
        assertThat(Converter.formatDurationDhms(DAY_STRING, 500)).isEqualTo("0d. 00:00:00.500");
        assertThat(Converter.formatDurationDhms(DAY_STRING, -500)).isEqualTo("-0d. 00:00:00.500");
        assertThat(Converter.formatDurationDhms(DAY_STRING, 3600000)).isEqualTo("0d. 01:00:00");
        assertThat(Converter.formatDurationDhms(DAY_STRING, -3600000)).isEqualTo("-0d. 01:00:00");
    }


    @Test
    public void formatValue() {
        String value = Converter.formatValue(1.789, 1);
        assertThat(value).isNotEmpty().hasSize(3).isEqualTo("1.8");
    }


    @Test
    public void formatValue1() {
        String value = Converter.formatValue(1.6779, 2);
        assertThat(value).isNotEmpty().hasSize(4).isEqualTo("1.68");
    }
}
