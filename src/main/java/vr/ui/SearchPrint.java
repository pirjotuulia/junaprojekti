package vr.ui;

import vr.data.train.BackgroundData;
import vr.data.DistanceCalculator;
import vr.data.train.TimeTableRow;
import vr.data.train.Train;
import vr.data.weather.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.lang.String;

public class SearchPrint {
    private Locale loc;
    private DateTimeFormatter datef;
    private DateTimeFormatter timef;
    private BackgroundData bgrdata;
    private DistanceCalculator dc;
    private ConnectWeatherToStation cw;
    private WeatherJsonData weatherJsonData;

    public SearchPrint(BackgroundData bgrdata) {
//        this.loc = new Locale("fi", "FI"); //not in use
        this.datef = DateTimeFormatter.ofPattern("dd.MM.");
        this.timef = DateTimeFormatter.ofPattern("kk.mm");
        this.bgrdata = bgrdata;
        this.dc = new DistanceCalculator();
        this.cw = new ConnectWeatherToStation();
        this.weatherJsonData = new WeatherJsonData();
    }

    public void header() {
        emptyLines(10);
        System.out.println("=============================================");
        System.out.println("     Explore Finland by train?");
        System.out.println("=============================================\n");
        System.out.println("No trains today!\n");
        System.out.println("Just kidding.. ;-)\n");
        System.out.println("=============================================");

    }


    public void select() {
        System.out.println("\n Please choose from the options below:  \n");
        System.out.println(" 1) Search trains: from location");
        System.out.println("              Trains leaving from where you are now or any other station.");
        System.out.println("              If you just want to get away, no matter where you go!\n");
        System.out.println(" 2) Search trains: direct connections ");
        System.out.println("             Trains going to a specific destination from where you are now.");
        System.out.println("             For the destination oriented!\n");
        System.out.println(" 3) Is my train on time?");
        System.out.println("             Could be...");
        System.out.println("             but maybe you still have time to go for a cup of coffee and ice cream before it leaves?\n");
        System.out.println(" 4) Exit\n");
        System.out.print("Your choice: ");

    }

