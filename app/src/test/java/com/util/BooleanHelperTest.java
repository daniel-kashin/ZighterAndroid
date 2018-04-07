package com.util;

import com.zighter.zighterandroid.util.BooleanHelper;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BooleanHelperTest {

    @Test
    public void toBoolean_true() {
        int trueInt = 1124;
        assertEquals(BooleanHelper.toBoolean(trueInt), true);
    }

    @Test
    public void toBoolean_false() {
        int falseInt = 0;
        assertEquals(BooleanHelper.toBoolean(falseInt), false);
    }

    @Test
    public void fromBoolean_true() {
        assertEquals(BooleanHelper.toInt(true), 1);
    }

    @Test
    public void fromBoolean_false() {
        assertEquals(BooleanHelper.toInt(false), 0);
    }

    @Test
    public void toString_toBoolean_true() {
        assertEquals(BooleanHelper.toBoolean(BooleanHelper.toString(true)), true);
    }

    @Test
    public void toString_toBoolean_false() {
        assertEquals(BooleanHelper.toBoolean(BooleanHelper.toString(false)), false);
    }
}
