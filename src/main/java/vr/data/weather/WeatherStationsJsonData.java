package vr.data.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeatherStationsJsonData {
    private List<WeatherStation> weatherStationsFI;

    public List<WeatherStation> readWeatherStationJson() {//luetaan dataa: tässä luettiin ensin koko maailmaa koskeva file, muutettiin se
        try {
            File file = new File("weatherInFI.json");
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, WeatherStation.class);
            this.weatherStationsFI = mapper.readValue(file, tarkempiListanTyyppi);


        } catch (Exception ex) {
            System.out.println(ex);
        }

//        Iterator<WeatherStation> it = weatherStations.iterator();
//        while (it.hasNext()) {
//            if (!it.next().getCountry().equals("FI")) {
//                it.remove();
//            }
//        }
        return weatherStationsFI;
    }

//    public void weatherStationsInFinlandToJson() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            objectMapper.writeValue(new File("weatherInFI.json"), this.weatherStations);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
