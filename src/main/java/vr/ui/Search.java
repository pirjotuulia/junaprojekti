package vr.ui;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
import vr.data.TimeTableRow;
import vr.data.Train;

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

    public void timetableSearch() { // opening dialogue for TimeTableSearch
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

    private void nextDepartures() { // Looking for trains leaving from a given station
        while (true) {
            while (true) {
                String departure = getStation("departure"); // get station name from user
                if (departure == null) {
                    break;
                }
                String stationShortCode = bgrdata.getShortCode(departure); // get shortCode from station name-shortcode -map
                print.resultHeader(bgrdata.getStationName(stationShortCode)); //print out nice pic while searching
                if (stationShortCode != null) {
                    List<Train> suitableTrains = trainData.getTimeTable(stationShortCode);// get train data from api
                    suitableTrains = leavingTrains(suitableTrains, stationShortCode); //filter out trains which are arriving
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
            } //else repeat the while loop;
        }
    }


    private void timeTablesFromTo() { // timeTables from one station to another
        outer:
        while (true) {
            String departure = getStation("departure"); //get departure station name
            if (departure == null) {
                break outer;
            }
            String arrival = getStation("arrival"); //get arrival station name
            if (arrival == null) {
                break outer;
            }
            String departureShortCode = bgrdata.getShortCode(departure); // get shortcode from map
            print.resultHeader(bgrdata.getStationName(departureShortCode)); //pring out nice pic
            String arrivalShortCode = bgrdata.getShortCode(arrival); //get the other shortcode from map
            if (departureShortCode != null && arrivalShortCode != null) { //to this loop only if info was valid and gave shortcodes for both stations
                List<Train> suitableTrains = trainData.getTimeTable(departureShortCode, arrivalShortCode); //get list of trains
                if (!suitableTrains.isEmpty()) {
                    print.departureAndArrivalWithDateAndTime(suitableTrains, departureShortCode, arrivalShortCode); //print out timetables
                } else {
                    System.out.println("There are no connections from " + departure + " station to " + arrival + " station in the near future.");
                }
            } else {
                System.out.println("Unfortunately we couldn't find the train stations for you.");
            }
            System.out.println("======================================\n");
            System.out.print("Happy? Want to search for more departures? (y/n) ");
            String answer = reader.nextLine();
            if (answer.equals("n")) {
                return;
            }
        }
    }

    private String getStation(String wanted) { // get station name from user
        String station = "";
        System.out.print("Write the name of the " + wanted + " station: ");
        int givingUp = 0; //count for unsuccessfull tries
        while (true) {
            station = reader.nextLine().toUpperCase(); //get input
            if (bgrdata.isKey(station)) { //if input string is found in map
                break;
            } else {
                helpCustomerFindStation(station); //try to get a valid input string from user
                givingUp++; //count unsuccessfull attempts
                if (givingUp > 3) { //if user is not succeeding and is getting frustrated
                    if (offerGivingUp()) {
                        return null;
                    }
                }
                continue;
            }
        }

        return station;
    }

    private void helpCustomerFindStation(String station) { //dialogue and offer of most likely stations
        if (!station.isEmpty()) {
            List<String> nearestMatches = bgrdata.getNearestMatches(station); //get stationnames which start with what the user wrote
            System.out.println("Did you mean for example ");//offer list of found names as a hint
            nearestMatches.stream().forEach(System.out::println);
        }
        System.out.println("Please write the full name of the station.");
    }

    private boolean offerGivingUp() { //this is offered as a way out
        System.out.println("Sometimes it's better to stay still than constantly be on the move. Do you want to give up? (y/n)");
        String answer = reader.nextLine();
        if (answer.equals("y")) {
            return true;
        }
        return false;
    }

    private List<Train> leavingTrains(List<Train> departure, String departureShortCode) { //filter out arriving trains from one stations full train list and return departing trains
        Iterator<Train> it = departure.iterator();
        while (it.hasNext()) {
            Train train = it.next();
            List<TimeTableRow> timeTable = train.getTimeTableRows();
            int found = (int) timeTable.stream().filter(r -> r.getStationShortCode().equals(departureShortCode)).filter(r -> r.getType().equals("DEPARTURE")).count();
            if (found < 1) { //if none of the timetablerows of a train contain the given shortcode and are for a departing train, the train is removed from list
                it.remove();
            }
        }
        departure = orderLeavingTrains(departure, departureShortCode);
        return departure;
    }

    private List<Train> orderLeavingTrains(List<Train> departure, String departureShortCode) { //for sorting trains by the time they leave from a given station.
        departure = departure.stream()
                .sorted((t1, t2) -> print.getScheduledTime(t1.getTimeTableRows(), "DEPARTURE", departureShortCode)
                        .compareTo(print.getScheduledTime(t2.getTimeTableRows(), "DEPARTURE", departureShortCode)))
                .collect(Collectors.toList());
        return departure;
    }

    private void trainLateOrInTime() { //not done yet
        System.out.println("Sorry, we have no idea if your train's on time or not. Neither has VR..");
    }
}
