package vr.ui;

import vr.data.BackgroundData;
import vr.data.DistanceCalculator;
import vr.data.TimeTableRow;
import vr.data.Train;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SearchPrint {
    private Locale loc;
    private DateTimeFormatter datef;
    private DateTimeFormatter timef;
    private BackgroundData bgrdata;
    private DistanceCalculator dc;

    public SearchPrint(BackgroundData bgrdata) {
        this.loc = new Locale("fi", "FI");
        this.datef = DateTimeFormatter.ofPattern("dd.MM.");
        this.timef = DateTimeFormatter.ofPattern("kk.mm");
        this.bgrdata = bgrdata;
        this.dc = new DistanceCalculator();
    }

    public void header() {
        System.out.println ("");
        System.out.println("=============================================");
        System.out.println("     Explore Finland by train?");
        System.out.println("=============================================\n");
        System.out.println("No trains today!\n");
        System.out.println("Just kidding.. ;-)\n");
        System.out.println("=============================================");

    }

    public void select() {
        System.out.println ("");
        System.out.println(" Please choose from the options below:  ");
        System.out.println(" 1) Train search: NO spesific destination");
        System.out.println(" 2) Train search: TO a spesific destination");
        System.out.println(" 3) Is my train on time?");
        System.out.println(" 4) Exit\n");
        System.out.println("---------------------------------------------\n");
        System.out.println("Explanations of the search");
        System.out.println(" 1) Search for trains leaving from where you are now or any other station. If you just want to get away, no matter where you go!");
        System.out.println(" 2) Search for trains going to a specific destination from where you are now. For the destination oriented!");
        System.out.println(" 3) Is my train on time? Could be, but maybe you still have time to go for a cup of coffee and ice cream before it leaves?");
        System.out.println(" 4) I keep on trekking, thank you very much!\n");
        System.out.print("Your choice: \n");

    }

    public void resultHeader(String departure) {
        System.out.println("");
        System.out.println("");
        System.out.println("Looking for trains leaving from " + departure + " station:");
        System.out.println("");
        System.out.println("#########################################################");
        System.out.println("___________   _______________________________________^__");
        System.out.println(" ___   ___ |||  ___   ___   ___    ___ ___  |   __  ,----\\");
        System.out.println("|   | |   |||| |   | |   | |   |  |   |   | |  |  | |_____\\");
        System.out.println("|___| |___|||| |___| |___| |___|  | O | O | |  |  |        \\");
        System.out.println("           |||                    |___|___| |  |__|         )");
        System.out.println("___________|||______________________________|______________/");
        System.out.println("           ||| Happiness onboard!    /--------");
        System.out.println("#########################################################");
    }

    public void departureScheduleFromOneStation(List<Train> trains, String departure, String departureShortCode) {
        System.out.println();
        System.out.println("Below you can find trains that are leaving next from " + departure);
        System.out.println("");
        System.out.println("                          TIMETABLE                                     ");
        System.out.println("=========================================================================");
        System.out.println("Date & leaving time \t     Destination  \t \t    Type of Train  ");
        System.out.println("=========================================================================");
        for (Train train : trains) {
            List<TimeTableRow> timetable = train.getTimeTableRows();
            LocalDateTime departureTime = getScheduledTime(timetable, "DEPARTURE", departureShortCode);
            System.out.println(datef.format(departureTime) + " " + timef.format(departureTime) + " \t \t \t " + getDestinationStationName(train) + "  \t \t  " + train.getTrainCategory() + " ");
        }
    }

    public void departureAndArrivalWithDateAndTime(List<Train> trains, String departureShortCode, String arrivalShortCode) {
        String departureStation = bgrdata.getStationName(departureShortCode);
        String arrivalStation = bgrdata.getStationName(arrivalShortCode);
        double distance = Math.round(dc.calculateDistance(getCoordinate(departureShortCode, "latitude"), getCoordinate(departureShortCode, "longitude"), getCoordinate(arrivalShortCode, "latitude"), getCoordinate(arrivalShortCode, "longitude")));
        System.out.println("Timetable from: " + departureStation + " to " + arrivalStation);
        System.out.println("Distance between " + departureStation + " and " + arrivalStation + " is "
                + distance
                + " km");
        System.out.println("                          TIMETABLE                                     ");
        System.out.println("=========================================================================");
        ;
        System.out.println("Leaving time \t     Arrival time \t     Type of Train  ");
        System.out.println("=========================================================================");
        ;
        for (Train train : trains) {
            List<TimeTableRow> timetable = train.getTimeTableRows();
            LocalDateTime departureTime = getScheduledTime(timetable, "DEPARTURE", departureShortCode);
            LocalDateTime arrivalTime = getScheduledTime(timetable, "ARRIVAL", arrivalShortCode);
            System.out.println(datef.format(departureTime) + " " + timef.format(departureTime) + " \t \t \t " + timef.format(arrivalTime) + " \t \t \t " + train.getTrainCategory() + " ");
        }
    }

    public LocalDateTime getScheduledTime(List<TimeTableRow> schedule, String depOrArr, String stationShortCode) {
        LocalDateTime scheduledTime = null;
        for (TimeTableRow ttr : schedule) {
            if (ttr.getStationShortCode().equals(stationShortCode) && ttr.getType().equals(depOrArr)) {
                scheduledTime = ttr.getLocalDateTime();//TimeTableRows method returns LocalDateTime in the final version.
            }
        }
        return scheduledTime;
    }

    private String getDestinationStationName(Train train) {
        List<TimeTableRow> timeTable = train.getTimeTableRows();
        String shortCode = timeTable.get(timeTable.size() - 1).getStationShortCode();
        return bgrdata.getStationName(shortCode);
    }

    private double getCoordinate(String stationShortCode, String wantedParameter) {
        if (wantedParameter.equals("latitude")) {
            return bgrdata.getStation(stationShortCode).getLatitude();
        } else if (wantedParameter.equals("longitude")) {
            return bgrdata.getStation(stationShortCode).getLongitude();
        }
        return 0;
    }


}
