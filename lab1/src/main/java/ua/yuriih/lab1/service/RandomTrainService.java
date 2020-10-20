package ua.yuriih.lab1.service;

import ua.yuriih.lab1.model.Locomotive;
import ua.yuriih.lab1.model.PassengerWagon;
import ua.yuriih.lab1.model.Train;
import ua.yuriih.lab1.model.TrainVehicle;

import java.util.ArrayList;
import java.util.Random;

public class RandomTrainService {
    Random rng = new Random();

    public Train makeRandomTrain() {
        boolean useLocomotives = rng.nextBoolean();
        int length = 5 + rng.nextInt(5);
        ArrayList<TrainVehicle> wagons = new ArrayList<>(length);

        wagons.add(makePoweredVehicle(useLocomotives));

        for (int i = 1; i < length - 1; i++)
            wagons.add(makePassengerWagon(false));

        if (rng.nextBoolean())
            wagons.add(makePoweredVehicle(useLocomotives));
        else
            wagons.add(makePassengerWagon(false));

        String name = "Train T-" + rng.nextInt(1000);
        return new Train(name, wagons);
    }
    
    private TrainVehicle makePoweredVehicle(boolean useLocomotives) {
        if (useLocomotives)
            return makeLocomotive();
        else
            return makePassengerWagon(true);
    }
    
    private PassengerWagon makePassengerWagon(boolean powered) {
        String name = "Wagon P-" + rng.nextInt(1000);
        int passengerCapacity = 10 + rng.nextInt(50);
        int cargoCapacityKg = 10 + rng.nextInt(50);
        int comfortLevel = rng.nextInt(5);
        int horsepower = powered ? 2000 + rng.nextInt(5000) : 0;
        return new PassengerWagon(name, passengerCapacity, cargoCapacityKg, comfortLevel, horsepower);
    }

    private Locomotive makeLocomotive() {
        String name = "Wagon P-" + rng.nextInt(1000);
        int horsepower = 2000 + rng.nextInt(5000);
        return new Locomotive(name, horsepower);
    }
}
