package com.example.OOP_Project.Controller.Visualization;

import java.util.ArrayList;

import com.example.OOP_Project.Controller.Display.Display;
import com.example.OOP_Project.Controller.Storage.DataController;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ReadLaterController extends Display {
    public static ArrayList<String[]> news_inputs = DataController.getRead();
    static String[] init = { "Welcome to our ReadLater Page", "This is the instruction", "", "Instructionssssss",
            "Category 1", "Tag 1", "Time 1", "Link 1", "Image Link 1", "1" };

    @FXML
    private VBox laterContainer;

    public void initialize() {
        for (String[] arr : news_inputs) {
            for (String str : arr) {
                System.out.print(str + " ");
            }
            System.out.println();
        }
        System.out.println(news_inputs);
        if (news_inputs.isEmpty()) {
            DataController.initialRead();
        } else {
            DataController.removeFromLater(init[0]);
        }
        addreadlater();
    }

    public void addreadlater() {
        addArticles(news_inputs, laterContainer);
    }
}
