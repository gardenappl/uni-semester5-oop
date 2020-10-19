package ua.yuriih.lab1;

import ua.yuriih.lab1.train.PassengerWagon;
import ua.yuriih.lab1.train.Train;
import ua.yuriih.lab1.train.TrainVehicle;

import java.util.ArrayList;
import java.util.Comparator;

public class TrainUtilities {
    public enum PassengerWagonSort {
        CAPACITY((wagon1, wagon2) -> {
            return Integer.compare(wagon1.getCapacity(), wagon2.getCapacity());
        }),
        COMFORT((wagon1, wagon2) -> {
            return Integer.compare(wagon1.getComfortLevel(), wagon2.getComfortLevel());
        });

        private Comparator<PassengerWagon> comparator;

        PassengerWagonSort(Comparator<PassengerWagon> comparator) {
            this.comparator = comparator;
        }
        
        public Comparator<PassengerWagon> getComparator() {
            return comparator;
        }
    }
    
    public static ArrayList<PassengerWagon> getPassengerWagons(Train train) {
        ArrayList<PassengerWagon> passengerWagons = new ArrayList<>();
        passengerWagons.ensureCapacity(train.getVehicleCount());

        for (TrainVehicle vehicle : train.getVehicles()) {
            if (vehicle instanceof PassengerWagon)
                passengerWagons.add((PassengerWagon)vehicle);
        }
        return passengerWagons;
    }

    public static ArrayList<PassengerWagon> sortPassengerWagons(
            Train train, PassengerWagonSort sort
    ) {
        ArrayList<PassengerWagon> passengerWagons = getPassengerWagons(train);
        passengerWagons.sort(sort.getComparator());
        return passengerWagons;
    }
    
    public static ArrayList<PassengerWagon> findPassengerWagons(
            Train train, int minCapacity, int maxCapacity
    ) {
        ArrayList<PassengerWagon> passengerWagons = getPassengerWagons(train);
        passengerWagons.removeIf(wagon -> wagon.getCapacity() < minCapacity ||
                wagon.getCapacity() > maxCapacity);
        return passengerWagons;
    }
}
