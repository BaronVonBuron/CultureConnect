package com.example.cultureconnect;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.calendar.CalendarCell;
import com.example.cultureconnect.calendar.ProjektCell;
import com.example.cultureconnect.customListview.LokationListCell;
import com.example.cultureconnect.customListview.PersonListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    public Tab editProjectTab;
    public Button EditProjektButton;
    public Tab ProjektTab;
    public Tab CreateNewProjektTab;
    public Button CancelCreateProjektButton;
    public Button CreateProjektButton;
    public TextArea CreateNewProjectNotesTextArea;
    public Button CreateNewProjectAddActivityButton;
    public DatePicker CreateNewProjektEndDatePicker;
    public TextArea CreateNewProjektDescriptionTextArea;
    public TextField CreateNewProjectTitleTextField;
    public ListView CreateNewProjektPersonListView;
    public ListView CreateNewProjektCreatorListView;
    public ListView CreateNewProjectLokationListView;
    public Label ProjektTitelLabel;

    private int calendarColumns = 52;
    private int calendarRows = 35;//skal sættes af antallet af projekter.
    private int columnWidth = 35;
    private int rowHeight = 35;
    Tooltip projektTooltip = new Tooltip();
    private Logic logic = new Logic();
    ObservableList<PersonListCell> users = FXCollections.observableArrayList();
    ObservableList<LokationListCell> places = FXCollections.observableArrayList();
    List<Projekt> projects = new ArrayList<>();




    public void initialize() {
        startSequence();
        //TODO highlight the current week
    }


    public void startSequence(){
        CalendarTabPane.getTabs().remove(editProjectTab);
        CalendarTabPane.getTabs().remove(ProjektTab);
        CalendarTabPane.getTabs().remove(CreateNewProjektTab);
        logic.updateLists();
        populateGridPane();
        while (logic.getIsUpdating()) {
            //wait 100 ms
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Lists are updated");
        projects = logic.getProjects();
        fillCalendarWithProjects();
        loadLokations();
        loadUsers();
        //TODO scroll til denne uge.
        CalendarScrollPane.setHvalue(getCurrentWeekNumber() / 52.0);
    }


    public void fillCalendarWithProjects(){
        if (projects.isEmpty()){
            System.out.println("No projects to show");
        } else {
            int noOfProjects = 1;
            AtomicInteger reuseableRow = new AtomicInteger();
            HashMap<Integer, Projekt> projektGrid = new HashMap<>();
            for (Projekt projekt : projects) {
                if (noOfProjects > 15){
                    //TODO check if the projects are finished and a couple of weeks old - if yes, reuse the row. if not, carry on.
                    projektGrid.forEach((key, value) -> {//check if the projekt's start date is 4 weeks after the last projekt's end date.
                        Date endDate = value.getEndDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(endDate);
                        calendar.add(Calendar.WEEK_OF_YEAR, 4);
                        Date endDatePlus4Weeks = calendar.getTime();
                        if (endDatePlus4Weeks.before(projekt.getStartDate())) {
                            reuseableRow.set(key);
                        }
                    });
                }
                int startWeek = getWeekNumber(projekt.getStartDate());
                int endWeek = getWeekNumber(projekt.getEndDate());
                int length = endWeek - startWeek + 1; //eksempel 17 - 19 = 2, så plus en for at få det til at passe.
                ProjektCell pcell = new ProjektCell(length, projekt.getColor(), projekt);
                pcell.setHeight(rowHeight);
                pcell.setWidth(columnWidth * length); // Adjust the width of the cell
                //make the cell red
                pcell.setFill(javafx.scene.paint.Color.RED);
                if (reuseableRow.get() > 0){
                    CalendarGridPane.add(pcell, startWeek, reuseableRow.get());
                    GridPane.setColumnSpan(pcell, length); // Make the cell span multiple weeks
                    projektGrid.put(reuseableRow.get(), projekt);
                    reuseableRow.set(0);
                } else {
                    CalendarGridPane.add(pcell, startWeek, noOfProjects);
                    GridPane.setColumnSpan(pcell, length); // Make the cell span multiple weeks
                    projektGrid.put(noOfProjects, projekt);
                }
                noOfProjects++;
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
        if (!CalendarTabPane.getTabs().contains(CreateNewProjektTab)){
            CalendarTabPane.getTabs().add(CreateNewProjektTab);
            CalendarTabPane.getSelectionModel().select(CreateNewProjektTab);
        } else {
            CalendarTabPane.getSelectionModel().select(CreateNewProjektTab);
        }
    }

    public void adminMenuNewUserOrLocationButtonPressed(ActionEvent event) {
        //TODO only when admin is logged in, should the button be visible, and when pressed, open a dialogwindow next to the listview, where the admin can create a new user.
    }

    public void listviewSearchButtonPressed() {
        if (ListviewSearchTextField != null && UserToggleButton.isSelected()){
            String searchText = ListviewSearchTextField.getText().toLowerCase();
            //search for persons
            List<PersonListCell> searchResults = users.stream()
                    .filter(personListCell -> personListCell.getName().toLowerCase().contains(searchText)
                            || personListCell.getEmail().toLowerCase().contains(searchText)
                            || String.valueOf(personListCell.getTlfNr()).contains(searchText))
                    .distinct()
                    .collect(Collectors.toList());

            UserOrLocationListview.setItems(FXCollections.observableList(searchResults));
        } else if (ListviewSearchTextField != null && !UserToggleButton.isSelected()) {
            String searchText = ListviewSearchTextField.getText().toLowerCase();

            // Search for locations
            List<LokationListCell> locationSearchResults = places.stream()
                    .filter(locationListCell -> locationListCell.getName().toLowerCase().contains(searchText)
                            || locationListCell.getDescription().toLowerCase().contains(searchText))
                    .distinct()
                    .collect(Collectors.toList());

            UserOrLocationListview.setItems(FXCollections.observableList(locationSearchResults));
        }
    }

    public void userToggleButtonPressed(ActionEvent event) {
        UserOrLocationListview.setItems(users);
    }

    public void locationToggleButtonPressed(ActionEvent event) {
        UserOrLocationListview.setItems(places);
    }

    public void loadUsers(){
        List<Person> persons = logic.getPersons();
        List<PersonListCell> cells = new ArrayList<>();
        for (Person person : persons) {
            cells.add(new PersonListCell(person));
        }
        users.addAll(cells);

        UserOrLocationListview.setItems(users);
    }

    public void loadLokations(){
        List<Lokation> lokations = logic.getLocations();
        List<LokationListCell> cells = new ArrayList<>();
        for (Lokation lokation : lokations) {
            cells.add(new LokationListCell(lokation));
        }
        places.addAll(cells);
        UserOrLocationListview.setItems(places);
    }

    public void SearchFieldKeyPressed(KeyEvent keyEvent) {
        listviewSearchButtonPressed();
    }

    public void GridPaneClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof ProjektCell && !this.projektTooltip.isShowing()) {
            ProjektCell cell = (ProjektCell) mouseEvent.getTarget();
            openSmallProjektDialogWindow(cell.getProjekt(), mouseEvent);
        } else {
            this.projektTooltip.hide();
        }
    }

    public void openSmallProjektDialogWindow(Projekt projekt, MouseEvent mouseEvent){
        VBox dialogVBox = new VBox();
        Label titleLabel = new Label(projekt.getTitel());
        titleLabel.getStyleClass().add("ProjectTooltipTitle");
        Label descriptionLabel = new Label(projekt.getDescription());
        Label startDateLabel = new Label("Startdato: " + projekt.getStartDate().toString());
        Label endDateLabel = new Label("Slutdato: " + projekt.getEndDate().toString());
        Button moreInfoButton = new Button("Se mere");
        dialogVBox.getChildren().add(titleLabel);
        dialogVBox.getChildren().add(descriptionLabel);
        dialogVBox.getChildren().add(startDateLabel);
        dialogVBox.getChildren().add(endDateLabel);
        dialogVBox.getChildren().add(moreInfoButton);
        dialogVBox.setPrefHeight(100);
        dialogVBox.setPrefWidth(200);
        dialogVBox.setSpacing(10);
        projektTooltip.setGraphic(dialogVBox);
        projektTooltip.setStyle(getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        projektTooltip.getStyleClass().add("ProjectTooltip");



        projektTooltip.show(CalendarGridPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        moreInfoButton.setOnAction(event -> {
            openProjektTab(projekt);
            projektTooltip.hide();
        });
    }

    public void openProjektTab(Projekt projekt){
        //check if the projekt tab is already open with the projekt based on the title. If it is, select the tab, if not, create a new tab.
        if(CalendarTabPane.getTabs().contains(ProjektTab)){
            if (ProjektTitelLabel.equals(projekt.getTitel())){
                CalendarTabPane.getSelectionModel().select(ProjektTab);
            }
        } else {
            CalendarTabPane.getTabs().remove(ProjektTab);
            ProjektTab = new Tab(projekt.getTitel());
            CalendarTabPane.getTabs().add(ProjektTab);
            CalendarTabPane.getSelectionModel().select(ProjektTab);
        }
        //TODO open the projekt tab, and show the projekt information.
    }


    public void editProjektButtonPressed(ActionEvent actionEvent) {

    }

    public void cancelCreateProjektButtonPressed(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annuller oprettelse af  projekt");
        alert.setHeaderText("Er du sikker på at du vil annullere oprettelsen af projektet?");
        alert.setContentText("Alle indtastede informationer vil gå tabt.");
        alert.getButtonTypes().clear();
        ButtonType buttonType = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonType1 = new ButtonType("Nej", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().addAll(buttonType, buttonType1);
        alert.showAndWait();
        if (alert.getResult() == buttonType){
            CalendarTabPane.getSelectionModel().select(CalendarTab);
            CalendarTabPane.getTabs().remove(CreateNewProjektTab);
        }
    }

    public void createProjektButtonPressed(ActionEvent actionEvent) {
        //TODO create a new projekt, and add it to the database.
        //TODO check if the projekt is valid, and if not, show an error message.
        //TODO if the projekt is valid, add it to the database, and update the calendar.
        //TODO update the calendar with the new projekt.
    }

    public void createNewProjectAddActivityButtonPressed(ActionEvent actionEvent) {

    }

    public void createNewProjektEndDatePickerPressed(ActionEvent actionEvent) {

    }

    public void redigerNyAktivitetKnapPressed(ActionEvent actionEvent) {
    }

    public void sletProjektKnapPressed(ActionEvent actionEvent) {
    }

    public void gemÆndringerKnapPressed(ActionEvent actionEvent) {
    }
}