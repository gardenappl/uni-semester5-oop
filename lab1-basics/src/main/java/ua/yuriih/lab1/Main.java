package ua.yuriih.lab1;

import ua.yuriih.lab1.model.Train;
import ua.yuriih.lab1.service.RandomTrainService;
import ua.yuriih.lab1.view.TrainConsoleView;

public class Main {

    public static void main(String[] args) {
        RandomTrainService service = new RandomTrainService();
        Train train = service.makeRandomTrain();
        TrainConsoleView view = new TrainConsoleView(train);

        view.printTrainInfo(System.out);
        view.printSortedPassengerWagons(System.out,
                TrainConsoleView.PassengerWagonSort.CAPACITY);

        view.printPassengerWagons(System.out, 20, 30);
    }
}
