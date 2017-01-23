import java.io.*;

/**
 * Created by manabu on 2017/01/23.
 */
public class Mesh5thCustom {
    public static void main(String args[]) {
        File inFile = new File("/Users/manabu/Desktop/mesh5DB.csv");
        File outFile = new File("/Users/manabu/Desktop/mesh5DBcustom.csv");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "Shift_JIS"));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"))) {

            String line = br.readLine();
            String pair[] = line.split(",");
            String str = pair[0] + "," + pair[1] + "," + pair[2] + "," + pair[3] + "," + pair[4] + ",BLOCK," + pair[6] + "," + pair[7] + ",ES,AREA";
            pw.print(str);
            while((line = br.readLine()) != null) {
                pair = line.split(",");
                str = pair[0] + "," + pair[1] + "," + pair[2] + "," + pair[3] + "," + pair[4] + ",," + pair[6] + "," + pair[6] + pair[7];
                if(pair[5].length()==7) {
                    str = str + ",0" + pair[5];
                }
                else {
                    str = str + "," + pair[5];
                }
                str = str + "," + pair[6] + pair[7] + pair[8] + pair[9];
                pw.print("\n" + str);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
