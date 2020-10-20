package ua.yuriih.lab1.train;

public abstract class TrainVehicle {
    private final String name;
    /**
     * The horsepower field is stored in TrainVehicle as opposed to Locomotive,
     * because apparently there are trains with no distinct locomotives. They
     * consist entirely of passenger wagons and each of them has its own motor.
     */
    private final int horsepower;
    
    public TrainVehicle(String name, int horsepower) {
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
}
