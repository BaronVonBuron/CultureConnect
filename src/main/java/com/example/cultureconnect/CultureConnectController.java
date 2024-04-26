package com.example.cultureconnect;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
    public TableView CalendarTableView;


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