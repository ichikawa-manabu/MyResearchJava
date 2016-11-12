package org.ichilab.SoarsModule.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import env.EquippedObject;

public class Collection2File {

	private String separator = "\t";
	
	private File file = null;
	
	private FileOutputStream fos = null;
	private OutputStreamWriter osw = null;
	private PrintWriter pw = null;
	
	public Collection2File() {
		
	}
	
	public Collection2File(String path) throws IOException {
		super();
		
		file = new File(path);
		if(!file.exists())
			file.createNewFile();
		
		fos = new FileOutputStream(file,true);
	    osw = new OutputStreamWriter(fos);
	    pw = new PrintWriter(osw);
	}
	
	public void addCollection(Collection<EquippedObject> col) {
		String line = "";
		System.out.println(col.size());
		Iterator<EquippedObject> it = col.iterator();
		if(!col.isEmpty())
			line = it.next().toString();
		
		while(it.hasNext())
			line = line + separator + it.next().toString();
		
		pw.println(line);
	}
	
	public void closeCollection2File() {
		pw.close();
	}
}
