package vr.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeTableRow {


    private String stationShortCode;
    private int stationUICCode;
    private Locale countryCode;
    private String type;
    private boolean trainStopping;
    private boolean commercialStop;
    private String commercialTrack;
    private boolean cancelled;
    private Date scheduledTime;
    private int differenceInMinutes;

    //TODO toimii, getteri tekee muunnoksen samalla. Halutessa voi tehdä muutokset aliohjelman.
    public LocalDateTime getLocalDateTime() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = scheduledTime.toInstant();
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();

        return localDateTime;
    }

    public int getDifferenceInMinutes() {
        return differenceInMinutes;
    }

    public void setDifferenceInMinutes(int differenceInMinutes) {
        this.differenceInMinutes = differenceInMinutes;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public int getStationUICCode() {
        return stationUICCode;
    }

    public void setStationUICCode(int stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    public Locale getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Locale countryCode) {
        this.countryCode = countryCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTrainStopping() {
        return trainStopping;
    }

    public void setTrainStopping(boolean trainStopping) {
        this.trainStopping = trainStopping;
    }

    public boolean isCommercialStop() {
        return commercialStop;
    }

    public void setCommercialStop(boolean commercialStop) {
        this.commercialStop = commercialStop;
    }

    public String getCommercialTrack() {
        return commercialTrack;
    }

    public void setCommercialTrack(String commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}


