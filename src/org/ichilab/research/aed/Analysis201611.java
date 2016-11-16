package org.ichilab.research.aed;

import java.io.*;
import java.util.HashMap;

/**
 * Created by manabu on 2016/11/15.
 */
public class Analysis201611 {
    public static void main(String args[]) throws IOException {
        File routeFile = new File("/Users/manabu/Documents/SOARS/model/Active/20161117-19_日本救急医学会_AEDモデル/100回実験/ルート.csv");
        File logDir = new File("/Users/manabu/Documents/SOARS/model/Active/20161117-19_日本救急医学会_AEDモデル/100回実験");
        File outFile = new File("/Users/manabu/Documents/SOARS/model/Active/20161117-19_日本救急医学会_AEDモデル/100回実験/結果結合.tsv");

        BufferedReader brRoute = new BufferedReader(new InputStreamReader(new FileInputStream(routeFile), "Shift_JIS"));
        String route = brRoute.readLine();
        HashMap<String, String> routeDB = new HashMap<>();
        while((route = brRoute.readLine()) != null) {
            String pair[] = route.split(",");
            routeDB.put("kumHouseholdSpot" + pair[0], pair[1] + "\t" + pair[2]);
        }
        brRoute.close();

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));

        for(int i=1; i<=100; i++) {
            File logFile = new File(logDir.getPath() + "/" + Integer.toString(i) + "/userdata/log_agent.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "UTF-8"));
            String line = br.readLine();
            if(i==1) {
                pw.print("no\t" + line + "\tAED\tDistance");
            }
            while((line = br.readLine()) != null) {
                String pair[] = line.split("\t");
                if(routeDB.containsKey(pair[4])) {
                    pw.print("\n" + Integer.toString(i) + "\t" + line + "\t" + routeDB.get(pair[4]));
                }
                else {
                    pw.print("\n" + Integer.toString(i) + "\t" + line + "\t-\t300");
                }

            }
            br.close();
        }
        pw.close();
    }
}
