package vr.ui;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
import vr.data.TimeTableRow;
import vr.data.Train;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Search {
    private Scanner reader;
    private BackgroundData bgrdata;
    private JsonReadData trainData;
    private Locale loc;

    public Search(Scanner reader, BackgroundData bgrdata, JsonReadData trainData) {
        this.reader = reader;
        this.bgrdata = bgrdata;
        this.trainData = trainData;
        this.loc = new Locale("fi", "FI");
    }

    public void timetableSearch() {
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

    private void nextDepartures() {
        while (true) {
            while (true) {
                String departure = getStation("departure");
                String stationShortCode = bgrdata.getShortCode(departure);
                if (stationShortCode != null) {
                    List<Train> suitableTrains = trainData.getTimeTable(stationShortCode);
                    if (!suitableTrains.isEmpty()) {
                        printDepartureScheduleFromOneStation(suitableTrains, departure);//ui presumes that all trains on the list are passenger trains.
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
            if (departureShortCode != null && arrivalShortCode != null) {
                List<Train> suitableTrains = trainData.getTimeTable(departureShortCode, arrivalShortCode);
                if (!suitableTrains.isEmpty()) {
                    //printDepartureAndArrivalWithDateAndTime(suitableTrains, departureShortCode, arrivalShortCode);
                    suitableTrains.stream().forEach(System.out::println);//ui presumes that all trains on the list are passenger trains.
                } else {
                    System.out.println("There are no connections from " + departure + " station to " + arrival + " station in the near future.");
                }
            } else {
                System.out.println("Unfortunately we couldn't find the train stations for you.");
            }
            System.out.println ("----------------------------------------------");
            System.out.print("Happy? Want to search for more departures? (y/n) ");
            String answer = reader.nextLine();
            if (answer.equals("n")) {
                return;
            }
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

    private void helpCustomerFindStation(String station) {
        List<String> nearestMatches = bgrdata.getNearestMatches(station);
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

    private String getDestinationStationName(Train train) {
        List<TimeTableRow> timeTable = train.getTimeTableRows();
        String shortCode = timeTable.get(timeTable.size()-1).getStationShortCode();
        return bgrdata.getStationName(shortCode);
    }

    private void printDepartureScheduleFromOneStation(List<Train> trains, String departure) {

        System.out.println ("");
        System.out.println ("");
        System.out.println("Looking for trains leaving from " + departure + " station:");
        System.out.println ("");
        System.out.println ("#########################################################");
        System.out.println ("___________   _______________________________________^__");
        System.out.println (" ___   ___ |||  ___   ___   ___    ___ ___  |   __  ,----\\");
        System.out.println ("|   | |   |||| |   | |   | |   |  |   |   | |  |  | |_____\\");
        System.out.println ("|___| |___|||| |___| |___| |___|  | O | O | |  |  |        \\");
        System.out.println ("           |||                    |___|___| |  |__|         )");
        System.out.println ("___________|||______________________________|______________/");
        System.out.println ("           ||| Happiness onboard!    /--------");
        System.out.println ("#########################################################");


        System.out.println ();
        System.out.println ("Below you can find trains that are leaving next from: " + departure);
        System.out.println ("");
        System.out.println ("                          TIMETABLE                                     " );
        System.out.println ("---------------------------------------------------------------------------");
        System.out.println ("Leaving time  //      " +        "Destination  //        " +      " Type of Train  ");
        System.out.println ("---------------------------------------------------------------------------");
        for (Train train : trains) {
            System.out.println(train.getDepartureLocalDate()+ " | " + " DestinationUnknown " + " | " + train.getTrainCategory () + " | ");
        }
    }

    private void printDepartureAndArrivalWithDateAndTime(List<Train> trains, String departureShortCode, String arrivalShortCode) {
        for (Train train : trains) {
            List<TimeTableRow> timetable = train.getTimeTableRows();
            LocalDateTime departureTime = getScheduledTime(timetable, "DEPARTURE", departureShortCode);
            LocalDateTime arrivalTime = getScheduledTime(timetable, "ARRIVAL", arrivalShortCode);


        }
    }

    private LocalDateTime getScheduledTime(List<TimeTableRow> schedule, String depOrArr, String stationShortCode) {
        LocalDateTime scheduledTime = null;
        for (TimeTableRow ttr : schedule) {
            if (ttr.getStationShortCode().equals(stationShortCode) && ttr.getType().equals(depOrArr)) {
                scheduledTime = ttr.getScheduledTime();//TimeTableRows method returns LocalDateTime in the final version.
            }
        }
        return scheduledTime;
    }

    private void trainLateOrInTime() {
        System.out.println("Sorry, we have no idea if your train's on time or not. Neither as VR..");
    }
}
