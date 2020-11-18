package ua.yuriih.lab1.model;

public class TrainVehicle {
    private final String name;
    private final int horsepower;
    
    public TrainVehicle(String name, int horsepower) {
        if (horsepower < 0)
            throw new IllegalArgumentException("Vehicle must not have less than 0 horsepower.");

        this.name = name;
        this.horsepower = horsepower;
    }

    public TrainVehicle(String name) {
        this(name, 0);
    }

    public String getName() {
        return name;
    }
    
    public int getHorsepower() {
        return horsepower;
    }

    public boolean canPullOrPushTrain() {
        return horsepower > 0;
    }
}
