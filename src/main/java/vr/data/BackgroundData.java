package vr.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BackgroundData {

    public Map<String, String> generateShortCodes() {

        String baseurl="https://rata.digitraffic.fi/api/v1";
        Map<String, String> map =new HashMap<> ();

        try {
            URL url=new URL (URI.create (String.format ("%s/metadata/stations", baseurl)).toASCIIString ()); // haettiin nämä URL datat, jotta saadaan
            ObjectMapper mapper=new ObjectMapper ();
            CollectionType tarkempiListanTyyppi=mapper.getTypeFactory ().constructCollectionType (ArrayList.class, Stations.class);
            List<Stations> stations=mapper.readValue (url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi

            for (Stations st : stations) {
                map.put(st.getStationName (), st.getStationShortCode ());
            }
            System.out.println (map);


        } catch (Exception ex) {
            System.out.println (ex);
        }

        return map;
    }

}
