package articles;

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

// AmbCrypto Scraper
class BeInCrypto extends ArticleScraper {
    @Override
    public void scrapeArticles(List<String> urls, FileWriter csvWriter) throws IOException {
        // Write CSV header only if articleCount is 0 (indicating the file is empty)
        if (ArticleScraper.getArticleCount() == 0) {
            csvWriter.append("id::link::source::type::title::summary::content::publishDate::tags::author::category::image\n");
        }

        for (String url : urls) {
            scrapeArticle(url, csvWriter);
        }
    }

    private void scrapeArticle(String url, FileWriter csvWriter) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements articles = doc.select("article");

        for (Element article : articles) {
            ArticleScraper.articleCount++;

            String link = url;
            String source = doc.select("meta[property=og:site_name]").attr("content");
            String type = doc.select("meta[property=og:type]").attr("content");
            String title = article.select("h1").text();
            Element summaryElement = article.select("strong").first();
            String summary = summaryElement != null ? summaryElement.text() : "N/A";

            String content = article.select("p").text();
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
            // Format the LocalDateTime to a String
            String formattedPublishDate = publishDate != null ? publishDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "N/A";

            String tags = extractCategoryFromLink(doc);
            String author = doc.select("meta[name=twitter:data1]").attr("content");

         // Extract the category from the <a> tag
            String category = extractCategoryFromLink(doc);
            
            String image = doc.select("meta[property=og:image]").attr("content");

            csvWriter.append(String.join("::", new String[]{
                String.valueOf(ArticleScraper.articleCount), link, source, type, title, summary, content, formattedPublishDate,
                tags, author, category, image
            })).append("\n");
        }
    }
    private String extractCategoryFromLink(Document doc) {
        String category = "N/A";
        // Select the <a> tag with the class that matches the category information
        Element categoryElement = doc.select("a.whitespace-nowrap.inline-flex.items-center.p4.font-sans.font-normal.text-currentColor.hover\\:underline").first();
        if (categoryElement != null) {
            category = categoryElement.text();
        }
        return category;
    }
}
