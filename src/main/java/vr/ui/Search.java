package vr.ui;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
import vr.data.TimeTableRow;
import vr.data.Train;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Search {
    private Scanner reader;
    private BackgroundData bgrdata;
    private JsonReadData trainData;
    private Locale loc;
    private DateTimeFormatter datef;
    private DateTimeFormatter timef;

    public Search(Scanner reader, BackgroundData bgrdata, JsonReadData trainData) {
        this.reader=reader;
        this.bgrdata=bgrdata;
        this.trainData=trainData;
        this.loc=new Locale ("fi", "FI");
        this.datef=DateTimeFormatter.ofPattern ("dd.MM.");
        this.timef=DateTimeFormatter.ofPattern ("kk.mm");
    }

    public void timetableSearch() { // dialogi mihin mennään, mitä halutaan hakea
        System.out.println ("======================================");
        System.out.println ("     Travel by train in Finland.");
        System.out.println ("======================================");
        System.out.println ("No trains today!");
        System.out.println ("Just kidding.. ;-)");
        System.out.println ("-----------------------------");
        System.out.println (" 1) Choose 1 if you don't have a specific destination");
        System.out.println (" 2) Choose 2 if you have a specific destination");
        System.out.println (" 3) Check if the train is on time?");
        System.out.println ("");
        System.out.println (" Time to choose! ");
        System.out.println ("------------------------------");
        System.out.println ("Explanations of the search");
        System.out.println (" 1) Search for trains leaving from where you are now or any other station. If you just want to get away, no matter where you go!");
        System.out.println (" 2) Search for trains going to a specific destination from where you are now. For the destination oriented!");
        System.out.println (" 3) Your train might be on time, but maybe you still have time to go for a cup of coffee and ice cream before it leaves?");
        System.out.println ();
        String answer=reader.nextLine ();
        if (answer.equals ("1")) {
            nextDepartures ();
        } else if (answer.equals ("2")) {
            timeTablesFromTo ();
        } else if (answer.equals ("3")) {
            trainLateOrInTime ();
        }
    }

    private void nextDepartures() { // mihin mennään
        while (true) {
            while (true) {
                String departure=getStation ("departure"); // tänne tallennetaan aseman nimi
                String stationShortCode=bgrdata.getShortCode (departure); // etsitään mapista
                if (stationShortCode != null) {
                    List<Train> suitableTrains=trainData.getTimeTable (stationShortCode);// saadaan lista junista
                    suitableTrains=leavingTrains (suitableTrains, stationShortCode);
                    if (!suitableTrains.isEmpty ()) {
                        printDepartureScheduleFromOneStation (suitableTrains, departure, stationShortCode);//ui presumes that all trains on the list are passenger trains.
                        break;
                    } else {
                        System.out.println ("There are no trains leaving from the " + departure + " station in the near future.");
                        break;
                    }
                } else {
                    System.out.print ("The station you gave was not found on our system. Finnish spelling can be quite hard, please try again! ");
                }
            }
            System.out.print ("\nHappy? Want to search for more departures? (y/n) ");
            String answer=reader.nextLine ();
            if (answer.equals ("n")) {
                return;
            }
        }
    }


    private void timeTablesFromTo() {
        outer:
        while (true) {
            String departure=getStation ("departure");
            if (departure == null) {
                break outer;
            }
            String arrival=getStation ("arrival");
            if (arrival == null) {
                break outer;
            }
            String departureShortCode=bgrdata.getShortCode (departure);
            String arrivalShortCode=bgrdata.getShortCode (arrival);
            if (departureShortCode != null && arrivalShortCode != null) {
                List<Train> suitableTrains=trainData.getTimeTable (departureShortCode, arrivalShortCode);
                if (!suitableTrains.isEmpty ()) {
                    printDepartureAndArrivalWithDateAndTime (suitableTrains, departureShortCode, arrivalShortCode);
                    //              suitableTrains.stream().forEach(System.out::println);//ui presumes that all trains on the list are passenger trains.
                } else {
                    System.out.println ("There are no connections from " + departure + " station to " + arrival + " station in the near future.");
                }
            } else {
                System.out.println ("Unfortunately we couldn't find the train stations for you.");
            }
            System.out.println ("----------------------------------------------");
            System.out.print ("Happy? Want to search for more departures? (y/n) ");
            String answer=reader.nextLine ();
            if (answer.equals ("n")) {
                return;
            }
        }
    }

    private String getStation(String wanted) { //  Tältä voidaan kysyä sekä lähtö että saapumisasemaa
        String station="";
        System.out.print ("Write the name of the " + wanted + " station: ");
        int givingUp=0;
        while (true) {
            station=reader.nextLine ();
            if (bgrdata.isKey (station)) {
                break;
            } else {
                helpCustomerFindStation (station);
                givingUp++;
                if (givingUp > 3) {
                    if (offerGivingUp ()) {
                        return null;
                    }
                }
                continue;
            }
        }
        return station;
    }

    private void helpCustomerFindStation(String station) {
        List<String> nearestMatches=bgrdata.getNearestMatches (station);
        System.out.println ("Did you mean for example ");
        nearestMatches.stream ().forEach (System.out::println);
        System.out.println ("Please write the full name of the station.");
    }

    private boolean offerGivingUp() {
        System.out.println ("Sometimes it's better to stay still than constantly be on the move. Do you want to give up? (y/n)");
        String answer=reader.nextLine ();
        if (answer.equals ("y")) {
            return true;
        }
        return false;
    }

    private String getDestinationStationName(Train train) {
        List<TimeTableRow> timeTable=train.getTimeTableRows ();
        String shortCode=timeTable.get (timeTable.size () - 1).getStationShortCode ();
        return bgrdata.getStationName (shortCode);
    }

    private void printDepartureScheduleFromOneStation(List<Train> trains, String departure, String departureShortCode) {

        System.out.println ("");
        System.out.println ("");
        System.out.println ("Looking for trains leaving from " + departure + "station:");
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
        System.out.println ("                          TIMETABLE                                     ");
        System.out.println ("---------------------------------------------------------------------------");
        System.out.println ("Date & time \t     Destination  \t \t \t    Type of Train  ");
        System.out.println ("---------------------------------------------------------------------------");
        for (Train train : trains) {
            List<TimeTableRow> timetable=train.getTimeTableRows ();
            LocalDateTime departureTime=getScheduledTime (timetable, "DEPARTURE", departureShortCode);
            System.out.println (datef.format (departureTime) + " " + timef.format (departureTime) +" \t " + getDestinationStationName (train) + "  \t \t \t " + train.getTrainCategory () + " ");
        }
    }

    private void printDepartureAndArrivalWithDateAndTime(List<Train> trains, String departureShortCode, String arrivalShortCode) {
        System.out.println ("Timetable from: " + bgrdata.getStationName (departureShortCode) + " to " + bgrdata.getStationName (arrivalShortCode));
        for (Train train : trains) {
            List<TimeTableRow> timetable=train.getTimeTableRows ();
            LocalDateTime departureTime=getScheduledTime (timetable, "DEPARTURE", departureShortCode);
            LocalDateTime arrivalTime=getScheduledTime (timetable, "ARRIVAL", arrivalShortCode);
            System.out.println (datef.format(departureTime) + " " + timef.format(departureTime) + " \t "+  timef.format(arrivalTime ) + " \t " + train.getTrainCategory () + " ");

        }
    }

    private LocalDateTime getScheduledTime(List<TimeTableRow> schedule, String depOrArr, String stationShortCode) {
        LocalDateTime scheduledTime=null;
        for (TimeTableRow ttr : schedule) {
            if (ttr.getStationShortCode ().equals (stationShortCode) && ttr.getType ().equals (depOrArr)) {
                scheduledTime=ttr.getLocalDateTime ();//TimeTableRows method returns LocalDateTime in the final version.
            }
        }
        return scheduledTime;
    }

    private List<Train> leavingTrains(List<Train> departure, String departureShortCode) {
        Iterator<Train> it=departure.iterator ();
        while (it.hasNext ()) {
            if (!it.next ().getTimeTableRows ().get (0).getStationShortCode ().equals (departureShortCode)) {
                it.remove ();
            }
        }
        return departure;
    }

    private void trainLateOrInTime() {
        System.out.println ("Sorry, we have no idea if your train's on time or not. Neither as VR..");
    }
}
