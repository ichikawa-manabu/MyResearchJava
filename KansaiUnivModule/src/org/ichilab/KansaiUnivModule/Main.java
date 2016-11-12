package org.ichilab.KansaiUnivModule;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by manabu on 2016/11/09.
 */
public class Main {
    public static void main(String args[]) throws IllegalAccessException, InstantiationException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        FileAccess.readXmlFile(new File("/Users/manabu/Desktop/saitama_latlng_20161104/11208_tokorozawa_latlng.xml"));
    }
}
