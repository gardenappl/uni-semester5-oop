package ua.yuriih.lab1.model;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private final List<TrainVehicle> trainVehicles;
    private final String name;
    
    public Train(String name, List<TrainVehicle> wagons) {
        if (wagons.isEmpty())
            throw new IllegalArgumentException("Train must have at least one vehicle.");

        if (!wagons.get(0).canPullOrPushTrain() &&
                !wagons.get(wagons.size() - 1).canPullOrPushTrain()) {
            throw new IllegalArgumentException("Train must have powered vehicles on at least one end.");
        }

        this.name = name;
        trainVehicles = new ArrayList<>(wagons);
    }
    
    public int getPassengerCapacity() {
        int totalCapacity = 0;
        for (TrainVehicle vehicle : trainVehicles) {
            if (vehicle instanceof PassengerWagon) {
                totalCapacity += ((PassengerWagon) vehicle).getPassengerCapacity();
            }
        }
        return totalCapacity;
    }

    public int getCargoCapacity() {
        int totalCapacityKg = 0;
        for (TrainVehicle vehicle : trainVehicles) {
            if (vehicle instanceof PassengerWagon) {
                totalCapacityKg += ((PassengerWagon) vehicle).getCargoCapacityKg();
            }
        }
        return totalCapacityKg;
    }

    public int getHorsepower() {
        int totalHP = 0;
        for (TrainVehicle vehicle : trainVehicles) {
            totalHP += vehicle.getHorsepower();
        }
        return totalHP;
    }
    
    public List<TrainVehicle> getVehicles() {
        return new ArrayList<>(trainVehicles);
    }
    
    public int getVehicleCount() {
        return trainVehicles.size();
    }

    public String getName() {
        return name;
    }
}
