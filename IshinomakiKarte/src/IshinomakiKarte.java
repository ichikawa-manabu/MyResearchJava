import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by manabu on 2017/04/06.
 */
public class IshinomakiKarte {

    public static void main(String args[]) {

        String str  = "高血圧";
        //File inFile = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ0_カルテID有り.csv");
        File inFile = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ0_カルテID有_最終列判定入り.csv");
        File outFile = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ0_カルテID有_最終列判定入り_sort済.csv");
        File outFile1 = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ1_高血圧文字含むレコードのみ.csv");
        File outFile2 = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ2_高血圧出現患者の全診療レコード.csv");
        File outFile3 = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ3_高血圧出現患者の全診療レコード_項目絞り込み.csv");
        File outFile4 = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ4_高血圧出現患者の全診療レコード_項目絞り込み_患者レコード結合.csv");

        File outFile99 = new File("/Users/manabu/Desktop/東日本大震災 石巻カルテデータ分析中/東日本大震災 石巻カルテデータ99_列数確認.csv");

        String fileEncording = "Shift_JIS";

        checkColNum(inFile, outFile99, fileEncording);
        resort(inFile, outFile, fileEncording);
        analysis01(outFile, outFile1, fileEncording, str);
        analysis02(outFile, outFile1, outFile2, fileEncording);
        analysis03(outFile2, outFile3, fileEncording);
        analysis04(outFile3, outFile4, fileEncording);

    }

