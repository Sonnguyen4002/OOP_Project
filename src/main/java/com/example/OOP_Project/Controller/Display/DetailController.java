package com.example.OOP_Project.Controller.Display;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DetailController {

    @FXML
    private AnchorPane detail;

    public void hideDetail() {
        detail.setVisible(false);
    }

    public void showDetail() {
        detail.setVisible(true);
    }

    @FXML
    private ImageView img_detail;

    @FXML
    private Label title_detail;
    @FXML
    private Label type_detail;

    @FXML
    private Label news_detail;

    @FXML
    private Label summary_detail;

    @FXML
    private Label category_detail;

    @FXML
    private Label tag_detail;

    @FXML
    private Label time_detail;

    @FXML
    private Button link_detail;

    @FXML
    private void openLink(String url) {
        // Xử lý sự kiện khi nút được nhấn
        link_detail.setOnAction((ActionEvent event) -> {
            // Mở liên kết web trong trình duyệt mặc định của hệ thống

            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    java.net.URI uri = new java.net.URI(url);
                    desktop.browse(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openImage(String imagePath) {
        Image image = new Image(imagePath);
        img_detail.setImage(image);
    }

    public void setDetail(String title, String type, String news, String summary, String category, String tag,
            String time, String url, String image) {
        title_detail.setText(title);
        type_detail.setText(type);
        news_detail.setText(news);
        summary_detail.setText(summary);
        category_detail.setText(category);
        tag_detail.setText(tag);
        time_detail.setText(time);
        openLink(url);
        openImage(image);
    }

}
