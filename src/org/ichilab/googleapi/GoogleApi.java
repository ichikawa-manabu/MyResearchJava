package org.ichilab.googleapi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by manabu on 2017/01/14.
 */
public class GoogleApi {
    private String key;

    public GoogleApi() {

    }

    public GoogleApi(String key) {
        this.key = key;
    }

    private getResponse

    private JSONObject TextSearch(String str, String language) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + str +
                    "&language=" + language + "&key=" + key);
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
                            builder.append(line);
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

        JSONObject jsonObject = new JSONObject(builder.toString());
        return jsonObject;
    }
}
