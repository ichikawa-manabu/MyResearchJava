import java.io.*;
import java.util.HashMap;

/**
 * Created by manabu on 2017/01/23.
 */
public class Mesh5thCustom {
    public static void main(String args[]) {
        File meshBaseFile = new File("/Users/manabu/Desktop/mesh5DB.csv");
        File meshXY = new File("/Users/manabu/Desktop/mesh5thXY.csv");
        File meshCustomFile = new File("files/mesh5DB/mesh5thCustom.csv");
        File meshCustomFileSD = new File("files/mesh5DB/mesh5thCustomSD.csv");
        File mesh5thDB = new File("files/mesh5DB/mesh5thDB.csv");
        File mesh4thStaticalData = new File("files/mesh5DB/mesh4thData.csv");

        //create5thDB(meshBaseFile, meshCustomFile);
        addStaticalData(meshCustomFile, mesh4thStaticalData, meshCustomFileSD);
        addXY(meshCustomFileSD, meshXY, mesh5thDB);
    }

    public static void create5thDB(File inFile, File outFile) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = br.readLine();
            String pair[] = line.split(",");
            String str = pair[0] + "," + pair[1] + "," + pair[2] + "," + pair[3] + "," + pair[4] + ",BLOCK," + pair[6] + "," + pair[7] + ",ES,AREA";
            pw.print(str);
            while ((line = br.readLine()) != null) {
                pair = line.split(",");
                str = pair[0] + "," + pair[1] + "," + pair[2] + "," + pair[3] + "," + pair[4] + ",," + pair[6] + "," + pair[6] + pair[7];
                if (pair[5].length() == 7) {
                    str = str + ",0" + pair[5];
                } else {
                    str = str + "," + pair[5];
                }
                str = str + "," + pair[6] + pair[7] + pair[8] + pair[9];
                pw.print("\n" + str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addStaticalData(File inFile1, File inFile2, File outFile) {
        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
             BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 4次メッシュのデータベース化
            HashMap<String, String> dataMap = new HashMap<String, String>();
            String line = br2.readLine(); // 1行目は見出し
            while((line = br2.readLine()) != null) {
                String pair[] = line.split(",");
                dataMap.put(pair[0], line);
            }

            line = br1.readLine();
            pw.print(line + ",NUM_P,NUM_M,NUM_W,NUM_H");
            while((line = br1.readLine()) != null) {
                String pair[] = line.split(",");
                if(dataMap.containsKey(pair[1])) {
                    String data = dataMap.get(pair[1]);
                    String tmp[] = data.split(",");
                    pw.print("\n" + line + "," + Double.parseDouble(tmp[1]) / 4 + "," + Double.parseDouble(tmp[2]) / 4 + "," + Double.parseDouble(tmp[3]) / 4 + "," + Double.parseDouble(tmp[4]) / 4);
                }
                else {
                    pw.print("\n" + line + ",0,0,0,0");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addXY(File inFile1, File inFile2, File outFile) {
        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "Shift_JIS"));
             BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "Shift_JIS"));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            // 5次メッシュのデータベース化
            HashMap<String, String> dataMap = new HashMap<String, String>();
            String line = br2.readLine(); // 1行目は見出し
            while((line = br2.readLine()) != null) {
                String pair[] = line.split(",");
                dataMap.put(pair[2], "," + pair[0] + "," + pair[1]);
            }

            line = br1.readLine();
            pw.print(line + ",X,Y");
            while((line = br1.readLine()) != null) {
                String pair[] = line.split(",");
                pw.print("\n" + line + dataMap.get(pair[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
