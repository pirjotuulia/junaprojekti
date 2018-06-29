package vr.data.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectWeatherToStation { // find nearest place to given coordinates from the weather station list
    private WeatherStationsJsonData jd;
    private List<WeatherStation> stations;

    public ConnectWeatherToStation() {
        WeatherStationsJsonData jd = new WeatherStationsJsonData();
        this.stations = jd.readWeatherStationJson();
    }

    public String findNearest(double longitude, double latitude) {
        for (WeatherStation w : stations) {
            Coord c = w.getCoord();
            if (Math.abs(c.getLon()-longitude)<0.1&&Math.abs(c.getLat()-latitude)<0.1) {//if lon/lat is within 0.1 with the given lon/lat
                return w.getName();//return the name of the weather station as a string
            }
        }
        return null; //if a weather station is not found above, return null
    }
}
