package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.calendar.CalendarCell;
import com.example.cultureconnect.calendar.ProjektCell;
import com.example.cultureconnect.customListview.LokationListCell;
import com.example.cultureconnect.customListview.PersonListCell;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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

    //Create new projekt tab////////////////////////////////////////////////
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
    public DatePicker plannedActivityStartDatePicker;
    public DatePicker plannedActivityEndDatePicker;
    public TextField plannedActivityTitleTextField;
    @FXML
    private TextArea nytProjektPlanlagteMøderFelt;
    //Create new projekt tab slut////////////////////////////////////////////

    //Edit projekt tab start////////////////////////////////////////////
    @FXML
    private TextArea redigerBeskrivelseFelt;

    @FXML
    private TextArea redigerNoterFelt;

    @FXML
    private TextArea redigerPlanlagteMøderFelt;
    //Edit projekt tab slut////////////////////////////////////////////

    private int calendarColumns = 52;
    private int calendarRows = 35;//skal sættes af antallet af projekter.
    private int columnWidth = 35;
    private int rowHeight = 35;
    Tooltip projektTooltip = new Tooltip();
    private Logic logic;
    ObservableList<PersonListCell> users = FXCollections.observableArrayList();
    ObservableList<LokationListCell> places = FXCollections.observableArrayList();
    List<Projekt> projects = new ArrayList<>();
    //SimpleIntegerProperty count = new SimpleIntegerProperty();
    int rowHeightTextareas = 10;




    public void initialize() {
        this.logic = Logic.getInstance();
        startSequence();
        loginSequence();

        autoExpandingTextareas(CreateNewProjektDescriptionTextArea, CreateNewProjectNotesTextArea, nytProjektPlanlagteMøderFelt,
                redigerBeskrivelseFelt, redigerNoterFelt, redigerPlanlagteMøderFelt
        );

        //TODO highlight the current week
    }

    public void autoExpandingTextareas(TextArea... textAreas) {
        for (TextArea textArea : textAreas) {
            textArea.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            // Initialize count with the initial height of the text area
            SimpleIntegerProperty count = new SimpleIntegerProperty((int) textArea.getPrefHeight());

            // Bind height properties
            textArea.prefHeightProperty().bindBidirectional(count);
            textArea.minHeightProperty().bindBidirectional(count);

            // Listen for scroll events
            textArea.scrollTopProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
                if(newVal.intValue() > rowHeightTextareas){
                    count.set(count.get() + newVal.intValue());
                }
            });
        }
    }


    public void startSequence(){
        CalendarTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        CalendarTab.setOnCloseRequest(event -> event.consume());
        CreateNewProjektTab.setOnCloseRequest(event -> event.consume());
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

    public void loginSequence(){
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/example/cultureconnect/CultureConnectLogin.fxml"));
        Stage loginStage = new Stage();
        loginStage.setTitle("CultureConnect Login");
        try {
            Scene loginScene = new Scene(loginLoader.load());
            CultureConnectLoginController loginController = loginLoader.getController();
            loginController.setLogic(this.logic);
            loginStage.setScene(loginScene);
            loginStage.setAlwaysOnTop(true);
            loginStage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            loginStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        for (PersonListCell user : users) {
            if (user.getPerson().getEmail().equals(logic.getCurrentUser().getEmail())){
                PersonListCell pcell = new PersonListCell(logic.getCurrentUser());
                CreateNewProjektCreatorListView.getItems().add(pcell);
                users.remove(user);
                UserOrLocationListview.setItems(users);
                break;
            }
        }
    }


    public void adminMenuNewUserOrLocationButtonPressed(ActionEvent event) {
        //TODO only when admin is logged in, should the button be visible, and when pressed, open a dialogwindow next to the listview, where the admin can create a new user.
        if (UserToggleButton.isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cultureconnect/NewUserDialog.fxml"));
                Stage addUser = new Stage();
                addUser.setTitle("Opret Ny Bruger");
                AnchorPane editLayout = loader.load();
                Scene scene = new Scene(editLayout);
                addUser.setScene(scene);
                addUser.setResizable(false);

                addUser.show();

            } catch (IOException e){
                System.out.println("Can't open new user window");
            }

        } else if (LocationToggleButton.isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cultureconnect/NewLocationDialog.fxml"));
                Stage addLocation = new Stage();
                addLocation.setTitle("Opret Ny Lokation");
                AnchorPane editLayout = loader.load();
                Scene scene = new Scene(editLayout);
                addLocation.setScene(scene);
                addLocation.setResizable(false);

                addLocation.show();

            } catch (IOException e){
                System.out.println("Can't open new location window");
            }
        }
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
        AdminMenuNewUserOrLocationButton.setText("Ny Bruger");
    }

    public void locationToggleButtonPressed(ActionEvent event) {
        UserOrLocationListview.setItems(places);
        AdminMenuNewUserOrLocationButton.setText("Ny Lokation");
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
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
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

    public void createProjektLokationDragDropped(DragEvent dragEvent) {
        // Get the Dragboard
        Dragboard db = dragEvent.getDragboard();

        // Check if the Dragboard has a String. This should be the name of the Lokation
        if (db.hasString()) {
            // Get the name of the Lokation from the Dragboard
            String lokationName = db.getString();

            // Find the Lokation with this name in your list of Lokations
            LokationListCell droppedLokation = places.stream()
                    .filter(lokation -> lokation.getName().equals(lokationName))
                    .findFirst()
                    .orElse(null);

            // If the Lokation was found, add it to the ListView
            if (droppedLokation != null) {
                CreateNewProjectLokationListView.getItems().add(droppedLokation);
                places.remove(droppedLokation);
                UserOrLocationListview.setItems(places);
            }

            // Indicate that the drag data was successfully transferred and used
            dragEvent.setDropCompleted(true);
        } else {
            // Indicate that no valid data was in the Dragboard
            dragEvent.setDropCompleted(false);
        }

        // Consume the event to indicate it has been handled
        dragEvent.consume();
    }

    public void createProjektLokationDraggedOver(DragEvent dragEvent) {
        // Accept the drag operation if the dragboard has a String
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            System.out.println("Dragged over to participant listview");
        }
        dragEvent.consume();
    }

    public void createProjektCreatorPersonDragDropped(DragEvent dragEvent) {
        // Get the Dragboard
        Dragboard db = dragEvent.getDragboard();
        // Check if the Dragboard has a String. This should be the name of the Person
        if (db.hasString()) {
            // Get the name of the Person from the Dragboard
            String personName = db.getString();
            // Find the Person with this name in your list of Persons
            PersonListCell droppedPerson = users.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            // If the Person was found, add it to the ListView
            if (droppedPerson != null) {
                CreateNewProjektCreatorListView.getItems().add(droppedPerson);
                users.remove(droppedPerson);
                UserOrLocationListview.setItems(users);
            }
            // Indicate that the drag data was successfully transferred and used
            dragEvent.setDropCompleted(true);
        } else {
            // Indicate that no valid data was in the Dragboard
            dragEvent.setDropCompleted(false);
        }
        // Consume the event to indicate it has been handled
        dragEvent.consume();
    }

    public void createProjektCreatorPersonDraggedOver(DragEvent dragEvent) {
        // Accept the drag operation if the dragboard has a String
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            System.out.println("Dragged over to participant listview");
        }
        dragEvent.consume();
    }

    public void createProjektParticipantPersonDragDropped(DragEvent dragEvent) {
        // Get the Dragboard
        Dragboard db = dragEvent.getDragboard();
        // Check if the Dragboard has a String. This should be the name of the Person
        if (db.hasString()) {
            // Get the name of the Person from the Dragboard
            String personName = db.getString();
            // Find the Person with this name in your list of Persons
            PersonListCell droppedPerson = users.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            // If the Person was found, add it to the ListView
            if (droppedPerson != null) {
                CreateNewProjektPersonListView.getItems().add(droppedPerson);
                users.remove(droppedPerson);
                UserOrLocationListview.setItems(users);
            }
            // Indicate that the drag data was successfully transferred and used
            dragEvent.setDropCompleted(true);
        } else {
            // Indicate that no valid data was in the Dragboard
            dragEvent.setDropCompleted(false);
        }
        // Consume the event to indicate it has been handled
        dragEvent.consume();
    }

    public void createProjektParticipantDraggedOver(DragEvent dragEvent) {
        // Accept the drag operation if the dragboard has a String
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            System.out.println("Dragged over to participant listview");
        }
        dragEvent.consume();
    }



    public void userLokationListViewDragDetected(MouseEvent mouseEvent) {

    }

    public void userLokationListViewDragDone(DragEvent dragEvent) {

    }
}