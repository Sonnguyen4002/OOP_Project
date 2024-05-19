package com.example.OOP_Project.Media;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class NewsArticle {
    // Default values
    private String title = "Title";
    private String type = "Type";
    private String news = "News";
    private String summary = "Summary";
    private String category = "Category";
    private String tags = "Tags";
    private String publishDate = "Time";
    private String link = "https://www.facebook.com/";
    private String img_url = "https://www.w3schools.com/images/w3schools_logo_436_2.png";

    public NewsArticle() {
    }

    public NewsArticle(String jsonString) {
        this();
        Object obj = JSONValue.parse(jsonString);
        JSONObject data = (JSONObject) obj;
        try {
            title = (String) data.get("title");
            type = (String) data.get("type");
            news = (String) data.get("content");
            summary = (String) data.get("summary");
            category = (String) data.get("category");
            tags = (String) data.get("tags");
            publishDate = (String) data.get("publishDate");
            link = (String) data.get("link");
            img_url = (String) data.get("image");
        } catch (Exception e) {
        }
    }

    public String[] toArray() {
        return new String[] { title, type, category, summary, news, tags, publishDate, link, img_url };
    }
}
