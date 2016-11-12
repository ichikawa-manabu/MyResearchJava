package org.ichilab.KansaiUnivModule;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by manabu on 2016/11/12.
 */
public class Xml2Csv {
    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        File inFile = new File("/Users/manabu/Desktop/saitama_latlng_20161104/11208_tokorozawa_latlng.xml");
        File outFile1 = new File("/Users/manabu/Desktop/saitama_latlng_20161104/11208_tokorozawa_household.csv");
        File outFile2 = new File("/Users/manabu/Desktop/saitama_latlng_20161104/11208_tokorozawa_human.csv");

        convertFile(inFile, outFile1, outFile2);
    }

    /**
     * 関西大学の世帯推計XML出力を世帯情報CSVと人情報CSVに変換する
     * @param inFile 関西大学の世帯推計XMLファイル
     * @param outFile1 世帯情報CSVファイル
     * @param outFile2 人情報CSVフィイル
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void convertFile(File inFile, File outFile1, File outFile2) throws IOException, SAXException, ParserConfigurationException {
        // 入力ファイルの処理
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(inFile);

        // 出力ファイルの処理
        PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile1, false), "Shift_JIS"));
        PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile2, false), "Shift_JIS"));
        pw1.write("household_id,city_code,town_code,city_name,town_name,chome,banchi,gou,region,lat,lng,building_type,building_lat,building_lng,household_type_id,household_type_name,num_human"); // 見出しを書き出す
        pw2.write("human_id,household_id,age,sex,role,sex_id,role_id");

        // ドキュメントルートを取得する
        Element root = document.getDocumentElement(); // root = cityとなるはず
        NodeList householdNodeList = root.getChildNodes(); // householdのノードを取得

        int household_id = 0;
        int human_id = 0;
        for (int i = 0; i < householdNodeList.getLength(); i++) {
            Node householdNode = householdNodeList.item(i);

            if (householdNode.getNodeType() == Node.ELEMENT_NODE) {
                household_id++; // 世帯IDの更新
                String household_str = Integer.toString(household_id);
                NodeList hoseholdItems = householdNode.getChildNodes(); // householdのアイテムを取得
                for (int j = 0; j < hoseholdItems.getLength(); j++) {
                    Node householdItemNode = hoseholdItems.item(j);
                    if (householdItemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element householdElement = (Element) householdItemNode;
                        if (householdItemNode.getNodeName().equals("address")) { // addressアイテムの処理
                            household_str = household_str + "," + householdElement.getAttribute("city_code"); // 市区町村コードの設定
                            household_str = household_str + "," + householdElement.getAttribute("town_code"); // タウンコードの設定
                            household_str = household_str + "," + root.getAttribute("city_name"); // 市区町村名の設定
                            household_str = household_str + "," + householdElement.getAttribute("town_name"); // タウン名の設定
                            household_str = household_str + "," + householdElement.getAttribute("chome"); // 丁目の設定
                            household_str = household_str + "," + householdElement.getAttribute("banchi"); // 番地の設定
                            household_str = household_str + "," + householdElement.getAttribute("gou"); // 号の設定
                            household_str = household_str + "," + householdElement.getAttribute("region"); // regionの設定
                        } else if (householdItemNode.getNodeName().equals("latlng")) {
                            household_str = household_str + "," + householdElement.getAttribute("lat"); // 緯度の設定
                            household_str = household_str + "," + householdElement.getAttribute("lng"); // 経度の設定
                        } else if (householdItemNode.getNodeName().equals("building")) {
                            household_str = household_str + "," + householdElement.getAttribute("type"); // 建物タイプの設定
                            household_str = household_str + "," + householdElement.getAttribute("lat"); // 建物緯度の設定
                            household_str = household_str + "," + householdElement.getAttribute("lng"); // 建物経度の設定
                        } else if (householdItemNode.getNodeName().equals("type")) {
                            household_str = household_str + "," + householdElement.getAttribute("id"); // 世帯タイプIDの設定
                            household_str = household_str + "," + householdElement.getAttribute("name"); // 世帯タイプの設定
                        } else if (householdItemNode.getNodeName().equals("personnel")) {
                            household_str = household_str + "," + householdElement.getAttribute("human"); // 世帯人数の設定
                        } else if (householdItemNode.getNodeName().equals("human")) {
                            human_id++;
                            String human_str = Integer.toString(human_id) + "," + Integer.toString(household_id);
                            human_str = human_str + "," + householdElement.getAttribute("age"); // 年齢の設定
                            human_str = human_str + "," + householdElement.getAttribute("sex"); // 性別の設定
                            human_str = human_str + "," + householdElement.getAttribute("role"); // 役割の設定
                            human_str = human_str + "," + householdElement.getAttribute("sexId"); // 年齢IDの設定
                            human_str = human_str + "," + householdElement.getAttribute("roleId"); // 役割IDの設定
                            pw2.write("\n" + human_str);
                        }
                    }
                }
                pw1.write("\n" + household_str);
            }
        }
        pw1.close();
        pw2.close();
    }
}