    public static void checkColNum(File inFile, File outFile, String fileEncording) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            // 全レコードの列数を確認する
            String line = br.readLine(); // 1行目は見出し
            String[] pair = line.split(",");
            pw.write(pair[0] + "," + pair.length);
            while ((line = br.readLine()) != null) {
                pair = line.split(",");
                pw.write("\n" + pair[0] + "," + pair.length);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resort(File inFile, File outFile, String fileEncording) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            // 全レコードの列数を並び替える
            String line = br.readLine(); // 1行目は見出し
            pw.write(line);

            // 患者ごとのDBを作る
            HashMap<String, HashMap<String, String>> db = new HashMap<>();
            HashMap<String, Integer> db2 = new HashMap<>();


            while((line=br.readLine())!=null) { // 先ずは患者ごとにデータベースにレコードを記録する。キーは診療No.
                String[] pair = line.split(",");
                if(!db.containsKey(pair[0])) { // まだ患者がDBに登録されていない場合
                    HashMap<String, String> tmp = new HashMap<>();
                    db.put(pair[0], tmp);
                    db2.put(pair[0], 0);
                }

                HashMap<String, String> pDB = db.get(pair[0]); // 個人の記録DBを取得。
                int counter = db2.get(pair[0]);
                counter++;
                pDB.put(pair[62], line); // 診療ナンバーを使って登録
                db.put(pair[0], pDB);
                db2.put(pair[0], counter);
            }

            int count = 0;
            // 診療No.の入れ替え
            for(String id : db.keySet()) {
                HashMap<String, String> pDB = db.get(id);
                count = count + pDB.size();
                LinkedList<String> yesDate = new LinkedList<>(); // 診療日があるもの
                LinkedList<String> noDate = new LinkedList<>(); // 診療日がないもの
                for(String no : pDB.keySet()) {
                    String[] str = pDB.get(no).split(",");

                    if(str[63].equals("")) { // 診療日がない場合の処理
                        noDate.addLast(pDB.get(no));
                    }
                    else { // 診療日がある場合の処理
                        String date1 = str[63];
                        if(yesDate.size()==0) {
                            yesDate.addLast(pDB.get(no));
                        }
                        else {
                            for(int i=0; i<yesDate.size() ; i++) {
                                String[] tmp = yesDate.get(i).split(",");
                                String date2 = tmp[63];

                                // System.out.println(date1 + " : " + date2);

                                String[] pairDate1 = date1.split("\\.");
                                String[] pairDate2 = date2.split("\\.");

                                if(Integer.parseInt(pairDate1[0]) < Integer.parseInt(pairDate2[0])) { // 西暦の比較
                                    yesDate.add(i, pDB.get(no));
                                    // System.out.println("YYYY");
                                    break;
                                }
                                else if (Integer.parseInt(pairDate1[0]) == Integer.parseInt(pairDate2[0])) { // 西暦が同じ場合
                                    if(Integer.parseInt(pairDate1[1]) < Integer.parseInt(pairDate2[1])) { // 月の比較
                                        yesDate.add(i, pDB.get(no));
                                        // System.out.println("YYYYMM");
                                        break;
                                    }
                                    else if(Integer.parseInt(pairDate1[1]) == Integer.parseInt(pairDate2[1])) { // 月が同じ場合
                                        if(Integer.parseInt(pairDate1[2]) < Integer.parseInt(pairDate2[2])) { // 月の比較
                                            yesDate.add(i, pDB.get(no));
                                            // System.out.println("YYYYMMDD");
                                            break;
                                        }
                                        else if(Integer.parseInt(pairDate1[2]) == Integer.parseInt(pairDate2[2])) { // 月の比較
                                            yesDate.add(i, pDB.get(no));
                                            // System.out.println(id);
                                            break;
                                        }
                                    }
                                }

                                if(i==(yesDate.size()-1)) {
                                    yesDate.addLast(pDB.get(no));
                                    // System.out.println("-----");
                                    break;
                                }
                            }
                        }
                    }
                }

                if(db2.get(id) != (yesDate.size() + noDate.size())) {
                    System.out.println(id + " : " +  db2.get(id) + " : " + yesDate.size() + noDate.size());
                }

                int index = 1;
                for(String s : yesDate) {
                    String[] record = s.split(",");
                    record[62] = Integer.toString(index);
                    pw.write("\n" + record[0]);
                    for(int k=1; k<record.length; k++) {
                        pw.write("," + record[k]);
                    }
                    index++;
                }
                for(String s : noDate) {
                    String[] record = s.split(",");
                    record[62] = Integer.toString(index);
                    record[63] = "-";
                    pw.write("\n" + record[0]);
                    for(int k=1; k<record.length; k++) {
                        pw.write("," + record[k]);
                    }
                    index++;
                }
            }
            System.out.println(count);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        /**
     * ある傷病（既往歴含む）が出現するレコードだけを抽出するプログラム
     * @param inFile 全レコード
     * @param outFile 出力ファイル
     * @param fileEncording 文字コード
     * @param str 傷病名
     */
    public static void analysis01(File inFile, File outFile, String fileEncording, String str) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            // 全レコードから、傷病が出現するレコードを抽出する
            String line = br.readLine(); // 1行目は見出し
            pw.write(line);
            while((line = br.readLine()) != null) {
                if(line.contains(str)) {
                    pw.write("\n" + line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ある傷病（既往歴含む）で診療を受けたことがある全患者の医療レコードを抽出するプログラム
     * @param inFile1 全レコード
     * @param inFile2 ある傷病の出現で絞り込んだレコード
     * @param outFile 出力ファイル
     * @param fileEncording 文字コード
     */
    public static void analysis02(File inFile1, File inFile2, File outFile, String fileEncording) {
        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), fileEncording));
             BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            // ある傷病が出現したことがある患者のIDを集める
            String line2 = br2.readLine(); // 1行目は見出し
            HashSet<String> pID = new HashSet<>(); // 抽出対象のカルテIDの集合
            while((line2=br2.readLine())!=null) {
                String[] pair = line2.split(",");
                pID.add(pair[0]); // 1つ目の要素がカルテID
            }

            // 全レコードから抽出対象のカルテIDのレコードだけを出力ファイルに書き込む
            String line1 = br1.readLine(); // 1行目は見出し
            pw.write(line1);
            while((line1=br1.readLine())!=null) {
                String[] pair = line1.split(",");
                if(pID.contains(pair[0])) { // 読み込んだレコードが抽出対象のカルテIDに含まれる場合は書き込み。
                    pw.write("\n" + line1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 傷病分析に必要な列だけに絞り込む
     * @param inFile 絞り込み対象のレコード
     * @param outFile 出力ファイル
     * @param fileEncording 文字コード
     */
    public static void analysis03(File inFile, File outFile, String fileEncording) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            int[] index = {1, 5, 7, 12, 14, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 56, 57, 58, 59}; // 患者情報
            int[] index2 = {63, 64, 70, 71, 72, 73, 74, 75, 76}; // 診察情報
            int[] index3 = {143,144,145,146,147,148,150,154}; // 傷病情報
            int[] index4 = {333,334,335,336,337}; // 医療品情報

            String line;
            boolean midashi = true;
            while ((line = br.readLine()) != null) {
                String[] pair = line.split(",");

                // 患者情報の書き込み
                if (midashi) {
                    for (int i = 0; i < index.length; i++) {
                        if (i == 0) {
                            pw.write(pair[index[i] - 1]);
                        } else {
                            pw.write("," + pair[index[i] - 1]);
                        }
                    }
                } else {
                    for (int i = 0; i < index.length; i++) {
                        if (i == 0) {
                            pw.write("\n" + pair[index[i] - 1]);
                        } else {
                            pw.write("," + pair[index[i] - 1]);
                        }
                    }
                }

                // 診察情報の書き込み
                for (int i = 0; i < index2.length; i++) {
                    pw.write("," + pair[index2[i] - 1]);
                }

                // 傷病情報の書き込み
                for(int j = 0; j<10; j++) {
                    for (int i = 0; i < index3.length; i++) {
                        pw.write("," + pair[(index3[i] - 1) + 19 * j]);
                    }
                }

                // 傷病情報の書き込み
                for(int j = 0; j<22; j++) {
                    for (int i = 0; i < index4.length; i++) {
                        pw.write("," + pair[(index4[i] - 1) + 13 * j]);
                    }
                }

                if(midashi) {
                    pw.write(",最終列判定");
                    midashi = false;
                }
                else {
                    pw.write(",1");
                }
            }
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }



    //
    public static void analysis3(File inFile, File outFile, String fileEncording, String msg) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            String line = br.readLine();
            String[] cols = line.split(",");
            pw.write(cols[0] + "," + cols[30] + ",受診回数,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日,診療日");

            HashMap<String, String> karte = new HashMap<>();

            while((line = br.readLine()) != null) {
                String[] str = line.split(",");
                if (!karte.containsKey(str[0])) {
                    karte.put(str[0], str[0] + ",-");
                }

                String[] s = karte.get(str[0]).split(",");
                String tmp = "";

                for(int i=0; i<str.length; i++) {
                    if (str[i].contains(msg)) {
                        if (i == 30) {
                            s[1] = str[i];
                        } else {
                            if(str[63].equals("")) {
                                tmp = tmp + ",-";
                            }
                            else {
                                tmp = tmp + "," + str[63];
                            }
                        }
                    }
                }

                String tmp1 = str[0];
                for(int i=1; i<s.length; i++) {
                    tmp1 = tmp1 + "," + s[i];
                }
                tmp1 = tmp1 + tmp;
                karte.put(str[0], tmp1);
            }

            for(String s : karte.keySet()) {
                String[] tmp = karte.get(s).split(",");
                pw.write("\n" + tmp[0] + "," + tmp[1] + "," + (tmp.length-2));
                LinkedList<String> list = new LinkedList<>();
                for(int i=2; i<tmp.length; i++) {
                    if(list.isEmpty()) {
                        list.addLast(tmp[i]);
                    }
                    else {
                        if(tmp[i].equals("-")) {
                            list.addFirst(tmp[i]);
                        }
                        else {
                            for(int j=0; j<list.size(); j++) {
                                String s1 = list.get(j);
                                if(!s1.equals("-")) {
                                    String[] pair1 = s1.split("\\.");
                                    String[] pair2 = tmp[i].split("\\.");
                                    if(Integer.parseInt(pair1[0])>Integer.parseInt(pair2[0])) {
                                        list.add(j, tmp[i]);
                                        break;
                                    }
                                    else if(Integer.parseInt(pair1[0])==Integer.parseInt(pair2[0])) {
                                        if(Integer.parseInt(pair1[1])>Integer.parseInt(pair2[1])) {
                                            list.add(j, tmp[i]);
                                            break;
                                        }
                                        else if(Integer.parseInt(pair1[1])==Integer.parseInt(pair2[1])) {
                                            if(Integer.parseInt(pair1[2])>Integer.parseInt(pair2[2])) {
                                                list.add(j, tmp[i]);
                                                break;
                                            }
                                        }

                                    }
                                }
                                if(j==(list.size()-1)) {
                                    list.addLast(tmp[i]);
                                    break;
                                }
                            }
                        }
                    }
                }

                for(String t : list) {
                    pw.write("," + t);
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param inFile
     * @param outFile
     * @param fileEncording
     */
    public static void analysis04(File inFile, File outFile, String fileEncording) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            // 各患者のレコードを保持するDB
            HashMap<String, HashMap<String, String>> db = new HashMap<String, HashMap<String, String>>();

            String midashi = br.readLine();
            String line;
            int maxNum = 0;

            while((line=br.readLine())!=null) { // 先ずは患者ごとにデータベースにレコードを記録する。キーは診療No.
                String[] pair = line.split(",");
                if(!db.containsKey(pair[0])) { // まだ患者がDBに登録されていない場合
                    HashMap<String, String> tmp = new HashMap<>();
                    db.put(pair[0], tmp);
                }

                HashMap<String, String> pDB = db.get(pair[0]); // 個人の記録DBを取得。
                pDB.put(pair[23], line); // 診療ナンバーを使って登録
                db.put(pair[0], pDB);

                if(Integer.parseInt(pair[23]) > maxNum) {
                    maxNum = Integer.parseInt(pair[23]);
                }
            }

            // ファイルへの書き込み
            // 先ずは見出しの書き込み
            String[] str = midashi.split(",");
            pw.write(str[0]);
            for(int i=1; i<23; i++) { // 見出しで診療NO.前まで書き込み
                pw.write("," + str[i]);
            }

            for(int j=1; j<=maxNum; j++) {
                String j_s;
                if(j<10) {
                    j_s = "0" + Integer.toString(j);
                }
                else {
                    j_s = Integer.toString(j);
                }

                pw.write("," + str[24] + "_" + j_s); // 診療日を書き込む
                for (int i = 25; i < 32; i++) { // 見出しで傷病前まで書き込み
                    pw.write("," + str[i] + "_" + j_s);
                }

                // 傷病の記録を行う
                for(int k=1; k<=10; k++) {
                    String k_s;
                    if(k<10) {
                        k_s = "0" + Integer.toString(k);
                    }
                    else {
                        k_s = Integer.toString(k);
                    }
                    for (int i = 32; i < 40; i++) { // 見出しで傷病前まで書き込み
                        pw.write("," + str[i+(k-1)*8] + "_" + j_s + "-" + k_s);
                    }
                }

                // 医薬品の記録を行う
                for(int k=1; k<=22; k++) {
                    String k_s;
                    if(k<10) {
                        k_s = "0" + Integer.toString(k);
                    }
                    else {
                        k_s = Integer.toString(k);
                    }
                    for (int i = 112; i < 117; i++) { // 見出しで傷病前まで書き込み
                        pw.write("," + str[i+(k-1)*5] + "_" + j_s + "-" + k_s);
                    }
                }
            }


            // 患者カルテの書き込み
            for(String id : db.keySet()) {

                HashMap<String, String> pDB = db.get(id); // 個人の記録DBを取得。
                for(int i=1; i<=maxNum; i++) {
                    if(pDB.containsKey(Integer.toString(i))) {
                        if(i==1) {
                            String[] pair = pDB.get(Integer.toString(i)).split(",");
                            pw.write("\n" + pair[0]);
                            for(int j=1; j<23; j++) {
                                pw.write("," + pair[j]);
                            }
                            for(int j=24; j<pair.length-1; j++) {
                                pw.write("," + pair[j]);
                            }
                        }
                        else {
                            String[] pair = pDB.get(Integer.toString(i)).split(",");
                            for(int j=24; j<pair.length-1; j++) {
                                pw.write("," + pair[j]);
                            }
                        }
                    }
                }
                pw.write(",END");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 特定のキーワードがどこの列に入っていたのかをまとめる
    public static void RetainColByWord(File inFile, File outFile, String fileEncording, String msg) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), fileEncording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), fileEncording))) {

            String line = br.readLine();
            String[] cols = line.split(",");
            pw.write(cols[0]);
            for (int i = 1; i < 13; i++) {
                pw.write("," + cols[i]);
            }
            pw.write("," + cols[63] + ",以降出現項目名=出現名");

            while ((line = br.readLine()) != null) {
                String[] str = line.split(",");
                if (line.contains(msg)) {
                    pw.write("\n" + str[0]);
                    for (int i = 1; i < 13; i++) {
                        pw.write("," + str[i]);
                    }
                    if (str.length > 64) {
                        pw.write("," + str[63]);
                    } else {
                        pw.write(",");
                    }
                    for (int i = 0; i < str.length; i++) {
                        if (str[i].contains(msg)) {
                            pw.write("," + cols[i] + "=" + str[i]);
                        }
                    }
                }
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
