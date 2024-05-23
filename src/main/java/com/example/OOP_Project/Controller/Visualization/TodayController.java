package com.example.OOP_Project.Controller.Visualization;

import com.example.OOP_Project.Controller.Display.Display;
import com.example.OOP_Project.Controller.Storage.DataController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// Client socket
import com.example.OOP_Project.ClientHandler.SocketClient;
// Articles
import com.example.OOP_Project.Media.NewsArticle;

public class TodayController extends Display {
    // Client scoket
    private static final SocketClient client = initializeClient();

    private static SocketClient initializeClient() {
        SocketClient client;
        try {
            client = new SocketClient("127.0.0.1", 8888);
        } catch (Exception e) {
            throw new Error("Failed to initialize socket client");
        }
        return client;
    }

    // Inputs
    private static String[][] news_inputs = {
            { "Title 1", "Type 1", "News 1", "Summary 1", "Category 1", "Tag 1", "Time 1", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 2", "Type 2", "News 2", "Summary 2", "Category 2", "Tag 2", "Time 2", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 3", "Type 3", "News 3", "Summary 3", "Category 3", "Tag 3", "Time 3", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 4", "Type 4", "News 4", "Summary 4", "Category 4", "Tag 4", "Time 4", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 5", "Type 5", "News 5", "Summary 5", "Category 5", "Tag 5", "Time 5", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 6", "Type 6", "News 6", "Summary 6", "Category 6", "Tag 6", "Time 6", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 7", "Type 7", "News 7", "Summary 7", "Category 7", "Tag 7", "Time 7", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 8", "Type 8", "News 8", "Summary 8", "Category 8", "Tag 8", "Time 8", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 9", "Type 9", "News 9", "Summary 9", "Category 9", "Tag 9", "Time 9", "https://www.facebook.com/",
                    "https://www.w3schools.com/images/w3schools_logo_436_2.png" },
            { "Title 10", "Type 10", "News 10", "Summary 10", "Category 10", "Tag 10", "Time 10",
                    "https://www.facebook.com/", "https://www.w3schools.com/images/w3schools_logo_436_2.png" }
    };

    public void addArticles() {
        addArticles(news_inputs, articleContainer);
    }

    @FXML
    private TextField input;

    public void handleInputTextSearch() {
        String searchText = input.getText();
        System.out.println("Từ khóa tìm kiếm: " + searchText);
        updateSearchResult(searchText);
    }

    public void handlePresetButtonSearch(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        // Lấy text của Button
        String buttonText = clickedButton.getText();
        System.out.println("Từ khóa tìm kiếm: " + buttonText);
        updateSearchResult(buttonText);

    }

    public void updateSearchResult(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                while (!client.getRefreshStatus())
                    System.out.print("Loading\r");
                refreshArticles();
                client.setRefreshStatus(false);
            }
        });
        client.sendMessageToServer(text);
    }

    @FXML
    private VBox articleContainer;

    public void initialize() {
        addArticles();
    }

    @FXML
    public void close() {
        System.out.println("Close");
    }

    public static void setNewsInputs(int size) {
        news_inputs = new String[9][size];
        System.out.println("Inputs reseted");
    }

    public static void addSearchResult(int pos, String jsonString) {
        NewsArticle article = new NewsArticle(jsonString);

        news_inputs[pos] = article.toArray();
        System.out.println("Article added at " + pos);
    }

    public static void addSearchResult_storage(int pos, String jsonString) {
        NewsArticle article = new NewsArticle(jsonString);

        DataController.setInput(article.toArray(), pos);
        System.out.println(article.toArray()[0]);
        System.out.println("Article added at 111111111 " + pos);

    }

    public void refreshArticles() {
        articleContainer.getChildren().clear();
        addArticles();
    }

}