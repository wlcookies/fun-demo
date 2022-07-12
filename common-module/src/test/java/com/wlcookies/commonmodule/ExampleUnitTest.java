package com.wlcookies.commonmodule;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<String> ss = new ArrayList<>();
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("s1", true);
        map.put("s2", true);
        map.put("s3", true);
        map.put("s4", true);

        for (String s : map.keySet()) {
            Boolean aBoolean = map.get(s);
            if (aBoolean != null && !aBoolean) {
                ss.add(s);
            }
        }
        System.out.println(ss);
    }
}