package ua.yuriih.lab1.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainTest {
    private List<TrainVehicle> vehicles = Arrays.asList(
            new TrainVehicle("Test Locomotive", 5000),
            new PassengerWagon("Wagon 1", 30, 30, 0),
            new PassengerWagon("Wagon 2", 10, 50, 2),
            new PassengerWagon("Comfy Wagon", 5, 100, 5)
    );
    private Train train = new Train("Test Train", vehicles);

    @Test
    void getPassengerCapacity() {
        assertEquals(45, train.getPassengerCapacity());
    }

    @Test
    void getCargoCapacity() {
        assertEquals(180, train.getCargoCapacity());
    }

    @Test
    void getHorsepower() {
        assertEquals(5000, train.getHorsepower());
    }

    @Test
    void getVehicles() {
        assertArrayEquals(vehicles.toArray(), train.getVehicles().toArray());
    }

    @Test
    void getVehicleCount() {
        assertEquals(4, train.getVehicleCount());
    }

    @Test
    void getName() {
        assertEquals("Test Train", train.getName());
    }

    @Test
    void trainConstructorTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new Train("Empty train", new ArrayList<>()));

        assertThrows(IllegalArgumentException.class,
                () -> new Train("Train with no engine",
                        Collections.singletonList(new PassengerWagon("Test", 0, 0, 0))));

        assertDoesNotThrow(() -> new Train("Train with locomotive",
                Collections.singletonList(new TrainVehicle("Loco", 4000))));
    }
}