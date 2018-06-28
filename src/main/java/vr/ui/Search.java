package vr.ui;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
import vr.data.TimeTableRow;
import vr.data.Train;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Search {
    private Scanner reader;
    private BackgroundData bgrdata;
    private JsonReadData trainData;

    private SearchPrint print;

    public Search(Scanner reader, BackgroundData bgrdata, JsonReadData trainData) {
        this.reader = reader;
        this.bgrdata = bgrdata;
        this.trainData = trainData;
        this.print = new SearchPrint(bgrdata);
    }

    public void timetableSearch() { // dialogi mihin mennään, mitä halutaan hakea
        print.header();
        while (true) {
            print.select();
            String answer = reader.nextLine();
            if (answer.equals("1")) {
                nextDepartures();
            } else if (answer.equals("2")) {
                timeTablesFromTo();
            } else if (answer.equals("3")) {
                trainLateOrInTime();
            } else if (answer.equals("4")) {
                break;
            }
        }
    }

    private void nextDepartures() { // mihin mennään
        while (true) {
            while (true) {
                String departure = getStation("departure"); // tänne tallennetaan aseman nimi
                if (departure == null) {
                    break;
                }
                String stationShortCode = bgrdata.getShortCode(departure); // etsitään mapista
                print.resultHeader(bgrdata.getStationName(stationShortCode));
                if (stationShortCode != null) {
                    List<Train> suitableTrains = trainData.getTimeTable(stationShortCode);// saadaan lista junista
                    suitableTrains = leavingTrains(suitableTrains, stationShortCode);
                    if (!suitableTrains.isEmpty()) {
                        print.departureScheduleFromOneStation(suitableTrains, departure.toUpperCase(), stationShortCode);//ui presumes that all trains on the list are passenger trains.
                        break;
                    } else {
                        System.out.println("There are no trains leaving from the " + departure + " station in the near future.");
                        break;
                    }
                } else {
                    System.out.print("The station you gave was not found on our system. Finnish spelling can be quite hard, please try again! ");
                }
            }
            System.out.print("\nHappy? Want to search for more departures? (y/n) ");
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
            print.resultHeader(bgrdata.getStationName(departureShortCode));
            String arrivalShortCode = bgrdata.getShortCode(arrival);
            if (departureShortCode != null && arrivalShortCode != null) {
                List<Train> suitableTrains = trainData.getTimeTable(departureShortCode, arrivalShortCode);
                if (!suitableTrains.isEmpty()) {
                    print.departureAndArrivalWithDateAndTime(suitableTrains, departureShortCode, arrivalShortCode);
                } else {
                    System.out.println("There are no connections from " + departure + " station to " + arrival + " station in the near future.");
                }
            } else {
                System.out.println("Unfortunately we couldn't find the train stations for you.");
            }
            System.out.println("======================================");
            ;
            System.out.print("Happy? Want to search for more departures? (y/n) ");
            String answer = reader.nextLine();
            if (answer.equals("n")) {
                return;
            }
        }
    }

    private String getStation(String wanted) { //  Tältä voidaan kysyä sekä lähtö että saapumisasemaa
        String station = "";
        System.out.print("Write the name of the " + wanted + " station: ");
        int givingUp = 0;
        while (true) {
            station = reader.nextLine().toUpperCase();
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
        if (!station.isEmpty()) {
            List<String> nearestMatches = bgrdata.getNearestMatches(station);
            System.out.println("Did you mean for example ");
            nearestMatches.stream().forEach(System.out::println);
        }
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

    private List<Train> leavingTrains(List<Train> departure, String departureShortCode) {
        Iterator<Train> it = departure.iterator();
        while (it.hasNext()) {
            Train train = it.next();
            List<TimeTableRow> timeTable = train.getTimeTableRows();
            int found = (int) timeTable.stream().filter(r -> r.getStationShortCode().equals(departureShortCode)).filter(r -> r.getType().equals("DEPARTURE")).count();
            if (found < 1) {
                it.remove();
            }
        }
        departure = orderLeavingTrains(departure, departureShortCode);
        return departure;
    }

    private List<Train> orderLeavingTrains(List<Train> departure, String departureShortCode) {
        departure = departure.stream()
                .sorted((t1, t2) -> print.getScheduledTime(t1.getTimeTableRows(), "DEPARTURE", departureShortCode)
                        .compareTo(print.getScheduledTime(t2.getTimeTableRows(), "DEPARTURE", departureShortCode)))
                .collect(Collectors.toList());
        return departure;
    }

    private void trainLateOrInTime() {
        System.out.println("Sorry, we have no idea if your train's on time or not. Neither as VR..");
    }
}
