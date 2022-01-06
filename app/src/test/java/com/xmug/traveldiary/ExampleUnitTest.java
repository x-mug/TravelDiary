package com.xmug.traveldiary;

import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void fib_isCorrect() {
        assertEquals(fib(2), 2);
    }

    @Test
    public void fib_isCorrectTest() {
        assertEquals(fib(0), 1);
    }


    private int fib(int i) {

        if (i == 0) {
            return 1;
        } else if (i == 1) {
            return 1;
        } else {
            return fib(i -1) + fib(i -2);
        }
    }


    private long fibArr(int x) {
        if (x < 0) {
            return -1;
        } else if (x == 0 | x == 1) {
            return 1;
        } else {
            long a = 1;
            long b = 1;
            long c = 0;

            for (long i = 2; i <= x; i++) {
                c = a + b;
                a = b;
                b = c;
            }
            return c;
        }
    }

    @Test
    public void fibArr_init() {
        assertEquals(fibArr(0),1);
        assertEquals(fibArr(1),1);
    }

    @Test
    public void fibArr_two() {
        assertEquals(fibArr(2),2);
    }

    @Test
    public void fibArr_bigNumber() {
        assertEquals(fibArr(9999),1);
    }
}