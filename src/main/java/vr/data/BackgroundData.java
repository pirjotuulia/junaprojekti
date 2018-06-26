package vr.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;

import java.util.*;


public class BackgroundData {

    private Map<String, String> stationShortCodes; //esitellään Hashmap

    public BackgroundData() { //parametriton konstruktoi

        this.stationShortCodes = new HashMap<>(); //alustetaan hashmap

        try {
            String baseurl="https://rata.digitraffic.fi/api/v1";
            URL url=new URL (URI.create (String.format ("%s/metadata/stations", baseurl)).toASCIIString()); // haettiin nämä URL datat, jotta saadaan
            ObjectMapper mapper=new ObjectMapper();
            CollectionType tarkempiListanTyyppi=mapper.getTypeFactory ().constructCollectionType (ArrayList.class, Stations.class);
            List<Stations> stations=mapper.readValue (url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi

            for (Stations st : stations) {
                this.stationShortCodes.put(st.getStationName(), st.getStationShortCode());
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public boolean isKey(String answer) { // method that tells the user if the map contains the key
        return this.stationShortCodes.containsKey (answer);
    }

    public String getShortCode(String nameOfStation) { // method that should return the shortcode if found, null if not found
        if (isKey (nameOfStation)) {
            return this.stationShortCodes.get (nameOfStation);
        } else {
            return null;
        }

    }

    public List<String> getNearestMatches(String match) {
        // method that would return List of nearest String if similar String are found in Map
        List<String> nearest = new ArrayList<>();  // luodaan lista
        for (String name: stationShortCodes.keySet()){ // käy läpi jokaisen keysetin alkion
            if (name.startsWith(match)) { // ottaa huomioon kaikki, jotka matchaavat kirjoitettuun alkuun
                nearest.add(name); // lisää alkiot listaan
            }
        }
        return nearest; // palauttaa listan lähimmistä asemista
    }
}

