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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TheConversation Scraper
class TheConversation extends ArticleScraper {
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
            String content = article.select("div[itemprop=articleBody] > p").text();

            // Convert the publish date string to ZonedDateTime
            String publishDateStr = doc.select("time").attr("datetime");
            ZonedDateTime publishDate = ZonedDateTime.parse(publishDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String formattedPublishDate = publishDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            String tags = doc.select("meta[name=news_keywords]").attr("content");
            String author = doc.select("meta[name=author]").attr("content");
            String category = extractCategoryFromScript(doc);
            String image = doc.select("meta[property=og:image]").attr("content");

            csvWriter.append(String.join("::", new String[]{
                String.valueOf(ArticleScraper.articleCount), link, source, type, title, summary, content, formattedPublishDate,
                tags, author, category, image
            })).append("\n");
        }
    }
    // Extract attribute "category"
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
