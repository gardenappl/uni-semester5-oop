package ua.yuriih.lab1.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomTrainServiceTest {

    @Test
    @DisplayName("Test that RandomTrainService creates valid trains")
    void makeRandomTrain() {
        RandomTrainService trainService = new RandomTrainService();

        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(trainService::makeRandomTrain);
        }
    }
}