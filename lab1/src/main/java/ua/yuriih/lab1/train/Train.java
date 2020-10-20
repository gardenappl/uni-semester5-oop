package ua.yuriih.lab1.train;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private final ArrayList<TrainVehicle> trainVehicles;
    
    public Train(List<TrainVehicle> wagons) {
        if (wagons.get(0).getHorsepower() == 0 &&
                wagons.get(wagons.size() - 1).getHorsepower() == 0) {
            throw new IllegalArgumentException("Train must have powered vehicles on at least one end.");
        }
        trainVehicles = new ArrayList<>(wagons);
    }
    
    public int getPassengerCapacity() {
        int totalCapacity = 0;
        for (TrainVehicle vehicle : trainVehicles) {
            if (vehicle instanceof PassengerWagon) {
                totalCapacity += ((PassengerWagon) vehicle).getCapacity();
            }
        }
        return totalCapacity;
    }

    public int getCargoCapacity() {
        int totalCapacityKg = 0;
        for (TrainVehicle vehicle : trainVehicles) {
            if (vehicle instanceof CargoWagon) {
                totalCapacityKg += ((CargoWagon) vehicle).getCapacityKg();
            }
        }
        return totalCapacityKg;
    }
    
    public List<TrainVehicle> getVehicles() {
        return new ArrayList<>(trainVehicles);
    }
    
    public int getVehicleCount() {
        return trainVehicles.size();
    }
}
