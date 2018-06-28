package vr.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

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
            //CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, WeatherClass.class);
            weather = mapper.readValue(url, WeatherClass.class);

//            Iterator<Train> trainsIterator = weather.iterator();
//            while (trainsIterator.hasNext()) {
//                Train train = trainsIterator.next();
//                if (!(train.trainCategory.equals("Long-distance") || train.trainCategory.equals("Commuter"))) {
//                    trainsIterator.remove();
//                }
//            }


        } catch (Exception ex) {
            System.out.println(ex);
        }

        return weather;
    }


}


