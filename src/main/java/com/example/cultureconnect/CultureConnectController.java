package com.example.cultureconnect;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.calendar.CCCalendar;
import com.example.cultureconnect.calendar.CalendarCell;
import com.example.cultureconnect.calendar.ProjectRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CultureConnectController {
    public HBox UserOrLocationToggleHBox;
    public ToggleButton UserToggleButton;
    public ToggleButton LocationToggleButton;
    public HBox ListviewSearchHBox;
    public TextField ListviewSearchTextField;
    public Button ListviewSearchButton;
    public ListView UserOrLocationListview;
    public HBox AdminMenuHBox;
    public Button AdminMenuNewUserOrLocationButton;
    public TabPane CalendarTabPane;
    public Tab CalendarTab;
    public ButtonBar ButtonBarForZoomAndNewProject;
    public Button ZoomInCalendarButton, ZoomOutCalendarButton;
    public Button NewProjectCalendarButton;
    public AnchorPane AnchorPaneForCalendarTableView;
    public TableView CalendarTableView;//TODO make scrollable
    public SplitPane MainSplitPane;

    private int timeFrame = 52;
    private Logic logic = new Logic();


    public void initialize() {
        populateTableView();


        //TODO scroll til denne uge.
        CalendarTableView.scrollToColumnIndex(getCurrentWeekNumber());


        //TODO highlight the current week
        TableColumn column = (TableColumn) CalendarTableView.getColumns().get(getCurrentWeekNumber());

    }

    public int getCurrentWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR) - 1;
    }




    public void populateTableView() {
        List<ProjectRow> rows = generateRows();
        CCCalendar cccalendar = new CCCalendar(timeFrame);
        List<TableColumn> columns = cccalendar.populateTableView();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn<ProjectRow, String> column = columns.get(i);
            final int weekIndex = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCells().get(weekIndex)));
            column.setCellFactory(param -> new CalendarCell());
        }
        CalendarTableView.getColumns().addAll(columns);
        CalendarTableView.setItems(FXCollections.observableArrayList(rows));
    }

    private List<ProjectRow> generateRows() {
        List<ProjectRow> rows = new ArrayList<>();
        List<Projekt> projects = logic.getProjects();
        for (Projekt project : projects) {
            ProjectRow row = new ProjectRow(project, timeFrame);
            int startWeek = getWeekNumber(project.getStartDate());
            int endWeek = getWeekNumber(project.getEndDate());
            for (int i = startWeek; i <= endWeek; i++) {
                row.setCell(i, project.getTitel());
            }
            rows.add(row);
        }
        return rows;
    }

    private int getWeekNumber(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR) - 1;
    }

    public void zoomInCalendarButtonPressed(ActionEvent event) {
        //TODO make the calender zoom in when the button is pressed
    }

    public void zoomOutCalendarButtonPressed(ActionEvent event) {
        //TODO make the calender zoom out when the button is pressed
    }

    public void newProjectCalendarButtonPressed(ActionEvent event) {
        //TODO open a new tab in the calendar tabpane, where the user can create a new project
    }

    public void adminMenuNewUserOrLocationButtonPressed(ActionEvent event) {
        //TODO only when admin is logged in, should the button be visible, and when pressed, open a dialogwindow next to the listview, where the admin can create a new user.
    }

    public void listviewSearchButtonPressed(ActionEvent event) {
        //TODO when the button is pressed, the listview should be filtered to only show the search results.
    }

    public void userToggleButtonPressed(ActionEvent event) {
        //TODO when the user togglebutton is pressed, the listview should be filtered to only show users.
    }

    public void locationToggleButtonPressed(ActionEvent event) {
        //TODO when the location togglebutton is pressed, the listview should be filtered to only show locations.
    }
}