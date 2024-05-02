package com.example.cultureconnect;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.calendar.CalendarCell;
import com.example.cultureconnect.calendar.ProjektCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private int calendarRows = 35;//skal sættes af antallet af projekter.
    private int columnWidth = 35;
    private int rowHeight = 35;
    private Logic logic = new Logic();



    public void initialize() {
        populateGridPane();
        fillCalendarWithProjects();
        loadUsers();

        //TODO scroll til denne uge.


        //TODO highlight the current week
    }

    public void fillCalendarWithProjects(){
        List<Projekt> projects = logic.getProjects();
        if (projects.isEmpty()){
            //TODO make a label that says "No projects found" and show it in the middle of the gridpane, at the current week.
        } else {
            for (Projekt projekt : projects) {
                int startWeek = getWeekNumber(projekt.getStartDate());
                int endWeek = getWeekNumber(projekt.getEndDate());
                int length = endWeek - startWeek + 1; //eksempel 17 - 19 = 2, så plus en for at få det til at passe.
                ProjektCell pcell = new ProjektCell(length, projekt.getColor());
                pcell.setHeight(rowHeight);
                pcell.setWidth(columnWidth * length); // Adjust the width of the cell
                //make the cell red
                pcell.setFill(javafx.scene.paint.Color.RED);
                CalendarGridPane.add(pcell, startWeek, 1);
                GridPane.setColumnSpan(pcell, length); // Make the cell span multiple weeks
            }
        }
    }

    public int getCurrentWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR) - 1;
    }




    public void populateGridPane() {
        //fills the gridpane with empty cells and weeknumbers.
        for (int row = 0; row < calendarRows; row++) {
            for (int col = 0; col < calendarColumns; col++) {
                if(row == 0){
                    Label label = new Label(String.valueOf(col));
                    label.setAlignment(javafx.geometry.Pos.CENTER);
                    CalendarGridPane.setAlignment(javafx.geometry.Pos.CENTER);
                    CalendarGridPane.add(label, col, row);
                }
                CalendarCell cell = new CalendarCell(); // Create a new Cell instance
                cell.setWidth(columnWidth); // Set the width of the cell
                cell.setHeight(rowHeight); // Set the height of the cell
                CalendarGridPane.add(cell, col, row); // Add the cell to the GridPane at the specified column and row
            }
        }
        CalendarGridPane.setGridLinesVisible(true);
        CalendarGridPane.setHgap(1);
        CalendarGridPane.setVgap(1);
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
        if (ListviewSearchTextField != null){
            String searchText = ListviewSearchTextField.getText().toLowerCase();

            List<Person> searchResults = logic.getPersons().stream()
                    .filter(person -> person.getName().toLowerCase().contains(searchText)
                            || person.getEmail().toLowerCase().contains(searchText)
                            || String.valueOf(person.getTlfNr()).contains(searchText))
                    .distinct()
                    .collect(Collectors.toList());

            UserOrLocationListview.setItems(FXCollections.observableList(searchResults));
        }
    } //skal man kunne søge også med at taste enter?

    public void userToggleButtonPressed(ActionEvent event) {
        //TODO when the user togglebutton is pressed, the listview should be filtered to only show users.
    }

    public void locationToggleButtonPressed(ActionEvent event) {
        //TODO when the location togglebutton is pressed, the listview should be filtered to only show locations.
    }

    public void loadUsers(){
        ObservableList users = FXCollections.observableArrayList();
        List<Person> persons = logic.getPersons();
        users.addAll(persons);

        UserOrLocationListview.setItems(users);
    }
}