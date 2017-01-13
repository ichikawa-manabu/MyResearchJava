import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by manabu on 2017/01/13.
 */
public class GooglePlacesApiTest2 {
    public static void main(String args[]) {
        System.out.println("===== HTTP GET Start =====");
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=東京タワー&language=ja&key=AIzaSyAu7lFsJdjfpYD6XpG53E-trZ6VNo5q4wI");

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                            StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== HTTP GET End =====");
    }
}
