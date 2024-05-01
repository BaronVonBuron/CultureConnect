package com.example.cultureconnect;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.calendar.CCCalendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Calendar;

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

        //add 100 rows as a test
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < 100; i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int j = 0; j < timeFrame; j++) {
                row.add("test");
            }
            data.add(row);
        }
        CalendarTableView.setItems(data);


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
        CCCalendar cccalendar = new CCCalendar(timeFrame);
        CalendarTableView.getColumns().addAll(cccalendar.populateTableView());

        //TODO populate the tableview with the data from the database

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