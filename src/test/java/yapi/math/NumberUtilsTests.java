package yapi.math;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;

public class NumberUtilsTests {

    @Test
    public void roundTest() {
        double toRound = 0.9;
        int digits = 0;
        double result = NumberUtils.round(toRound, digits);

        assertThat(result, is(equalTo(1.0)));
    }

    private void roundTest(double d, int digits) {

    }

    @Test
    public void roundDownTest() {
        double toRound = 0.4;
        int digits = 0;
        double result = NumberUtils.round(toRound, digits);

        assertThat(result, is(equalTo(0.0)));
    }

}
