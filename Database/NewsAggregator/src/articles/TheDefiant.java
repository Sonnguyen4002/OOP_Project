package articles;

import datacollection.WebScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class TheDefiant extends WebScraper {

    @Override
    public void scrapeArticles(List<String> urls, FileWriter csvWriter) throws IOException {
        for (String url : urls) {
            scrapeArticle(url, csvWriter);
        }
    }

    @Override
    protected void scrapeArticle(String url, FileWriter csvWriter) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements articles = doc.select("article");

        for (Element article : articles) {
            extractArticleDetails(doc);
            writeArticleToCSV(csvWriter);
        }
    }

    @Override
    protected void extractArticleDetails(Document doc) {
        link = doc.baseUri();
        source = doc.select("meta[name=publisher]").attr("content"); 
        type = doc.select("meta[property=og:type]").attr("content");
        title = doc.select("h1").text(); 
        summary = doc.select("meta[name=twitter:description]").attr("content"); 
        content = doc.select("p").text();

        String publishDateStr = doc.select("meta[property=article:published_time]").attr("content");
     // Convert the publish date string to LocalDateTime
        LocalDateTime publishDate = null;
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(publishDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            publishDate = offsetDateTime.toLocalDateTime();
        } catch (DateTimeParseException e) {
            System.err.println("Failed to parse date time: " + publishDateStr);
            e.printStackTrace();
        }
        
     // Extract tags from the specified <div> element
        Elements tagElements = doc.select("div.flex.flex-row.flex-wrap.gap-2.mb-10 a");
        tags = tagElements.stream()
                                 .map(Element::text)
                                 .collect(Collectors.joining(", "));

        if (tags.trim().isEmpty()) {
            tags = "N/A";
        }
        
        author = doc.select("meta[name=author]").attr("content");
        category = doc.select("span.hidden").text(); 
        image = doc.select("meta[property=og:image]").attr("content");

     // Print article attributes
        System.out.println("ID: " + id);
        System.out.println("Link: " + link);
        System.out.println("Source: " + source);
        System.out.println("Type: " + type);
        System.out.println("Title: " + title);
        System.out.println("Summary: " + summary);
        System.out.println("Content: " + content);
        System.out.println("Publish Date: " + publishDate);
        System.out.println("Tags: " + tags);
        System.out.println("Author: " + author);
        System.out.println("Category: " + category);
        System.out.println("Image: " + image);
        System.out.println();

    }

}
