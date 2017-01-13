import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by manabu on 2017/01/12.
 */
public class GeoCoordingTest {
    public static void main(String args[]) {
        GeoCoordingTest gct = new GeoCoordingTest();
        GeocodingResult[] results = gct.getGeocodingResult("防衛医大");
    }

    private GeocodingResult[] getGeocodingResult(String str) {
        // Replace the API key below with a valid API key.
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAu7lFsJdjfpYD6XpG53E-trZ6VNo5q4wI");
        GeocodingResult[] results = null;
        try {

            results = GeocodingApi.geocode(context, str).language("ja").await();
            System.out.println(results[0].formattedAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
