package com.yesul.util;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlImageExtractor {
    public static List<String> extractAllImageUrls(String html) {
        Document doc = Jsoup.parse(html);
        Elements images = doc.select("img[src]");
        return images.stream()
                .map(img -> img.attr("src"))
                .collect(Collectors.toList());
    }
}
