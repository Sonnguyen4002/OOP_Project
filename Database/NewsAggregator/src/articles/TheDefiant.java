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
import java.util.stream.Collectors;

// TheDefiant Scraper
class TheDefiant extends ArticleScraper {
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
            String source = doc.select("meta[name=publisher]").attr("content");
            String type = doc.select("meta[property=og:type]").attr("content");
            String title = article.select("h1").text();
            String summary = doc.select("meta[name=twitter:description]").attr("content");
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

            // Extract tags from the specified <div> element
            Elements tagElements = doc.select("div.flex.flex-row.flex-wrap.gap-2.mb-10 a");
            String tags = tagElements.stream().map(Element::text).collect(Collectors.joining(", "));
            String author = doc.select("meta[name=author]").attr("content");

            // Extract category from the <span> tag with class="hidden"
            String category = doc.select("span.hidden").text();
            String image = doc.select("meta[property=og:image]").attr("content");

            csvWriter.append(String.join(",", new String[]{
                String.valueOf(ArticleScraper.articleCount), link, source, type, title, summary, content, formattedPublishDate,
                tags, author, category, image
            })).append("\n");
        }
    }
}
