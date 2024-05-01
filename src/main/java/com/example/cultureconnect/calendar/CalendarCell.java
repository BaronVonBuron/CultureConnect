package com.example.cultureconnect.calendar;

import javafx.scene.control.TableCell;

public class CalendarCell extends TableCell<ProjectRow, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setStyle("");
        } else {
            setText(item); // Set the text to the project's title
            setStyle("-fx-background-color: " + getColorForProject(item)); // Set the background color based on the project
        }
    }

    private String getColorForProject(String item) {
        // Return a color based on the project. This is just an example, you can return any color you want.
        // You might need to adjust this method to fit your actual data structure and requirements.
        return item.isEmpty() ? "#FFFFFF" : "#0000FF";
    }
}