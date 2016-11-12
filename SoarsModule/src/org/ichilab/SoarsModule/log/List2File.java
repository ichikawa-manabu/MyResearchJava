package org.ichilab.SoarsModule.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

import env.EquippedObject;

public class List2File {

	private String separator = "\t";
	
	private File file = null;
	
	private FileOutputStream fos = null;
	private OutputStreamWriter osw = null;
	private PrintWriter pw = null;
	
	public List2File() {
		
	}
	
	public List2File(String path) throws IOException {
		super();
		
		file = new File(path);
		if(!file.exists())
			file.createNewFile();
		
		fos = new FileOutputStream(file,true);
	    osw = new OutputStreamWriter(fos);
	    pw = new PrintWriter(osw);
	}
	
	public void addList(LinkedList<EquippedObject> list) {
		String line = "";
		for(int i=0; i<list.size(); i++) {
			if(i==0)
				line = list.get(i).toString();
			else
				line = line + separator + list.get(i).toString();
		}
		pw.println(line);
	}
	
	public void closeList2File() {
		pw.close();
	}
}
