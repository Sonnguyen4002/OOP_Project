package articles;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {
    private static final String CSV_FILE_PATH = "news.csv";

    public static void main(String[] args) {
        List<ArticleScraper> scrapers = new ArrayList<>();
        scrapers.add(new AmbCrypto());
        scrapers.add(new BeInCrypto());
        scrapers.add(new Blockworks());
        scrapers.add(new Coinatory());
        scrapers.add(new CoinDesk());
        scrapers.add(new CryptoBriefing());
        scrapers.add(new TheConversation());
        scrapers.add(new TheDefiant());
            

        try (FileWriter csvWriter = new FileWriter(CSV_FILE_PATH, true)) {
            // Reset the article count at the start
            ArticleScraper.resetArticleCount();

            for (ArticleScraper scraper : scrapers) {
                String scraperName = scraper.getClass().getSimpleName();
                List<String> urls = Files.readAllLines(Paths.get(scraperName + ".txt"));
                scraper.scrapeArticles(urls, csvWriter);
                System.out.println("The ID of articles on " + scraperName + " ends at " + scraper.getArticleCount() + ".");
            }
        } catch (IOException e) {
            System.err.println("Error during scraping: " + e.getMessage());
        }
    }
}