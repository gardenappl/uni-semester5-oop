package ua.yuriih.lab1.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.yuriih.lab1.TestUtils;
import ua.yuriih.lab1.model.Locomotive;
import ua.yuriih.lab1.model.PassengerWagon;
import ua.yuriih.lab1.model.Train;
import ua.yuriih.lab1.model.TrainVehicle;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

class TrainConsoleViewTest {
    private List<TrainVehicle> vehicles = Arrays.asList(
            new Locomotive("Test Locomotive", 5000),
            new PassengerWagon("Wagon 1", 30, 30, 2),
            new PassengerWagon("Wagon 2", 10, 50, 0),
            new PassengerWagon("Comfy Wagon", 5, 100, 5)
    );
    private Train train = new Train("Test Train", vehicles);
    private TrainConsoleView view = new TrainConsoleView(train);

    //Large enough to read all output at once
    private static final int BYTE_ARRAY_SIZE = 10000;

    
    @Test
    @DisplayName("Test sorting by passenger capacity")
    void printSortedPassengerWagons_testPassenger() {
        TestUtils.testPrintStream((PrintStream stream) ->
                        view.printSortedPassengerWagons(stream, TrainConsoleView.PassengerWagonSort.CAPACITY),
                BYTE_ARRAY_SIZE,
                "Passenger wagons of Test Train:",
                "\tComfy Wagon: passenger capacity = 5, cargo capacity = 100 kg, comfort = 5",
                "\tWagon 2: passenger capacity = 10, cargo capacity = 50 kg, comfort = 0",
                "\tWagon 1: passenger capacity = 30, cargo capacity = 30 kg, comfort = 2");
    }

    @Test
    @DisplayName("Test sorting by cargo capacity")
    void printSortedPassengerWagons_testCargo() {
        TestUtils.testPrintStream((PrintStream stream) ->
                        view.printSortedPassengerWagons(stream, TrainConsoleView.PassengerWagonSort.CARGO),
                BYTE_ARRAY_SIZE,
                "Passenger wagons of Test Train:",
                "\tWagon 1: passenger capacity = 30, cargo capacity = 30 kg, comfort = 2",
                "\tWagon 2: passenger capacity = 10, cargo capacity = 50 kg, comfort = 0",
                "\tComfy Wagon: passenger capacity = 5, cargo capacity = 100 kg, comfort = 5");
    }

    @Test
    @DisplayName("Test sorting by comfort level")
    void printSortedPassengerWagons_testComfort() {
        TestUtils.testPrintStream((PrintStream stream) ->
                        view.printSortedPassengerWagons(stream, TrainConsoleView.PassengerWagonSort.COMFORT),
                BYTE_ARRAY_SIZE,
                "Passenger wagons of Test Train:",
                "\tWagon 2: passenger capacity = 10, cargo capacity = 50 kg, comfort = 0",
                "\tWagon 1: passenger capacity = 30, cargo capacity = 30 kg, comfort = 2",
                "\tComfy Wagon: passenger capacity = 5, cargo capacity = 100 kg, comfort = 5");
    }

    @Test
    void printPassengerWagons() {
        TestUtils.testPrintStream((PrintStream stream) ->
                        view.printPassengerWagons(stream, 10, 15),
                BYTE_ARRAY_SIZE,
                "Wagons with capacity between 10 and 15:",
                "\tWagon 2: passenger capacity = 10, cargo capacity = 50 kg, comfort = 0");
    }

    @Test
    void printTrainInfo() {
        TestUtils.testPrintStream((PrintStream stream) ->
                        view.printTrainInfo(stream),
                BYTE_ARRAY_SIZE,
                "Test Train: passenger capacity = 45, cargo capacity = 180 kg, power = 5000 HP");
    }
}