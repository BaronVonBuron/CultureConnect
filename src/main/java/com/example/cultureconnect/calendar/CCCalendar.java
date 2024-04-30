package com.example.cultureconnect.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.List;

public class CCCalendar {
    private int timeFrame;


    public CCCalendar(int timeFrame) {
        this.timeFrame = timeFrame;
    }



    public List populateTableView() {
        List<TableColumn> columns = new ArrayList<>();
        for (int i = 1; i <= timeFrame; i++) {
            TableColumn column = new TableColumn();
            column.setText(String.valueOf(i));
            column.setMaxWidth(34);
            column.setMinWidth(34);
            column.setReorderable(false);
            column.setResizable(false);
            column.setSortable(false);
            columns.add(column);
        }
        return columns;
    }
}
