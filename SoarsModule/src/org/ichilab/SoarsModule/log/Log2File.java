package org.ichilab.SoarsModule.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import env.Agent;
import env.Environment;
import env.Spot;


public class Log2File {
	private LinkedList<String> agent_log_header = null;
	private LinkedList<String> spot_log_header = null;
	private String separater = "\t";
	
	private String agent_path = "";
	private File agent_file = null;
	private FileOutputStream agent_fos = null;
	private OutputStreamWriter agent_osw = null;
	private PrintWriter agent_pw = null;
	
	private String spot_path = "";
	private File spot_file = null;
	private FileOutputStream spot_fos = null;
	private OutputStreamWriter spot_osw = null;
	private PrintWriter spot_pw = null;
	
	public Log2File() {
		
	}
	
	public Log2File(String spot_log, String agent_log) throws IOException {
		super();

		spot_log_header = new LinkedList<String>();
		agent_log_header = new LinkedList<String>();

		spot_path = spot_log;
		spot_file = new File(spot_path);
		if(!spot_file.exists())
			spot_file.createNewFile();

		spot_fos = new FileOutputStream(spot_file,true);
		spot_osw = new OutputStreamWriter(spot_fos);
		spot_pw = new PrintWriter(spot_osw);
		
		agent_path = agent_log;
		agent_file = new File(agent_path);
		if(!agent_file.exists())
			agent_file.createNewFile();
		
		agent_fos = new FileOutputStream(agent_file,true);
	    agent_osw = new OutputStreamWriter(agent_fos);
	    agent_pw = new PrintWriter(agent_osw);
		
		createHeader();
		writeHeader();
	}
	
	public void createHeader() {
		List<Spot> spot_list = Spot.getList();
		List<Agent> agent_list = Agent.getList();

		spot_log_header.addLast("ID");
		spot_log_header.addLast("Name");
		spot_log_header.addLast("Time");
		spot_log_header.addLast("Stage");
		spot_log_header.addLast("Role");
		for(int i=0; i<spot_list.size(); i++) {
			Spot spot = (Spot) spot_list.get(i);
			Map<String, Object> map = spot.getEquipMap();
			for(Map.Entry<String, Object> e : map.entrySet()) {
				if(!(e.getKey().startsWith("__") || e.getKey().startsWith("$"))) {
					if(!spot_log_header.contains(e.getKey()))
						spot_log_header.addLast(e.getKey());
				}
			}
		}

		agent_log_header.addLast("ID");
		agent_log_header.addLast("Name");
		agent_log_header.addLast("Time");
		agent_log_header.addLast("Stage");
		agent_log_header.addLast("Spot");
		agent_log_header.addLast("Role");
		for(int i=0; i<agent_list.size(); i++) {
			Agent agent = (Agent) agent_list.get(i);
			Map<String, Object> map = agent.getEquipMap();
			for(Map.Entry<String, Object> e : map.entrySet()) {
				if(!(e.getKey().startsWith("__") || e.getKey().startsWith("$"))) {
					if(!agent_log_header.contains(e.getKey()))
						agent_log_header.addLast(e.getKey());
				}
			}
		}
	}
	
	public void writeHeader() {
		String agent_header = "";
		String spot_header = "";
		for(int i=0; i<agent_log_header.size(); i++) {
			if(i==0)
				agent_header = agent_log_header.get(i);
			else
				agent_header = agent_header + separater + agent_log_header.get(i);
		}
		
		for(int i=0; i<spot_log_header.size(); i++) {
			if(i==0)
				spot_header = spot_log_header.get(i);
			else
				spot_header = spot_header + separater + spot_log_header.get(i);
		}
		
		agent_pw.println(agent_header);
		spot_pw.println(spot_header);
	}

	public void writeSpotLog(Spot spot) {
		String str = Integer.toString(spot.getId()) // ID
				+ separater + spot.getName() // Name
				+ separater + Environment.getCurrent().getStepCounter().toString() // Time
				+ separater + Environment.getCurrent().getStepCounter().getStageNames().get(Environment.getCurrent().getStepCounter().getStage()); // Stage
		if(spot.getEquip("$Role")!=null) {
			str = str + separater + spot.getEquip("$Role");
		}
		else  {
			str = str + separater + "";
		}
		for(int j=5; j<spot_log_header.size(); j++) {
			if(spot.getEquip(spot_log_header.get(j))!=null)
				str = str + separater + spot.getEquip(spot_log_header.get(j)).toString();
			else
				str = str + separater + "";
		}
		spot_pw.println(str);
	}

	public void writeAllSpotLog() {
		List<Spot> spot_list = Spot.getList();

		for(int i=0; i<spot_list.size(); i++) {
			Spot spot = (Spot) spot_list.get(i);
			String str = Integer.toString(spot.getId())
					+ separater + spot.getName()
					+ separater + Environment.getCurrent().getStepCounter().toString()
					+ separater + Environment.getCurrent().getStepCounter().getStageNames().get(Environment.getCurrent().getStepCounter().getStage());
			if(spot.getEquip("$Role")!=null) {
				str = str + separater + spot.getEquip("$Role");
			}
			else  {
				str = str + separater + "";
			}
			for(int j=5; j<spot_log_header.size(); j++) {
				if(spot.getEquip(spot_log_header.get(j))!=null)
					str = str + separater + spot.getEquip(spot_log_header.get(j)).toString();
				else
					str = str + separater + "";
			}
			spot_pw.println(str);
		}
	}

	public void writeAgentLog(Agent agent) {
		String str = Integer.toString(agent.getId()) 
				+ separater + agent.getName() 
				+ separater + Environment.getCurrent().getStepCounter().toString()
				+ separater + Environment.getCurrent().getStepCounter().getStageNames().get(Environment.getCurrent().getStepCounter().getStage());
		if(agent.getSpot()!=null) {
			str = str + separater + agent.getSpot().getName();
		}
		else {
			str = str + separater + "";
		}
		if(agent.getActiveRole()!=null) {
			str = str + separater + agent.getActiveRole().toString();
		}
		else {
			str = str + separater + "";
		}
		for(int j=6; j<agent_log_header.size(); j++) {
			if(agent.getEquip(agent_log_header.get(j))!=null)
				str = str + separater + agent.getEquip(agent_log_header.get(j)).toString();
			else
				str = str + separater + "";
		}
		agent_pw.println(str);
	}
	
	public void writeAllAgentLog() {
		List<Agent> agent_list = Agent.getList();
		
		for(int i=0; i<agent_list.size(); i++) {
			Agent agent = (Agent) agent_list.get(i);
			String str = Integer.toString(agent.getId()) 
					+ separater + agent.getName() 
					+ separater + Environment.getCurrent().getStepCounter().toString()
					+ separater + Environment.getCurrent().getStepCounter().getStageNames().get(Environment.getCurrent().getStepCounter().getStage());
			if(agent.getSpot()!=null) {
				str = str + separater + agent.getSpot().getName();
			}
			else {
				str = str + separater + "";
			}
			if(agent.getActiveRole()!=null) {
				str = str + separater + agent.getActiveRole().toString();
			}
			else {
				str = str + separater + "";
			}

			for(int j=6; j<agent_log_header.size(); j++) {
				if(agent.getEquip(agent_log_header.get(j))!=null)
					str = str + separater + agent.getEquip(agent_log_header.get(j)).toString();
				else
					str = str + separater + "";
			}
			agent_pw.println(str);
		}
	}
	
	public void writeAllEntityLog() {
		writeAllAgentLog();
		writeAllSpotLog();
	}
	
	public void closeLog2File() {
		agent_pw.close();
		spot_pw.close();
	}
}
