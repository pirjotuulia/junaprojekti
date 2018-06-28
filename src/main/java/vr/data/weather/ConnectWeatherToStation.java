package vr.data.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectWeatherToStation {
    private WeatherStationsJsonData jd;
    private List<WeatherStation> stations;

    public ConnectWeatherToStation() {
        WeatherStationsJsonData jd = new WeatherStationsJsonData();
        this.stations = jd.readWeatherStationJson();
    }

    public String findNearest(double longitude, double latitude) {
        for (WeatherStation w : stations) {
            Coord c = w.getCoord();
            if (Math.abs(c.getLon()-longitude)<0.1&&Math.abs(c.getLat()-latitude)<0.1) {
                return w.getName();
            }
        }
        return null;
    }
}
