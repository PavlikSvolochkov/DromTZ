package ru.dromtz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetLinks {

    public List<String> getLinks(String url) throws IOException {

        List<String> links = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("a[href]");

        for (Element link : elements) {
            links.add(link.attr("href"));
        }

        return links;
    }

    public boolean isURLExist(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        } else
            return false;
    }
}
