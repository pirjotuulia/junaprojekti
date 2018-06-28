package vr;


import org.junit.Test;

import vr.data.*;
import vr.data.train.JsonReadData;
import vr.data.train.Stations;
import vr.data.train.TimeTableRow;
import vr.data.train.Train;
import vr.data.weather.WeatherClass;
import vr.data.weather.WeatherJsonData;
import vr.data.weather.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TrainTest {
    private Train train;
    private TimeTableRow timeTableRow;
    private DistanceCalculator dc;
    private Stations station;
    private WeatherJsonData weatherJsonData;
    private WeatherClass weather;
    private JsonReadData jsonReadData;


    @Test
    public void testaaTrainOlionLocalDate() {
        train = new Train();
        train.setDepartureDate(new Date());
        LocalDate ld = train.getDepartureLocalDate();
        System.out.println(ld);
        assertEquals(ld, train.getDepartureLocalDate());
    }

    @Test
    public void testTimeTableIsLocalDateTime() {
        timeTableRow = new TimeTableRow();
        timeTableRow.setScheduledTime(new Date());
        LocalDateTime ldt = timeTableRow.getLocalDateTime();
        System.out.println(ldt);
        assertEquals(ldt, timeTableRow.getLocalDateTime());
    }

    @Test
    public void testTheGreatDistance() {
        dc = new DistanceCalculator();
        //
        System.out.println();
        System.out.println(dc.calculateDistance(60.16952, 24.93545, 60.45148, 22.26869));
        System.out.println(dc.calculateDistance(60.16952, 24.93545, 60.45148, 22.26869));
    }

    @Test
    public void testInstanceofWeatherData() {
        weatherJsonData = new WeatherJsonData();
        //System.out.println(weatherJsonData.getWeatherData("Helsinki"));
        WeatherClass weatherClass = weatherJsonData.getWeatherData("Helsinki");
        Main main = weatherClass.getMain();
        int temp = main.getTemp();
        System.out.println(temp);

    }

    @Test
    public void testJsonClass(){
        jsonReadData = new JsonReadData();
        System.out.println(jsonReadData.getTimeTable("HKI"));
    }

}
