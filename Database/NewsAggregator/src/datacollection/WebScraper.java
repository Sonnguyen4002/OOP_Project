package datacollection;

import org.jsoup.nodes.Document;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class WebScraper {
    protected int id;
    protected String link;
    protected String source;
    protected String type;
    protected String title;
    protected String summary;
    protected String content;
    protected String publishDate;
    protected String tags;
    protected String author;
    protected String category;
    protected String image;

    public abstract void scrapeArticles(List<String> urls, FileWriter csvWriter) throws IOException;
    protected abstract void scrapeArticle(String url, FileWriter csvWriter) throws IOException;

    protected void writeCSVHeader(FileWriter csvWriter) throws IOException {
        csvWriter.append("id::link::source::type::title::summary::content::publishDate::tags::author::category::image\n");
    }

    protected void writeArticleToCSV(FileWriter csvWriter) throws IOException {
        csvWriter.append(String.join("::", new String[]{
            String.valueOf(id++), link, source, type, title, summary, content, publishDate,
            tags, author, category, image
        })).append("\n");
    }

    protected abstract void extractArticleDetails(Document doc);
}
