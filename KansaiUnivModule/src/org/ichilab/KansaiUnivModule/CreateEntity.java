package org.ichilab.KansaiUnivModule;

import env.Agent;
import env.Environment;
import env.Spot;
import role.Role;
import role.RoleType;
import time.Time;
import util.DoubleValue;
import util.IntValue;
import util.Invoker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by manabu on 2016/11/09.
 */
public class CreateEntity {
    public static Spot createHousehold(Spot spot) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Spot cspot = new Spot(); // スポットの生成
        cspot.assignName(spot.getName()); // スポット名の設定
        Role role = RoleType.forName("kumSpotRole").createRole(cspot);
        Environment.getCurrent().addSpotRule(role); // ロールの定義

        // ここから変数の設定（コピー）
        Map<String, Object> variables = spot.getEquipMap();
        for(Map.Entry<String, Object> e : variables.entrySet()) {
            if(!(e.getKey().startsWith("__") || e.getKey().startsWith("$"))) {  // 特殊変数はコピーしない
                if(e.getValue() instanceof String) { // キーワード変数の処理
                    cspot.setKeyword(e.getKey(), (String)e.getValue());
                }
                else if(e.getValue() instanceof IntValue) { // 数値変数（整数）の処理
                    cspot.setIntVariable(e.getKey(), ((IntValue)e.getValue()).intValue());
                }
                else if(e.getValue() instanceof DoubleValue) { // 数値変数（実数）の処理
                    cspot.setDoubleVariable(e.getKey(), ((DoubleValue)e.getValue()).doubleValue());
                }
                else if(e.getValue() instanceof Spot) { // スポット変数の処理
                    cspot.setSpotVariable(e.getKey(), (Spot)e.getValue());
                }
                else if(e.getValue() instanceof Time) { // 時間変数の処理
                    cspot.setTimeVariable(e.getKey(), (String)e.getValue());
                    cspot.setEquip(e.getKey(), new Object() {
                        public String toString() {
                            return cspot.getEquip("$Time." + e.getKey()).toString();
                        }
                    });
                }
                else if(e.getValue() instanceof HashSet) { // 集合変数の処理
                    HashSet<Object> set = new HashSet<>((HashSet)e.getValue());
                    cspot.setEquip(e.getKey(), set);
                }
                else if(e.getValue() instanceof LinkedList) { // 集合変数の処理
                    LinkedList<Object> list = new LinkedList<>((LinkedList)e.getValue());
                    cspot.setEquip(e.getKey(), list);
                }
                else if(e.getValue() instanceof HashMap) { // 集合変数の処理
                    HashMap<String, Object> map = new HashMap<>((HashMap)e.getValue());
                    cspot.setEquip(e.getKey(), map);
                }
                else if(e.getValue() instanceof Invoker) { // クラス変数の処理
                    String pair[] = e.getValue().toString().split(" ");
                    cspot.setEquip(e.getKey(), new Invoker(Environment.getCurrent().classForName(pair[1])));
                }
            }
        }
        return cspot;
    }

    public static Agent createPerson(Agent agent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Agent cagent = new Agent(); // エージェントの生成
        cagent.assignName(agent.getName()); // エージェント名の設定
        Role role = RoleType.forName("kumAgentRole").createRole(cagent);
        cagent.setActiveRole(role); // ロールの定義

        // ここから変数の設定（コピー）
        Map<String, Object> variables = agent.getEquipMap();
        for(Map.Entry<String, Object> e : variables.entrySet()) {
            if(!(e.getKey().startsWith("__") || e.getKey().startsWith("$"))) {  // 特殊変数はコピーしない
                if(e.getValue() instanceof String) { // キーワード変数の処理
                    cagent.setKeyword(e.getKey(), (String)e.getValue());
                }
                else if(e.getValue() instanceof IntValue) { // 数値変数（整数）の処理
                    cagent.setIntVariable(e.getKey(), ((IntValue)e.getValue()).intValue());
                }
                else if(e.getValue() instanceof DoubleValue) { // 数値変数（実数）の処理
                    cagent.setDoubleVariable(e.getKey(), ((DoubleValue)e.getValue()).doubleValue());
                }
                else if(e.getValue() instanceof Spot) { // スポット変数の処理
                    cagent.setSpotVariable(e.getKey(), (Spot)e.getValue());
                }
                else if(e.getValue() instanceof Time) { // 時間変数の処理
                    cagent.setTimeVariable(e.getKey(), (String)e.getValue());
                    cagent.setEquip(e.getKey(), new Object() {
                        public String toString() {
                            return cagent.getEquip("$Time." + e.getKey()).toString();
                        }
                    });
                }
                else if(e.getValue() instanceof RoleType) { // 役割変数の処理
                    cagent.setRoleVariable(e.getKey(), (RoleType)e.getValue());
                    cagent.setEquip(e.getKey(), new Object() {
                        public String toString() {
                            return cagent.getEquip("$Role." + e.getKey()).toString();
                        }
                    });
                }
                else if(e.getValue() instanceof HashSet) { // 集合変数の処理
                    HashSet<Object> set = new HashSet<>((HashSet)e.getValue());
                    cagent.setEquip(e.getKey(), set);
                }
                else if(e.getValue() instanceof LinkedList) { // 集合変数の処理
                    LinkedList<Object> list = new LinkedList<>((LinkedList)e.getValue());
                    cagent.setEquip(e.getKey(), list);
                }
                else if(e.getValue() instanceof HashMap) { // 集合変数の処理
                    HashMap<String, Object> map = new HashMap<>((HashMap)e.getValue());
                    cagent.setEquip(e.getKey(), map);
                }
                else if(e.getValue() instanceof Invoker) { // クラス変数の処理
                    String pair[] = e.getValue().toString().split(" ");
                    cagent.setEquip(e.getKey(), new Invoker(Environment.getCurrent().classForName(pair[1])));
                }
            }
        }
        return cagent;
    }
}
