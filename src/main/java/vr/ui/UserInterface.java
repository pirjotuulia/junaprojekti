package vr.ui;

import vr.data.BackgroundData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * by Pirjo
 */
public class UserInterface {
    private Scanner reader;
    //    private JsonRoadData trainData;
    private Map<String, String> stationShortCodes;

    public UserInterface(Scanner reader, Map<String, String> stationShortCodes) {
        this.reader = reader;
        this.stationShortCodes = stationShortCodes;
    }

    public void start() {
        System.out.println("======================================");
        System.out.println("          WE 'R HAPPINESS!  ");
        System.out.println("======================================");
        while (true) {
            System.out.println("");
            System.out.println(" 1) Timetable search - commuter trains");
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
            String departure = "";
            String arrival = "";
            System.out.print("Write the name of the station you want to depart from: ");
            while (true) {
                int givingUp = 0;
                departure = reader.nextLine();
                if (!this.stationShortCodes.containsKey(departure)) {
                    if (givingUp > 3) {
                        System.out.println("Sometimes it's better to stay still than constantly be on the move. Do you want to give up? (y/n)");
                        String answer = reader.nextLine();
                        if (answer.equals("y")) {
                            break outer;
                        }
                    }
                    System.out.print("The station you gave was not found on our system. Finnish spelling can be quite hard, please try again! ");
                    givingUp++;
                } else {
                    break;
                }
            }
            System.out.print("Write the name of the station you want to go to: ");
            while (true) {
                int givingUp = 0;
                arrival = reader.nextLine();
                if (!this.stationShortCodes.containsKey(arrival)) {
                    if (givingUp > 3) {
                        System.out.println("Sometimes it's better to stay still than constantly be on the move. Do you want to give up? (y/n)");
                        String answer = reader.nextLine();
                        if (answer.equals("y")) {
                            break outer;
                        }
                    }
                    System.out.print("The station you gave was not found on our system. Finnish spelling can be quite hard, please try again! ");
                } else {
                    break;
                }
            }
            String departureShortCode = this.stationShortCodes.get(departure);
            String arrivalShortCode = this.stationShortCodes.get(arrival);
//            List<Train> suitableTrains = trainData.getTimeTable(departureShortCode,arrivalShortCode);
            List<String> suitableTrains = new ArrayList<>();//this line only for testing purposes, should not be included in producy
            if (!suitableTrains.isEmpty()) {
                suitableTrains.stream().forEach(System.out::println);//ui presumes that all trains on the list are passenger trains.
                break;
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


    private void nextDepartures() {
        while (true) {
            System.out.print("Write the name of the station you want to depart from: ");
            while (true) {
                String answer = reader.nextLine();
                if (this.stationShortCodes.containsKey(answer)) {
                    String stationShortCode = this.stationShortCodes.get(answer);
//                    List<Train> suitableTrains = trainData.getTimeTable(stationShortCode);
                    List<String> suitableTrains = new ArrayList<>();//this line only for testing purposes, should not be included in producy
                    if (!suitableTrains.isEmpty()) {
                        suitableTrains.stream().forEach(System.out::println);//ui presumes that all trains on the list are passenger trains.
                        break;
                    } else {
                        System.out.println("There are no trains leaving from the " + answer + " station in the near future.");
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
//Sometimes it's better to stay still than constantly be on the move. Do you want to give up? (y/n)