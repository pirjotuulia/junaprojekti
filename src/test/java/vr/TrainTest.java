package vr;

import org.junit.Test;
import vr.data.DistanceCalculator;
import vr.data.Stations;
import vr.data.TimeTableRow;
import vr.data.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TrainTest {
    private Train train;
    private TimeTableRow timeTableRow;
    private DistanceCalculator dc;
    private Stations station;


    @Test
    public void testaaTrainOlionLocalDate(){
        train = new Train();
        train.setDepartureDate(new Date());
        LocalDate ld = train.getDepartureLocalDate();
        System.out.println(ld);
        assertEquals(ld, train.getDepartureLocalDate());
        }

    @Test
    public void testTimeTableIsLocalDateTime(){
        timeTableRow = new TimeTableRow();
        timeTableRow.setScheduledTime(new Date());
        LocalDateTime ldt = timeTableRow.getLocalDateTime();
        System.out.println(ldt);
        assertEquals(ldt, timeTableRow.getLocalDateTime());
        }

    @Test
    public void testTheGreatDistance(){
        dc = new DistanceCalculator();

        //
        System.out.println();
        System.out.println(dc.calculateDistance(60.16952, 24.93545, 60.45148, 22.26869));
        System.out.println(dc.calculateDistance(60.16952, 24.93545, 60.45148, 22.26869));
    }
}
