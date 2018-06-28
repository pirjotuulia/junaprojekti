package vr;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.Test;
import vr.data.weather.*;
import vr.data.train.*;
import vr.data.*;


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
        System.out.println(weatherJsonData.getWeatherData("Helsinki"));
        System.out.println();

    }

    @Test
    public void testJsonClass(){
        jsonReadData = new JsonReadData();
        System.out.println(jsonReadData.getTimeTable("HKI"));
    }

}
