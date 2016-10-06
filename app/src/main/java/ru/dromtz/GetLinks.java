package ru.dromtz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetLinks {

    public List<String> getLinks(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        Elements newsHeadlines = doc.select("a");

        return new ArrayList<String>(Arrays.<String>asList((String[]) newsHeadlines.toArray()));
    }
}
