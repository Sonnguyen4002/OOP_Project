package articles;

import datacollection.WebScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TheConversation extends WebScraper {

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
        source = doc.select("meta[property=og:site_name]").attr("content");
        type = doc.select("meta[property=og:type]").attr("content");
        title = doc.select("meta[property=og:title]").attr("content");
        summary = doc.select("meta[property=og:description]").attr("content");
        content = doc.select("div[itemprop=articleBody] > p").text();
        
        String publishDateStr = doc.select("time").attr("datetime");
        // Convert the publish date string to ZonedDateTime
        ZonedDateTime publishDateTime = ZonedDateTime.parse(publishDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // Format the ZonedDateTime to a String
        publishDate = publishDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        tags = doc.select("meta[name=news_keywords]").attr("content");
        author = doc.select("meta[name=author]").attr("content");
        category = extractCategoryFromScript(doc);
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
    
    private String extractCategoryFromScript(Document doc) {
        String category = "N/A";
        Elements scriptElements = doc.getElementsByTag("script");
        Pattern pattern = Pattern.compile("content_category\":\"([^\"]+)\"");
        for (Element scriptElement : scriptElements) {
            Matcher matcher = pattern.matcher(scriptElement.html());
            if (matcher.find()) {
                category = matcher.group(1);
                break;
            }
        }
        // Process category to extract relevant parts
        if (!category.equals("N/A")) {
            String[] parts = category.split("\\|")[0].split("\\+");
            category = String.join(", ", parts).trim();
        }
        return category;
    }
}
