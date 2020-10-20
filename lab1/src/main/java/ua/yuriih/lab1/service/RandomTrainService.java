package ua.yuriih.lab1.service;

import ua.yuriih.lab1.model.CargoWagon;
import ua.yuriih.lab1.model.PassengerWagon;
import ua.yuriih.lab1.model.Train;
import ua.yuriih.lab1.model.TrainVehicle;

import java.util.ArrayList;
import java.util.Random;

public class RandomTrainService {
    Random rng = new Random();

    public Train makeRandomTrain() {
        int length = 5 + rng.nextInt(5);
        ArrayList<TrainVehicle> wagons = new ArrayList<>(length);

        wagons.add(makePassengerWagon(true));
        for (int i = 1; i < length - 1; i++) {
            wagons.add(makePassengerWagon(false));
        }
        wagons.add(makePassengerWagon(rng.nextBoolean()));

        String name = "Train T-" + rng.nextInt(1000);
        return new Train(name, wagons);
    }
    
    private PassengerWagon makePassengerWagon(boolean powered) {
        String name = "Wagon P-" + rng.nextInt(1000);
        int capacity = 10 + rng.nextInt(50);
        int comfortLevel = rng.nextInt(5);
        int horsepower = powered ? 500 + rng.nextInt(2000) : 0;
        return new PassengerWagon(name, capacity, comfortLevel, horsepower);
    }
}
