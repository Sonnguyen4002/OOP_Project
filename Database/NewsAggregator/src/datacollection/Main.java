package datacollection;

import articles.AmbCrypto;
import articles.BeInCrypto;
import articles.Blockworks;
import articles.Coinatory;
import articles.CoinDesk;
import articles.CryptoBriefing;
import articles.TheConversation;
import articles.TheDefiant;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String CSV_FILE_PATH = "news_test.csv";

    public static void main(String[] args) {
        List<WebScraper> scrapers = new ArrayList<>();
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
            for (WebScraper scraper : scrapers) {
                List<String> urls = Files.readAllLines(Paths.get(scraper.getClass().getSimpleName() + ".txt"));
                scraper.scrapeArticles(urls, csvWriter);
                System.out.println("The ID of articles on " + scraper.getClass().getSimpleName() + " ends at " + scraper.id + ".");
            }
        } catch (IOException e) {
            System.err.println("Error during scraping: " + e.getMessage());
        }
    }
}
