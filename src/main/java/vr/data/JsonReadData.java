package vr.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonReadData {

    public ArrayList<Train> getTimeTable(String departure) {
        if (departure.isEmpty()) {
            return null;
        }
        departure = departure.toUpperCase() + "?arrived_trains=0&arriving_trains=0&departed_trains=0&departing_trains=10&include_nonstopping=false";
        return listOfTrains(departure);
    }

    public ArrayList<Train> getTimeTable(String departure, String arrival) {
        if (departure.isEmpty() || arrival.isEmpty()) {
            return null;
        }
        departure = departure.toUpperCase().replace("/", "");
        arrival = arrival.toUpperCase().replace("/", "");

        String stationsCombined = departure + "/" + arrival;

        return listOfTrains(stationsCombined);
    }

    private ArrayList<Train> listOfTrains(String stations) {
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        ArrayList<Train> trains = new ArrayList<>();

        try {
            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + stations, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);
            trains = mapper.readValue(url, tarkempiListanTyyppi);

            Iterator<Train> trainsIterator = trains.iterator();
            while (trainsIterator.hasNext()) {
                Train train = trainsIterator.next();
                if (!(train.trainCategory.equals("Long-distance") || train.trainCategory.equals("Commuter"))) {
                    trainsIterator.remove();
                }
            }


        } catch (Exception ex) {
            //System.out.println(ex);
        }

        return trains;
    }


}


