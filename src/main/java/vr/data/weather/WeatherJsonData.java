package vr.data.weather;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URL;

public class WeatherJsonData {

    public WeatherClass getWeatherData(String cityName) {
        String baseurl = "http://api.openweathermap.org/data/2.5";
        String apikey = "&APPID=cb564e465047c2ab8464ca869909b099&units=metric";
        WeatherClass weather = null;
        String city = cityName;
        String locale = ",fi";

        try {
            URL url = new URL(URI.create(String.format("%s/weather?q=" + city + locale + apikey, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            weather = mapper.readValue(url, WeatherClass.class);


        } catch (Exception ex) {
            System.out.println(ex);
        }

        return weather;
    }


}


