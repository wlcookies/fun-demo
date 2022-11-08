package com.wlcookies.commonmodule.utils;

import junit.framework.TestCase;

import org.junit.After;

public class SafetyUtilsTest extends TestCase {

    public void testGetString() {

        assertEquals(SafetyUtils.getString(null), "");
        assertEquals(SafetyUtils.getString("123"), "123");

        assertEquals(SafetyUtils.getString(null,"321"), "321");
        assertEquals(SafetyUtils.getString("456","321"), "456");

    }
}