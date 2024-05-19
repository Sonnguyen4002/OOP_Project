package articles;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class ArticleScraper {
    protected static int articleCount = 0;

    public abstract void scrapeArticles(List<String> urls, FileWriter csvWriter) throws IOException;

    public static int getArticleCount() {
        return articleCount;
    }

    public static void resetArticleCount() {
        articleCount = 0;
    }
}
