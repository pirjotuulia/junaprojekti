package vr.ui;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
import vr.data.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * by Pirjo
 */
public class UserInterface {
    private Scanner reader;
    private JsonReadData trainData;
    private BackgroundData bgrdata;

    public UserInterface(Scanner reader, BackgroundData bgrdata, JsonReadData trainData) {
        this.reader = reader;
        this.bgrdata = bgrdata;
        this.trainData = trainData;
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("            WE'R HAPPINESS!  ");
        System.out.println("========================================");
        while (true) {
            System.out.println("");
            System.out.println(" 1) Timetable search - passenger trains");
            System.out.println(" 2) Call Santa.");
            System.out.println(" 3) Find nearest Sauna.");
            System.out.println(" 4) Exit\n");
            System.out.print("Your choice: ");
            String answer = reader.nextLine();
            if (answer.equals("1")) {
                timetableSearch();
            } else if (answer.equals("2")) {
                System.out.println("Call Santa at Santa Claus Village amusement park, Rovaniemi: +358 16 3562096");
            } else if (answer.equals("3")) {
                System.out.println("Nearest sauna: Löyly at Hernesaarenranta 4, 00150 Helsinki");
            } else if (answer.equals("4")) {
                break;
            }
        }
        System.out.println("Thank you for using our happiness search!");

    }

    private void timetableSearch() {
        System.out.println("======================================");
        System.out.println("     Travel by train in Finland.");
        System.out.println("======================================");
        System.out.println("No trains today!");
        System.out.println("Just kidding.. ;-)");
        System.out.println(" 1) Search for trains leaving from where you are now or any other station. If you just want to get away, no matter where you go!");
        System.out.println(" 2) Search for trains going to a specific destination from where you are now. For the destination oriented!");
        System.out.println(" 3) Is my train on time? Could be, but maybe you still have time to go for a cup of coffee and ice cream before it leaves?");
        System.out.print(" Time to choose! ");
        String answer = reader.nextLine();
        if (answer.equals("1")) {
            nextDepartures();
        } else if (answer.equals("2")) {
            timeTablesFromTo();
        } else if (answer.equals("3")) {
            trainLateOrInTime();
        }
    }

    private void timeTablesFromTo() {
        outer:
        while (true) {
            String departure = getStation("departure");
            if (departure == null) {
                break outer;
            }
            String arrival = getStation("arrival");
            if (arrival == null) {
                break outer;
            }
            String departureShortCode = bgrdata.getShortCode(departure);
            String arrivalShortCode = bgrdata.getShortCode(arrival);
            List<Train> suitableTrains = trainData.getTimeTable(departureShortCode, arrivalShortCode);
            if (!suitableTrains.isEmpty()) {
                suitableTrains.stream().forEach(System.out::println);//ui presumes that all trains on the list are passenger trains.
            } else {
                System.out.println("There are no connections from " + departure + " station to " + arrival + " station in the near future.");
            }
            System.out.print("Happy? Want to search for more departures? (y/n) ");
            String answer = reader.nextLine();
            if (answer.equals("n")) {
                return;
            }
        }
    }

    private void tulostaJunaAikatauluYhdeltaAsemalta(List<Train> trains, String departure) {
        System.out.println("Next trains from " + departure + " station:");
        for (Train train : trains) {
            System.out.println(train.getDepartureLocalDate() + " " + train.getTrainCategory());
        }
    }

    private String getStation(String wanted) {
        String station = "";
        System.out.print("Write the name of the " + wanted + " station: ");
        int givingUp = 0;
        while (true) {
            station = reader.nextLine();
            if (bgrdata.isKey(station)) {
                break;
            } else {
                helpCustomerFindStation(station);
                givingUp++;
                if (givingUp > 3) {
                    if (offerGivingUp()) {
                        return null;
                    }
                }
                continue;
            }
        }
        return station;
    }

    private void helpCustomerFindStation(String departure) {
        List<String> nearestMatches = bgrdata.getNearestMatches(departure);
        System.out.println("Did you mean for example ");
        nearestMatches.stream().forEach(System.out::println);
        System.out.println("Please write the full name of the station.");
    }

    private boolean offerGivingUp() {
        System.out.println("Sometimes it's better to stay still than constantly be on the move. Do you want to give up? (y/n)");
        String answer = reader.nextLine();
        if (answer.equals("y")) {
            return true;
        }
        return false;
    }

    private void nextDepartures() {
        while (true) {
            while (true) {
                String departure = getStation("departure");
                String stationShortCode = bgrdata.getShortCode(departure);
                if (stationShortCode != null) {
                    List<Train> suitableTrains = trainData.getTimeTable(stationShortCode);
                    if (!suitableTrains.isEmpty()) {
                        suitableTrains.stream().forEach(System.out::println);//ui presumes that all trains on the list are passenger trains.
                        break;
                    } else {
                        System.out.println("There are no trains leaving from the " + departure + " station in the near future.");
                        break;

                    }

                } else {
                    System.out.print("The station you gave was not found on our system. Finnish spelling can be quite hard, please try again! ");
                }
            }
            System.out.print("Happy? Want to search for more departures? (y/n) ");
            String answer = reader.nextLine();
            if (answer.equals("n")) {
                return;
            }
        }
    }

    private void trainLateOrInTime() {
        System.out.println("Sorry, we have no idea if your train's on time or not. Neither as VR..");
    }
}