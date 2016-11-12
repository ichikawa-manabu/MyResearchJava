package org.ichilab.research.aed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by manabu on 2016/11/10.
 */
public class DownloadAedMap {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("http://www.google.co.jp").get();
        System.out.println(document.html());
    }
}
