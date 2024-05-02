package com.example.cultureconnect;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.calendar.CalendarCell;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Calendar;
import java.util.Date;

public class CultureConnectController {
    public HBox UserOrLocationToggleHBox;
    public ToggleButton UserToggleButton;
    public ToggleButton LocationToggleButton;
    public ToggleGroup group; //TODO make one button selected from the start, and make sure allways one button is selected.
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
    public SplitPane MainSplitPane;
    public AnchorPane LeftsideAnchorPane;
    public GridPane CalendarGridPane;
    public ScrollPane CalendarScrollPane;

    private int calendarColumns = 52;
    private int calendarRows = 10;//skal sættes af antallet af projekter.
    private int columnWidth = 35;
    private int rowHeight = 35;
    private Logic logic = new Logic();



    public void initialize() {
        populateGridPane();
        CalendarGridPane.setGridLinesVisible(true);

        //TODO scroll til denne uge.


        //TODO highlight the current week
    }

    public int getCurrentWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR) - 1;
    }




    public void populateGridPane() {
        for (int row = 0; row < calendarRows; row++) {
            for (int col = 0; col < calendarColumns; col++) {
                CalendarCell cell = new CalendarCell(); // Create a new Cell instance
                cell.setWidth(columnWidth); // Set the width of the cell
                cell.setHeight(rowHeight); // Set the height of the cell
                // Set the fill of the cell to a random color
                cell.setFill(javafx.scene.paint.Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                CalendarGridPane.add(cell, col, row); // Add the cell to the GridPane at the specified column and row
            }
        }
    }


    private int getWeekNumber(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR) - 1; //Returnerer ugenummeret, ud fra den givne dato
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