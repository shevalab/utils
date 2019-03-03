package com.shevalab.utils.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextNumberComparatorTest {

    @Test
    public void textNumberComparatorTest() {
        String version1 = "0:20140213-0.3.git4164c23.el7";
        String version2 = "0:20140804-0.1.git6bce2b0.el7_0";
        int compResult = new TextNumberComparator().compare(version1, version2);
        assertEquals(-1, compResult);
    }
    @Test
    public void textNumberComparatorTestBigInteger() {
        String version1 = "0:2019070529012939802938128430983459082345809823458029.git4164c23.el7";
        String version2 = "0:2019050529012939802938128430983459082345809823458029-0.1.git6bce2b0.el7_0";
        int compResult = new TextNumberComparator().compare(version1, version2);
        assertEquals(1, compResult);
    }
}