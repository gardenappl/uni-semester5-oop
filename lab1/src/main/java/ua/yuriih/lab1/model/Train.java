package ua.yuriih.lab1.model;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private final ArrayList<TrainVehicle> trainVehicles;
    private final String name;
    
    public Train(String name, List<TrainVehicle> wagons) {
        this.name = name;

        if (wagons.isEmpty())
            throw new IllegalArgumentException("Train must have at least one vehicle.");

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
