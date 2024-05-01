package com.example.cultureconnect.calendar;

import com.example.cultureconnect.Projekt.Projekt;
import java.util.ArrayList;
import java.util.List;

public class ProjectRow {
    private Projekt project;
    private List<String> cells;

    public ProjectRow(Projekt project, int timeFrame) {
        this.project = project;
        this.cells = new ArrayList<>();
        for (int i = 0; i < timeFrame; i++) {
            cells.add("");
        }
    }

    public Projekt getProject() {
        return project;
    }

    public List<String> getCells() {
        return cells;
    }

    public void setCell(int index, String value) {
        this.cells.set(index, value);
    }
}