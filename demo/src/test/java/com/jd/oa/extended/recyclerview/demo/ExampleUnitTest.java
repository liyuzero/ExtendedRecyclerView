package com.jd.oa.extended.recyclerview.demo;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);

        ArrayList<Integer> s = new ArrayList<>();
        s.add(5);
        s.add(5);

        list.addAll(2, s);

        for (Integer integer: list){
            System.out.print("="+integer);
        }
    }
}