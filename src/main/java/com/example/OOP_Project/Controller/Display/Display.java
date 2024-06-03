package com.example.OOP_Project.Controller.Display;

import java.net.URL;
import java.util.ArrayList;

import com.example.OOP_Project.Controller.Storage.DataController;
import com.example.OOP_Project.Controller.Visualization.TodayController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class Display {
    @FXML
    public DetailController detailController;

    public void addArticles(ArrayList<String[]> news_inputs, VBox articleContainer) {

        for (int i = 0; i < news_inputs.size(); i++) {
            AnchorPane art = createArticle(news_inputs.get(i)[0], news_inputs.get(i)[1], news_inputs.get(i)[2],
                    news_inputs.get(i)[3], news_inputs.get(i)[4]);

            Button moreButton = new Button();
            moreButton.setLayoutX(600);
            moreButton.setLayoutY(15);
            moreButton.setPrefSize(50, 40);
            moreButton.setStyle("-fx-background-color: #020023;");

            URL imageUrl = TodayController.class.getResource("/image/more.png");

            Image aterImage = new Image(imageUrl.toExternalForm());
            ImageView moreImageView = new ImageView(aterImage);

            moreImageView.setFitWidth(41);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(1.0);
            colorAdjust.setContrast(-0.05);
            colorAdjust.setSaturation(0.04);
            moreImageView.setEffect(colorAdjust);

            moreImageView.setFitHeight(29);
            moreButton.setGraphic(moreImageView);

            int finalI = i; // Variable 'i' needs to be effectively final or be effectively used as a final
                            // variable
            moreButton.setOnAction(event -> {

                setPane(news_inputs.get(finalI)[0], news_inputs.get(finalI)[1], news_inputs.get(finalI)[2],
                        news_inputs.get(finalI)[3], news_inputs.get(finalI)[4], news_inputs.get(finalI)[5],
                        news_inputs.get(finalI)[6], news_inputs.get(finalI)[7], news_inputs.get(finalI)[8]);
            });
            Pane checkReadLaterPane = new Pane();
            checkReadLaterPane.setLayoutX(0);
            checkReadLaterPane.setLayoutY(130);
            checkReadLaterPane.setPrefSize(200, 50);
            URL tick = Display.class.getResource("/image/tick.png");

            Image laterImage = new Image(tick.toExternalForm());
            ImageView tickImageView = new ImageView(laterImage);
            tickImageView.setLayoutX(5);
            tickImageView.setLayoutY(10);
            tickImageView.setFitWidth(35);
            tickImageView.setFitHeight(35);

            Label viewInReadLaterLabel = new Label("View in Read Later");
            viewInReadLaterLabel.setLayoutX(44);
            viewInReadLaterLabel.setLayoutY(19);
            viewInReadLaterLabel.setTextFill(Color.rgb(242, 117, 117));
            viewInReadLaterLabel.setFont(Font.font("System Italic", 11));

            Button laterButton = new Button();
            laterButton.setLayoutX(539);
            laterButton.setLayoutY(13);
            laterButton.setPrefSize(40, 50);
            laterButton.setStyle("-fx-background-color: #020023;");

            checkReadLaterPane.getChildren().addAll(tickImageView, viewInReadLaterLabel);
            checkReadLaterPane.setVisible(true);

            art.getChildren().addAll(moreButton, checkReadLaterPane);
            articleContainer.getChildren().add(art);

        }
    }

    public void addArticles(String[][] news_inputs, VBox articleContainer) {

        for (int i = 0; i < news_inputs.length; i++) {

            int num = i;

            AnchorPane art = createArticle(news_inputs[i][0], news_inputs[i][1], news_inputs[i][2],
                    news_inputs[i][3],
                    news_inputs[i][4]);

            Button moreButton = new Button();
            moreButton.setLayoutX(600);
            moreButton.setLayoutY(15);
            moreButton.setPrefSize(50, 40);
            moreButton.setStyle("-fx-background-color: #020023;");

            URL imageUrl = TodayController.class.getResource("/image/more.png");

            Image aterImage = new Image(imageUrl.toExternalForm());
            ImageView moreImageView = new ImageView(aterImage);

            moreImageView.setFitWidth(41);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(1.0);
            colorAdjust.setContrast(-0.05);
            colorAdjust.setSaturation(0.04);
            moreImageView.setEffect(colorAdjust);

            moreImageView.setFitHeight(29);
            moreButton.setGraphic(moreImageView);

            int finalI = i; // Variable 'i' needs to be effectively final or be effectively used as a final
                            // variable
            moreButton.setOnAction(event -> {

                setPane(news_inputs[finalI][0], news_inputs[finalI][1], news_inputs[finalI][2],
                        news_inputs[finalI][3], news_inputs[finalI][4], news_inputs[finalI][5],
                        news_inputs[finalI][6], news_inputs[finalI][7], news_inputs[finalI][8]);
            });
            Pane checkReadLaterPane = new Pane();
            checkReadLaterPane.setLayoutX(0);
            checkReadLaterPane.setLayoutY(130);
            checkReadLaterPane.setPrefSize(200, 50);
            URL tick = Display.class.getResource("/image/tick.png");

            Image laterImage = new Image(tick.toExternalForm());
            ImageView tickImageView = new ImageView(laterImage);
            tickImageView.setLayoutX(5);
            tickImageView.setLayoutY(10);
            tickImageView.setFitWidth(35);
            tickImageView.setFitHeight(35);

            Label viewInReadLaterLabel = new Label("View in Read Later");
            viewInReadLaterLabel.setLayoutX(44);
            viewInReadLaterLabel.setLayoutY(19);
            viewInReadLaterLabel.setTextFill(Color.rgb(242, 117, 117));
            viewInReadLaterLabel.setFont(Font.font("System Italic", 11));

            Button laterButton = new Button();
            laterButton.setLayoutX(539);
            laterButton.setLayoutY(13);
            laterButton.setPrefSize(40, 50);
            laterButton.setStyle("-fx-background-color: #020023;");
            URL imageUr = Display.class.getResource("/image/later.png");

            Image aterImag = new Image(imageUr.toExternalForm());
            ImageView laterImageView = new ImageView(aterImag);
            laterImageView.setFitWidth(40);
            laterImageView.setFitHeight(50);
            laterButton.setGraphic(laterImageView);
            ColorAdjust colorAdjus = new ColorAdjust();
            colorAdjus.setBrightness(1.0);
            colorAdjus.setContrast(0.06);
            laterImageView.setEffect(colorAdjus);
            laterButton.setOnAction(event -> {

                if (DataController.containsStringArray(news_inputs[num]) == false) {

                    DataController.addToLater(DataController.getItem(num));

                } else {

                    DataController.removeFromLater(DataController.getItem(num)[0]);

                }
                ;
                // DataController.setlater(num);
                // checkReadLaterPane.setVisible(DataController.getlater()[num]);
                checkReadLaterPane.setVisible(DataController.containsStringArray(news_inputs[num]));

            });
            checkReadLaterPane.getChildren().addAll(tickImageView, viewInReadLaterLabel);
            checkReadLaterPane.setVisible(DataController.containsStringArray(news_inputs[num]));
            // checkReadLaterPane.setVisible(DataController.getlater()[num]);

            art.getChildren().addAll(moreButton, checkReadLaterPane, laterButton);
            articleContainer.getChildren().add(art);

        }

    }

    public Boolean transpose(Boolean init) {
        return !init; // Trả về giá trị mới của biến init (readlater)
    }

    public void setPane(String title, String type, String news, String summary, String category, String tag,
            String time, String facebookLink, String imageLink) {
        detailController.showDetail();
        detailController.setDetail(title, type, news, summary, category, tag, time, facebookLink, imageLink);
    }

    private Pane checkReadLaterPane;
    private Boolean read_later = false;

    public AnchorPane createArticle(String _title, String _type, String _news, String _summary, String link) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(675, 200);

        Pane pane1 = new Pane();
        pane1.setLayoutX(10);
        pane1.setLayoutY(10);
        pane1.setPrefSize(655, 180);
        pane1.setStyle("-fx-background-color: #d13d91;");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(41);
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.49);
        dropShadow.setWidth(41);
        dropShadow.setColor(Color.rgb(188, 62, 136, 0.73));
        pane1.setEffect(dropShadow);

        Pane pane2 = new Pane();
        pane2.setLayoutX(0);
        pane2.setLayoutY(0);
        pane2.setPrefSize(655, 180);
        pane2.setStyle("-fx-background-color: #020023;");

        ImageView linkImageView = new ImageView(link);
        // ImageView linkImageView = new ImageView(new Image("file:" + link));
        linkImageView.setLayoutX(22);
        linkImageView.setLayoutY(15);
        linkImageView.setFitWidth(170);
        linkImageView.setFitHeight(130);

        Label titleLabel = new Label(_title);
        titleLabel.setLayoutX(218);
        titleLabel.setLayoutY(15);
        titleLabel.setPrefSize(325, 47);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("System Bold", 15));
        titleLabel.setWrapText(true);

        Label typeLabel = new Label(_type);
        typeLabel.setLayoutX(218);
        typeLabel.setLayoutY(65);
        typeLabel.setTextFill(Color.rgb(209, 61, 145));
        typeLabel.setFont(Font.font(13));
        typeLabel.setWrapText(true);
        typeLabel.setPrefSize(250, 20);

        Label newsLabel = new Label(_news);
        newsLabel.setLayoutX(480);
        newsLabel.setLayoutY(65);
        newsLabel.setTextFill(Color.rgb(134, 50, 189));
        newsLabel.setFont(Font.font("System Bold", 13));
        newsLabel.setWrapText(true);
        newsLabel.setPrefSize(110, 20);

        Label summaryLabel = new Label(_summary);
        summaryLabel.setLayoutX(218);
        summaryLabel.setLayoutY(94);
        summaryLabel.setPrefSize(415, 72);
        summaryLabel.setTextFill(Color.WHITE);
        summaryLabel.setFont(Font.font("System Bold", 13));
        summaryLabel.setWrapText(true);

        checkReadLaterPane = new Pane();
        checkReadLaterPane.setLayoutX(0);
        checkReadLaterPane.setLayoutY(130);
        checkReadLaterPane.setPrefSize(200, 50);
        URL tick = getClass().getResource("/image/tick.png");
        Image laterImage = new Image(tick.toExternalForm());
        ImageView tickImageView = new ImageView(laterImage);
        tickImageView.setLayoutX(5);
        tickImageView.setLayoutY(10);
        tickImageView.setFitWidth(35);
        tickImageView.setFitHeight(35);

        Label viewInReadLaterLabel = new Label("View in Read Later");
        viewInReadLaterLabel.setLayoutX(44);
        viewInReadLaterLabel.setLayoutY(19);
        viewInReadLaterLabel.setTextFill(Color.rgb(242, 117, 117));
        viewInReadLaterLabel.setFont(Font.font("System Italic", 11));

        Label moreLabel = new Label("More");
        moreLabel.setLayoutX(604);
        moreLabel.setLayoutY(42);
        moreLabel.setTextFill(Color.WHITE);
        moreLabel.setFont(Font.font("System Bold", 12));

        Button laterButton = new Button();
        laterButton.setLayoutX(539);
        laterButton.setLayoutY(2);
        laterButton.setPrefSize(40, 50);
        laterButton.setStyle("-fx-background-color: #020023;");
        URL imageUrl = getClass().getResource("/image/later.png");

        Image aterImage = new Image(imageUrl.toExternalForm());
        ImageView laterImageView = new ImageView(aterImage);
        laterImageView.setFitWidth(40);
        laterImageView.setFitHeight(50);
        laterButton.setGraphic(laterImageView);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(1.0);
        colorAdjust.setContrast(0.06);
        laterImageView.setEffect(colorAdjust);
        laterButton.setOnAction(event -> {
            setsPane(!read_later);
            checkReadLaterPane.setVisible(read_later);
        });

        pane2.getChildren().addAll(linkImageView, titleLabel, typeLabel, newsLabel, summaryLabel, moreLabel);
        checkReadLaterPane.getChildren().addAll(tickImageView, viewInReadLaterLabel);
        checkReadLaterPane.setVisible(read_later);
        pane1.getChildren().addAll(pane2);
        root.getChildren().addAll(pane1);

        return root;
    }

    public boolean getPane() {
        return read_later;
    }

    public void setsPane(Boolean init) {
        this.read_later = init;
    }

}
