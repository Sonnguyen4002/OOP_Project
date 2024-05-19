package com.example.OOP_Project.Controller.Storage;
// Articles

import java.util.ArrayList;
import java.util.Arrays;
public class DataController {
        private static String[][] inputs =new String[10][10];

private static ArrayList<String[]> Later = new ArrayList<>();
public static boolean containsStringArray(String[] arrayToCheck) {
    for (String[] array : Later) {
        if (Arrays.equals(array, arrayToCheck)) {
            return true;
        }
    }
    return false;
}
static String[] init =  {"Welcome to our ReadLater Page", "This is the instruction", "", "Instructionssssss", "Category 1", "Tag 1", "Time 1", "Link 1", "Image Link 1", "1"};
public static void addToLater(String[] newItem) {
        Later.add(newItem); 
    }
public static void removeFromLater(String title) {
        
        for (int i = 0; i < Later.size(); i++) {
            String[] item = Later.get(i);
            // Kiểm tra nếu title của item trùng với title cần xóa
            if (item[0].equals(title)) {
                Later.remove(i); // Xóa phần tử từ Later
                return;
            }
        }
        System.out.println("Item with title '" + title + "' not found in Later.");
    }

public static ArrayList<String[]> getRead() {
        return DataController.Later;
}
public static void initialRead() {
        Later.add(init); 
}
public static String[][] getInput() {
    return DataController.inputs;
}
public static String[] getItem(int pos) {
        return DataController.inputs[pos];
    }

public static void setInput(String[] new_inputs,int pos) {
        DataController.inputs[pos] = new_inputs;
}
public static void setReadLater(int pos, String num) {
    DataController.inputs[pos][9] = num;
}
public static String debug(int pos) {
    return DataController.inputs[pos][9];
}





}
