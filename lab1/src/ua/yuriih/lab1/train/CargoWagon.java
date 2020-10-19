package ua.yuriih.lab1.train;

public class CargoWagon extends TrainVehicle {
    private final int capacityKg;
    
    public CargoWagon(String name, int kgCapacity) {
        super(name);
        this.capacityKg = kgCapacity;
    }

    public int getCapacityKg() {
        return capacityKg;
    }
}
