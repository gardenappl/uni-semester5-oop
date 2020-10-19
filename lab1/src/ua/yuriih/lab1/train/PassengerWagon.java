package ua.yuriih.lab1.train;

public class PassengerWagon extends TrainVehicle {
    private final int capacity;
    private final int comfortLevel;

    public PassengerWagon(String name, int capacity, int comfortLevel) {
        super(name);
        this.capacity = capacity;
        this.comfortLevel = comfortLevel;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getComfortLevel() {
        return comfortLevel;
    }
}
