package com.redinn.oceanpeace.focus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FocusServiceTest {

    String[] appsData;
    long continuousTime;

    @BeforeEach
    void createTestData() {
        appsData = new String[] {"com.facebook.meta", "com.instagram.meta"};
        continuousTime = 10_000;
    }

    @Test
    @DisplayName("Checking data passing and saving by startStopwatch")
    void data_startStopwatch() {
        FocusService testClass = new FocusService();
        testClass.startStopwatch(appsData);
        assertArrayEquals(testClass.appsPackageNames, appsData);
    }


    @Test
    @DisplayName("Checking data passing and saving by startContinuous")
    void data_startContinuous() {
        FocusService testClass = new FocusService();
        testClass.startContinuous(appsData, continuousTime);
        assertArrayEquals(testClass.appsPackageNames, appsData);
    }
}