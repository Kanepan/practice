package com.kane.practice.base;

import java.time.Duration;

public class DurationTest {
    public static void main(String[] args) {

        // Duration 1 using parse() method
        Duration duration1
                = Duration.parse("P0DT0H4M");

        // Get the duration substracted
        // using minusSeconds() method
        System.out.println(duration1.minusSeconds(5));
        long time = System.currentTimeMillis();
        ;
        try {
            Thread.sleep(Duration.ofMillis(1000).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
