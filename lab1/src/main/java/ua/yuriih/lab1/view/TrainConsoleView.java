package ua.yuriih.lab1.view;

import ua.yuriih.lab1.model.PassengerWagon;
import ua.yuriih.lab1.model.Train;
import ua.yuriih.lab1.model.TrainVehicle;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;

public class TrainConsoleView {
    public enum PassengerWagonSort {
        CAPACITY((wagon1, wagon2) -> {
            return Integer.compare(wagon1.getCapacity(), wagon2.getCapacity());
        }),
        COMFORT((wagon1, wagon2) -> {
            return Integer.compare(wagon1.getComfortLevel(), wagon2.getComfortLevel());
        });

        private final Comparator<PassengerWagon> comparator;

        PassengerWagonSort(Comparator<PassengerWagon> comparator) {
            this.comparator = comparator;
        }

        Comparator<PassengerWagon> getComparator() {
            return comparator;
        }
    }


    Train train;

    public TrainConsoleView(Train train) {
        this.train = train;
    }


    private ArrayList<PassengerWagon> getPassengerWagons() {
        ArrayList<PassengerWagon> passengerWagons = new ArrayList<>();
        passengerWagons.ensureCapacity(train.getVehicleCount());

        for (TrainVehicle vehicle : train.getVehicles()) {
            if (vehicle instanceof PassengerWagon)
                passengerWagons.add((PassengerWagon)vehicle);
        }
        return passengerWagons;
    }

    private ArrayList<PassengerWagon> sortPassengerWagons(PassengerWagonSort sort) {
        ArrayList<PassengerWagon> passengerWagons = getPassengerWagons();
        passengerWagons.sort(sort.getComparator());
        return passengerWagons;
    }
    
    public void printSortedPassengerWagons(PrintStream output, PassengerWagonSort sort) {
        ArrayList<PassengerWagon> wagons = sortPassengerWagons(sort);
        
        output.println("Passenger wagons of " + train.getName() + ':');
        for (PassengerWagon wagon : wagons) {
            output.print('\t');
            output.println(getWagonInfo(wagon));
        }
    }
    
    private String getWagonInfo(PassengerWagon wagon) {
        StringBuilder sb = new StringBuilder()
                .append(wagon.getName())
                .append(": capacity = ")
                .append(wagon.getCapacity())
                .append(", comfort = ")
                .append(wagon.getComfortLevel());
        if (wagon.getHorsepower() > 0) {
            sb.append(", HP = ");
            sb.append(wagon.getHorsepower());
        }
        return sb.toString();
    }

    private ArrayList<PassengerWagon> findPassengerWagons(int minCapacity, int maxCapacity) {
        ArrayList<PassengerWagon> passengerWagons = getPassengerWagons();
        passengerWagons.removeIf(wagon -> wagon.getCapacity() < minCapacity ||
                wagon.getCapacity() > maxCapacity);
        return passengerWagons;
    }
    
    public void printPassengerWagons(PrintStream output, int minCapacity, int maxCapacity) {
        ArrayList<PassengerWagon> wagons = findPassengerWagons(minCapacity, maxCapacity);

        output.printf("Wagons with capacity between %d and %d:\n", minCapacity, maxCapacity);
        for (PassengerWagon wagon : wagons) {
            output.print('\t');
            output.println(getWagonInfo(wagon));
        }
    }
    
    public void printTrainInfo(PrintStream output) {
        output.printf("Train #%s: passenger capacity = %d, cargo capacity = %d Kg\n",
                train.getName(), train.getPassengerCapacity(), train.getCargoCapacity());
    }
}
