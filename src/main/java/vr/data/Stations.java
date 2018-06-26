package vr.data;

public class Stations {

    private boolean passengerTraffic;
    private String type;
    private String stationName;
    private String stationShortCode;
    private int stationUICCode;
    private String countryCode;
    private int longitude;
    private int latitude;

    public boolean isPassengerTraffic() {
        return passengerTraffic;
    }

    public void setPassengerTraffic(boolean passengerTraffic) {
        this.passengerTraffic=passengerTraffic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type=type;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName=stationName;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode=stationShortCode;
    }

    public int getStationUICCode() {
        return stationUICCode;
    }

    public void setStationUICCode(int stationUICCode) {
        this.stationUICCode=stationUICCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode=countryCode;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude=longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude=latitude;
    }


}

