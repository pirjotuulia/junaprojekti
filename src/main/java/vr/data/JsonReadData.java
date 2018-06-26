package vr.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
Vaatii Jackson kirjaston:
File | Project Structure
Libraries >> Add >> Maven
Etsi "jackson-databind", valitse esimerkiksi versio 2.0.5
Asentuu Jacksonin databind, sekä core ja annotations
 */

public class JsonReadData {

    private String stations;

    public void setStations(String departure){
        //TODO String käsittelijä
        if(departure.isEmpty()){
            System.out.println("Error, station empty");
        }
        departure = departure.toUpperCase();
        this.stations = departure;
    }

    public void setStations(String departure, String arrival){
        //TODO String käsittelijä
        if(departure.isEmpty()){
            System.out.println("Error, station empty");
        }
        departure = departure.toUpperCase().replace("/", "");
        arrival = arrival.toUpperCase().replace("/", "");

        String stationsCombined = departure + "/" + arrival;

        this.stations = stationsCombined;

    }

    public void lueJunanJSONData() {
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + stations, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);
            List<Train> junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi

            //TODO oma aliohjelmansa joka hakee ajat. Nyt vain testaus käyttö.
            for (Train train : junat){

                int junanNumero = train.getTrainNumber();
                String junanType = train.getTrainType();
                String junanCategory = train.getTrainCategory();

                System.out.println("Junan tyyppi: " + junanType + "Junan numero: " + junanNumero + " Junan kategoria: " + junanCategory);

                Date aika = train.getTimeTableRows().get(0).getScheduledTime();
                LocalDate aikaLocalDate = LocalDate.ofInstant(aika.toInstant(), ZoneId.systemDefault());
                System.out.println(aikaLocalDate);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


}


