package com.redinn.oceanpeace.focus;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Data")
@Test
@interface Data {

}

class FocusServiceTest {

    String[] appsData;
    long continuousTime;
    long workDuration;
    long breakDuration;

    @BeforeEach
    void createTestData() {
        appsData = new String[] {"com.facebook.meta", "com.instagram.meta"};
        continuousTime = 10_000;
        workDuration = 10_000;
        breakDuration = 5_000;
    }

    @Nested
    class startStopwatch {
        @Data
        @Test
        @DisplayName("Checking data passing and saving")
        void data_passing() {
            FocusService testClass = new FocusService();
            testClass.startStopwatch(appsData);
            assertArrayEquals(testClass.appsPackageNames, appsData);
        }
    }

    @Nested
    class startContinuous {
        @Data
        @Test
        @DisplayName("Checking data passing and saving")
        void data_passing() {
            FocusService testClass = new FocusService();
            testClass.startContinuous(appsData, continuousTime);
            assertArrayEquals(testClass.appsPackageNames, appsData);
        }
    }

    @Nested
    class startPomodoro {
        @Data
        @Test
        @DisplayName("Checking data passing and saving")
        void data_passing() {
            FocusService testClass = new FocusService();
            testClass.startPomodoro(appsData, workDuration, breakDuration, 3);
            assertArrayEquals(testClass.appsPackageNames, appsData);
        }
    }
}