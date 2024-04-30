package com.example.cultureconnect;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.calendar.CCCalendar;
import com.example.cultureconnect.calendar.CalendarCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

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

    //testprojekt

    Lokation lokation = new Lokation("Tønder Bibliotek", "Tønder Bibliotek", Color.RED);
    Person person = new Person("Marianne", "Hansen", lokation, "Udlånsafdelingen", "tilfældig@email.dk", 12345678, null);
    List<Person> personList = new ArrayList<>();


    /*
    Gør så der ikke kommer en grim dobbeltpil over divideren i splitpane

     */


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



        //testing
        Date start = new Date();
        Date slut = new Date();
        personList.add(person);
        Projekt projekt = new Projekt("test", personList, "test", person, lokation, start, new Date(start.getTime() + 10000000));

        List<Projekt> projekter = new ArrayList<>();
        projekter.add(projekt);

        for (Projekt projekt1 : projekter) {
            CalendarCell cc = new CalendarCell(projekt1);

            CalendarTableView.getItems().add(cc.getCell());
        }
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