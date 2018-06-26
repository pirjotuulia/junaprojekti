package vr.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class JsonReadData {

    private String setStations(String departure){
        //TODO poistetaanko virheviesti?
        if(departure.isEmpty()){
            System.out.println("Error, station empty");
        }
        departure = departure.toUpperCase();
        return departure;
    }

    private String setStations(String departure, String arrival){
        //TODO poistetaanko virheviesti?
        if(departure.isEmpty()){
            System.out.println("Error, station empty");
        }
        departure = departure.toUpperCase().replace("/", "");
        arrival = arrival.toUpperCase().replace("/", "");

        String stationsCombined = departure + "/" + arrival;

        return stationsCombined;

    }

    public ArrayList<Train> getTimeTable(String shortcodes) {
        String stations = setStations(shortcodes);
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        ArrayList<Train> trains = new ArrayList<>();

        try {
            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + stations, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);
            trains = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return trains;
    }


}


