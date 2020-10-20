package ua.yuriih.lab1.model;

public class Locomotive extends TrainVehicle {
    public Locomotive(String name, int horsepower) {
        super(name, horsepower);
        if (horsepower <= 0)
            throw new IllegalArgumentException("Locomotive must have more than 0 horsepower.");
    }
}