    public void resultHeader(String departure) {//art by Kaarina
        emptyLines(2);
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

    public void departureScheduleFromOneStation(List<Train> trains, String departure, String departureShortCode) { //printing trains departing from given station
        double longitude = getCoordinate(departureShortCode, "longitude");
        double latitude = getCoordinate(departureShortCode, "latitude");
        String nearestWeatherCity = cw.findNearest(longitude, latitude);

        System.out.println();
        System.out.println("Below you can find trains that are leaving next from " + departure);
        if (nearestWeatherCity != null) {//weather is given only if it's available from a nearby weather station
            WeatherClass weather = weatherData(nearestWeatherCity);
            weather(weather, nearestWeatherCity);
        }
        System.out.println("");
        System.out.println("                                     TIMETABLE                                     ");
        System.out.println("=========================================================================================");
        System.out.println("Departure time \t \t    Destination  \t\t  Type of Train  \t  Distance to destination ");
        System.out.println("=========================================================================================");
        for (Train train : trains) {
            List<TimeTableRow> timetable = train.getTimeTableRows();
            LocalDateTime departureTime = getScheduledTime(timetable, "DEPARTURE", departureShortCode);
            String arrivalShortCode = bgrdata.getShortCode(getDestinationStationName(train));
            double distance = Math.round(dc.calculateDistance(getCoordinate(departureShortCode, "latitude"), getCoordinate(departureShortCode, "longitude"), getCoordinate(arrivalShortCode, "latitude"), getCoordinate(arrivalShortCode, "longitude")));
            System.out.println(String.format("%4s %4s %20s %20s %20s", datef.format(departureTime), timef.format(departureTime), getDestinationStationName(train), train.getTrainCategory(), distance + " km\t"));
            if (train.getTimeTableRows().get(0).getDifferenceInMinutes() > 0) {
                System.out.println(" Train is " + train.getTimeTableRows().get(0).getDifferenceInMinutes() + " minutes late.");
            }
            System.out.println("");
        }
    }

    private void weather(WeatherClass weather, String station) {//print out the weather for a station
        System.out.println("\nThe temperature in " + station + " is " + weather.getMain().getTemp() + " and it looks like there will be " + weather.getWeather().get(0).getDescription() + ".");
    }

    public void departureAndArrivalWithDateAndTime(List<Train> trains, String departureShortCode, String arrivalShortCode) {//printing trains between two stations
        String departureStation = bgrdata.getStationName(departureShortCode);
        String arrivalStation = bgrdata.getStationName(arrivalShortCode);
        double longitude = getCoordinate(arrivalShortCode, "longitude");
        double latitude = getCoordinate(arrivalShortCode, "latitude");
        String nearestWeatherCity = cw.findNearest(longitude, latitude);//find out if there's a weather station near the railway station
        double distance = Math.round(dc.calculateDistance(getCoordinate(departureShortCode, "latitude"), getCoordinate(departureShortCode, "longitude"), getCoordinate(arrivalShortCode, "latitude"), getCoordinate(arrivalShortCode, "longitude")));
        System.out.println("");
        System.out.println("Timetable from: " + departureStation + " to " + arrivalStation);
        System.out.println("Distance between " + departureStation + " and " + arrivalStation + " is "
                + distance
                + " km");
        if (nearestWeatherCity != null) {
            WeatherClass weather = weatherData(nearestWeatherCity);
            weather(weather, nearestWeatherCity);
        }
        System.out.println("\n \n");
        System.out.println("                     TIMETABLE                              ");
        System.out.println("============================================================");
        ;
        System.out.println("Departure time \t    Arrival time \t   Type of Train  ");
        System.out.println("============================================================");
        ;
        for (Train train : trains) {
            List<TimeTableRow> timetable = train.getTimeTableRows();
            LocalDateTime departureTime = getScheduledTime(timetable, "DEPARTURE", departureShortCode);
            LocalDateTime arrivalTime = getScheduledTime(timetable, "ARRIVAL", arrivalShortCode);
            System.out.print(String.format("%4s %4s %15s %20s", datef.format(departureTime), timef.format(departureTime), timef.format(arrivalTime), train.getTrainCategory() + " "));
            if (train.getTimeTableRows().get(0).getDifferenceInMinutes() > 0) {
                System.out.print(" Train is late " + train.getTimeTableRows().get(0).getDifferenceInMinutes() + " minutes.");
            }
            System.out.println("");
        }
    }

    public LocalDateTime getScheduledTime(List<TimeTableRow> schedule, String depOrArr, String stationShortCode) {//get time for a departure or arrival to a given station
        LocalDateTime scheduledTime = null;
        for (TimeTableRow ttr : schedule) {
            if (ttr.getStationShortCode().equals(stationShortCode) && ttr.getType().equals(depOrArr)) {
                scheduledTime = ttr.getLocalDateTime();//TimeTableRows method returns LocalDateTime in the final version.
            }
        }
        return scheduledTime;
    }

    private String getDestinationStationName(Train train) { //this assumes that the destination is the last row of the timetable.
        if (train.getCommuterLineID().equals("P") || train.getCommuterLineID().equals("I")) {
            return "LENTOASEMA";
        } else {
            List<TimeTableRow> timeTable = train.getTimeTableRows();
            String shortCode = timeTable.get(timeTable.size() - 1).getStationShortCode();
            return bgrdata.getStationName(shortCode);
        }
    }

    private double getCoordinate(String stationShortCode, String wantedParameter) { //get station coordinates for calculating the distance
        if (wantedParameter.equals("latitude")) {
            return bgrdata.getStation(stationShortCode).getLatitude();
        } else if (wantedParameter.equals("longitude")) {
            return bgrdata.getStation(stationShortCode).getLongitude();
        }
        return 0;
    }

    private WeatherClass weatherData(String name) {//get weather from the weather api
        WeatherClass weatherClass = weatherJsonData.getWeatherData(name);
        return weatherClass;
    }

    private void emptyLines(int numberOfLines) {//print wanted amount of empty lines
        for (int i=0; i < numberOfLines; i++) {
            System.out.println("");
        }
    }
}
