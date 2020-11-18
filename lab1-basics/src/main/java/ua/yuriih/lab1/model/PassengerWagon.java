package ua.yuriih.lab1.model;

public class PassengerWagon extends TrainVehicle {
    private final int passengerCapacity;
    private final int cargoCapacityKg;
    private final int comfortLevel;

    public PassengerWagon(String name, int passengerCapacity, int cargoCapacityKg, int comfortLevel) {
        super(name);
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.comfortLevel = comfortLevel;
    }

    public PassengerWagon(String name, int passengerCapacity, int cargoCapacityKg, int comfortLevel, int horsepower) {
        super(name, horsepower);
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.comfortLevel = comfortLevel;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public int getComfortLevel() {
        return comfortLevel;
    }

    public int getCargoCapacityKg() {
        return cargoCapacityKg;
    }
}
