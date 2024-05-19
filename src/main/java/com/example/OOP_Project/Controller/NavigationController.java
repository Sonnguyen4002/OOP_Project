package com.example.OOP_Project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NavigationController {
    private void switchToNewPage(ActionEvent event, String name) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(name)));
        System.out.println(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToday(ActionEvent event) throws IOException {
        switchToNewPage(event, "/fxml/Today.fxml");
    }

    public void switchReadlater(ActionEvent event) throws IOException {
        switchToNewPage(event, "/fxml/ReadLater.fxml");
    }

    public void switchContact(ActionEvent event) throws IOException {
        switchToNewPage(event, "/fxml/Contact.fxml");
    }
}
