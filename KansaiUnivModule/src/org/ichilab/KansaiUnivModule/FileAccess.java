package org.ichilab.KansaiUnivModule;

import env.Agent;
import env.Spot;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.Resolver;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by manabu on 2016/11/09.
 */
public class FileAccess {

    public static void readXmlFile(String str) throws IllegalAccessException, InstantiationException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        File file = new File(str);
        readXmlFile(file);
    }

    public static void readXmlFile(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        // resolverで作成系は行う
        new Resolver() {
            public void resolve() throws IllegalAccessException, InstantiationException, ClassNotFoundException {


                // ドキュメントルートを取得する
                Element root = document.getDocumentElement(); // root = cityとなるはず
                NodeList householdNodeList = root.getChildNodes(); // householdのノードを取得

                for (int i = 0; i < householdNodeList.getLength(); i++) {
                    Node householdNode = householdNodeList.item(i);

                    if (householdNode.getNodeType() == Node.ELEMENT_NODE) {
                        Spot spot = CreateEntity.createHousehold(Spot.forName("kumHouseholdSpot")); // 世帯スポットの作成
                        NodeList hoseholdItems = householdNode.getChildNodes(); // householdのアイテムを取得
                        for (int j = 0; j < hoseholdItems.getLength(); j++) {
                            Node householdItemNode = hoseholdItems.item(j);
                            if (householdItemNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element householdElement = (Element) householdItemNode;
                                if (householdItemNode.getNodeName().equals("address")) { // addressアイテムの処理
                                    spot.setKeyword("kum_city_code", householdElement.getAttribute("city_code")); // 市区町村コードの設定
                                    spot.setKeyword("kum_town_code", householdElement.getAttribute("town_code")); // タウンコードの設定
                                    spot.setKeyword("kum_city_name", root.getAttribute("city_name")); // 市区町村名の設定
                                    spot.setKeyword("kum_town_name", householdElement.getAttribute("town_name")); // タウン名の設定
                                    spot.setKeyword("kum_chome", householdElement.getAttribute("chome")); // 丁目の設定
                                    spot.setKeyword("kum_banchi", householdElement.getAttribute("banchi")); // 番地の設定
                                    spot.setKeyword("kum_gou", householdElement.getAttribute("gou")); // 号の設定
                                    spot.setIntVariable("kum_region", Integer.parseInt(householdElement.getAttribute("region"))); // regionの設定
                                } else if (householdItemNode.getNodeName().equals("latlng")) {
                                    spot.setDoubleVariable("kum_lat", Double.parseDouble(householdElement.getAttribute("lat"))); // 緯度の設定
                                    spot.setDoubleVariable("kum_lon", Double.parseDouble(householdElement.getAttribute("lng"))); // 経度の設定
                                } else if (householdItemNode.getNodeName().equals("building")) {
                                    spot.setKeyword("kum_building_type", householdElement.getAttribute("type")); // 建物タイプの設定
                                    spot.setDoubleVariable("kum_building_lat", Double.parseDouble(householdElement.getAttribute("lat"))); // 建物緯度の設定
                                    spot.setDoubleVariable("kum_building_lon", Double.parseDouble(householdElement.getAttribute("lng"))); // 建物経度の設定
                                } else if (householdItemNode.getNodeName().equals("type")) {
                                    spot.setIntVariable("kum_household_type_id", Integer.parseInt(householdElement.getAttribute("id"))); // 世帯タイプIDの設定
                                    spot.setKeyword("kum_household_type", householdElement.getAttribute("name")); // 世帯タイプの設定
                                } else if (householdItemNode.getNodeName().equals("personnel")) {
                                    spot.setIntVariable("kum_personnel", Integer.parseInt(householdElement.getAttribute("human"))); // 世帯人数の設定
                                } else if (householdItemNode.getNodeName().equals("human")) {
                                    Agent agent = CreateEntity.createPerson(Agent.forName("kumPersonAgent")); // 人間エージェントの作成
                                    agent.setIntVariable("kum_age", Integer.parseInt(householdElement.getAttribute("age"))); // 年齢の設定
                                    agent.setKeyword("kum_sex", householdElement.getAttribute("sex")); // 性別の設定
                                    agent.setKeyword("kum_role", householdElement.getAttribute("role")); // 役割の設定
                                    agent.setIntVariable("kum_sex_id", Integer.parseInt(householdElement.getAttribute("sexId"))); // 年齢IDの設定
                                    agent.setIntVariable("kum_role_id", Integer.parseInt(householdElement.getAttribute("roleId"))); // 役割IDの設定
                                    agent.setSpot(spot);

                                    // 家族の登録
                                    HashSet<Agent> family = spot.getEquip("kum_family");
                                    family.add(agent);
                                    spot.setEquip("kum_family", family);
                                }
                            }
                        }
                    }
                }
            }
        }.requestResolve();
    }
}
