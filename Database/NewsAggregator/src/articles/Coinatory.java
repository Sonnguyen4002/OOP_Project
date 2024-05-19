package articles;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Coinatory Scraper
class Coinatory extends ArticleScraper {
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
            String title = doc.select("meta[property=og:title]").attr("content"); 
            String summary = doc.select("meta[property=og:description]").attr("content"); 
            String content = article.select("p").text();
            
            String publishDateStr = doc.select("meta[property=article:published_time]").attr("content");
            // Convert the publish date string to ZonedDateTime
            ZonedDateTime publishDate = ZonedDateTime.parse(publishDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            // Format the ZonedDateTime to a String
            String formattedPublishDate = publishDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String tags = doc.select("ul.tdb-tags li a").text().replaceAll(" ", ", ");       
            String author = doc.select("meta[name=author]").attr("content");
            String category = doc.select("a.tdb-entry-crumb").text(); 
            String image = doc.select("meta[property=og:image]").attr("content");

            csvWriter.append(String.join("::", new String[]{
                String.valueOf(ArticleScraper.articleCount), link, source, type, title, summary, content, formattedPublishDate,
                tags, author, category, image
            })).append("\n");
        }
    }
}
