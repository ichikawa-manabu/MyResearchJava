package org.ichilab.googleapi;

import org.json.JSONObject;

/**
 * Created by manabu on 2017/01/14.
 */
public class GoogleApiMain {
    public static void main(String args[]) {
        GoogleApi ga = new GoogleApi(args[0]);
        JSONObject jsonObject = ga.textSearch("東京タワー", "ja");
        System.out.println(jsonObject);
    }
}
