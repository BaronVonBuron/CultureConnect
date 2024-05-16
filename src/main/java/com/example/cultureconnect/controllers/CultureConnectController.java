package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.Projekt.ProjektAktivitet;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CultureConnectController {
    public HBox UserOrLocationToggleHBox;
    public ToggleButton UserToggleButton;
    public ToggleButton LocationToggleButton;
    public ToggleGroup group;
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
    public ListView<PersonListCell> CreateNewProjektPersonListView;
    public ListView<LokationListCell> projektLokationerListview;////////
    public ListView<PersonListCell> projektProjektejereListview;
    public ListView<PersonListCell> projektProjektdeltagereListview;
    public Label projektBeskrivelse;
    public Label projektDato;
    public Label projektMøder;
    public Label projektNoter;////////
    public ListView<ProjektAktivitet> createProjektAktiviteterListview;
    private ObservableList<PersonListCell> createNewProjektPersonList;
    public ListView<PersonListCell> CreateNewProjektCreatorListView;
    private ObservableList<PersonListCell> createNewProjektCreatorList;
    public ListView<LokationListCell> CreateNewProjectLokationListView;
    private ObservableList<LokationListCell> createNewProjektLokationList;
    public Label ProjektTitelLabel;
    public DatePicker plannedActivityStartDatePicker;
    public DatePicker plannedActivityEndDatePicker;
    public TextField plannedActivityTitleTextField;
    //Create new projekt tab slut////////////////////////////////////////////

    //Edit projekt tab start////////////////////////////////////////////
    public Button gemÆndringerKnap;
    public Button sletProjektKnap;
    public TextField RedigerTitelPåAktivitetFelt;
    public Button redigerNyAktivitetKnap;
    public DatePicker redigerSlutdatoDatepicker;
    public DatePicker RedigerStartdatoDatepicker;
    public DatePicker redigerArrangementDatoDatepicker;
    public TextField redigerTitelFelt;
    public ListView<PersonListCell> redigerProjektdeltagereListview; //TODO lav drag ting
    public ListView<PersonListCell> redigerProjektejereListview;//TODO lav drag ting
    public ListView<LokationListCell> redigerLokationerListview;//TODO lav drag ting
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
    private Projekt currentlySelectedProjekt;
    Tooltip projektTooltip = new Tooltip();
    private Logic logic;
    ObservableList<PersonListCell> users = FXCollections.observableArrayList();
    ObservableList<PersonListCell> usersForNewProjekt;// = FXCollections.observableArrayList();
    ObservableList<LokationListCell> places = FXCollections.observableArrayList();
    ObservableList<LokationListCell> placesForNewProjekt;// = FXCollections.observableArrayList();
    List<Projekt> projects = new ArrayList<>();
    //SimpleIntegerProperty count = new SimpleIntegerProperty();
    int rowHeightTextareas = 10;




    public void initialize() {
        this.logic = Logic.getInstance();
        startSequence();
        loginSequence();

        autoExpandingTextareas(CreateNewProjektDescriptionTextArea, CreateNewProjectNotesTextArea,
                redigerBeskrivelseFelt, redigerNoterFelt, redigerPlanlagteMøderFelt
        );

        //eventlistener, to see which tab is selected
        CalendarTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(CreateNewProjektTab)){
                if (UserToggleButton.isSelected()){
                    UserOrLocationListview.setItems(placesForNewProjekt);
                    UserOrLocationListview.setItems(usersForNewProjekt);
                } else {
                    UserOrLocationListview.setItems(usersForNewProjekt);
                    UserOrLocationListview.setItems(placesForNewProjekt);
                }
            } else if (newValue.equals(CalendarTab)){

                if (UserToggleButton.isSelected()){
                    UserOrLocationListview.setItems(users);
                } else {
                    UserOrLocationListview.setItems(places);
                }
            } else if (newValue.equals(ProjektTab)){
                //something
            }
        });
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
        //clear the gridpane of projects
        CalendarGridPane.getChildren().removeIf(node -> node instanceof ProjektCell);

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
                int startWeek = getWeekNumber(projekt.getStartDate()) + 1;
                int endWeek = getWeekNumber(projekt.getEndDate()) + 1;
                int length = endWeek - startWeek + 1; //eksempel 17 - 19 = 2, så plus en for at få det til at passe.
                ProjektCell pcell = new ProjektCell(length, projekt.getColor(), projekt);
                pcell.setHeight(rowHeight);
                pcell.setWidth(columnWidth * length); // Adjust the width of the cell
                //make the cell red
                pcell.setFill(javafx.scene.paint.Color.RED);

                //set title on the project cells
                Label title = new Label(projekt.getTitel());
                title.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
                StackPane sp = new StackPane();
                sp.getChildren().addAll(pcell, title);

                if (reuseableRow.get() > 0){
                    CalendarGridPane.add(sp, startWeek, reuseableRow.get());
                    GridPane.setColumnSpan(sp, length); // Make the cell span multiple weeks
                    projektGrid.put(reuseableRow.get(), projekt);
                    reuseableRow.set(0);
                } else {
                    CalendarGridPane.add(sp, startWeek, noOfProjects);
                    GridPane.setColumnSpan(sp, length); // Make the cell span multiple weeks
                    projektGrid.put(noOfProjects, projekt);
                }
                noOfProjects++;
            }
        }
    }


    public int getCurrentWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }




    public void populateGridPane() {
        //fills the gridpane with empty cells and weeknumbers.
        for (int row = 0; row < calendarRows; row++) {
            for (int col = 1; col <= calendarColumns; col++) {
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
            createNewProjektCreatorList = FXCollections.observableArrayList();
            createNewProjektPersonList = FXCollections.observableArrayList();
            createNewProjektLokationList = FXCollections.observableArrayList();
            usersForNewProjekt = FXCollections.observableArrayList();
            placesForNewProjekt = FXCollections.observableArrayList();
            usersForNewProjekt.addAll(users);
            placesForNewProjekt.addAll(places);
            for (PersonListCell user : usersForNewProjekt) {
                if (user.getPerson().getEmail().equals(logic.getCurrentUser().getEmail())){
                    PersonListCell pcell = new PersonListCell(logic.getCurrentUser());
                    createNewProjektCreatorList.add(pcell);
                    CreateNewProjektCreatorListView.setItems(createNewProjektCreatorList);
                    usersForNewProjekt.remove(user);
                    UserOrLocationListview.setItems(usersForNewProjekt);
                    break;
                }
            }
        } else {
            CalendarTabPane.getSelectionModel().select(CreateNewProjektTab);
            if (UserToggleButton.isSelected()){
                UserOrLocationListview.setItems(placesForNewProjekt);
                UserOrLocationListview.setItems(usersForNewProjekt);
            } else {
                UserOrLocationListview.setItems(usersForNewProjekt);
                UserOrLocationListview.setItems(placesForNewProjekt);
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

                NewLocationDialogController controller = loader.getController();
                controller.setMainController(this);

                addLocation.show();
                addLocation.setOnHidden(e -> loadLokations());

            } catch (IOException e){
                System.out.println("Can't open new location window");
            }
        }
    }

    public void listviewSearchButtonPressed() { //TODO null pointer safety
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
        if (CreateNewProjektTab.isSelected()){
            UserOrLocationListview.setItems(usersForNewProjekt);
        } else {
            UserOrLocationListview.setItems(users);
        }
        AdminMenuNewUserOrLocationButton.setText("Ny Bruger");

        UserToggleButton.setSelected(true);
    }

    public void locationToggleButtonPressed(ActionEvent event) {
        if (CreateNewProjektTab.isSelected()){
            UserOrLocationListview.setItems(placesForNewProjekt);
        } else {
            UserOrLocationListview.setItems(places);
        }
        AdminMenuNewUserOrLocationButton.setText("Ny Lokation");

        LocationToggleButton.setSelected(true);
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
        places.clear();
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
        this.currentlySelectedProjekt = projekt;
        //check if the projekt tab is already open with the projekt based on the title. If it is, select the tab, if not, create a new tab.
        if(CalendarTabPane.getTabs().contains(ProjektTab)){
            if (ProjektTitelLabel.getText().equals(projekt.getTitel())){
                setProjektInformation(projekt);
                CalendarTabPane.getSelectionModel().select(ProjektTab);
            }
        } else {
            CalendarTabPane.getTabs().remove(ProjektTab);
            CalendarTabPane.getTabs().add(ProjektTab);
            setProjektInformation(projekt);
            CalendarTabPane.getSelectionModel().select(ProjektTab);
        }
        System.out.println(projekt.getProjectCreator());
        System.out.println(projekt.getParticipants());
    }

    public void setProjektInformation(Projekt projekt){
        ProjektTab.setText(projekt.getTitel());
        ObservableList<PersonListCell> tempUsers = FXCollections.observableArrayList();
        ObservableList<PersonListCell> tempCreators = FXCollections.observableArrayList();
        ObservableList<LokationListCell> tempPlaces = FXCollections.observableArrayList();

        projekt.getProjectCreator().forEach(person -> tempCreators.add(new PersonListCell(person)));
        projekt.getParticipants().forEach(person -> tempUsers.add(new PersonListCell(person)));
        tempPlaces.add(new LokationListCell(projekt.getLokation()));
        projektLokationerListview.setItems(tempPlaces);
        projektProjektejereListview.setItems(tempCreators);
        projektProjektdeltagereListview.setItems(tempUsers);
        ProjektTitelLabel.setText(projekt.getTitel());
        projektBeskrivelse.setText(projekt.getDescription());
        projektDato.setText("Startdato: " + projekt.getStartDate().toString() + " Slutdato: " + projekt.getEndDate().toString());
        projektMøder.setText(projekt.getAktiviteter());
        projektNoter.setText(projekt.getNotes());
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
        if (CreateNewProjectTitleTextField.getText().isEmpty() ||
                CreateNewProjektEndDatePicker.getValue() == null ||
                CreateNewProjektCreatorListView.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl i oprettelse af projekt");
            alert.setHeaderText("Projektet kunne ikke oprettes");
            alert.setContentText("Projektet skal have en projektejer, titel og en slutdato.");
            alert.showAndWait();
        } else {
            Date startDate;
            Date endDate;
            if (createProjektAktiviteterListview.getItems().isEmpty()) {
                startDate = new Date();
            } else {
                startDate = findEarliestDate(createProjektAktiviteterListview.getItems());
            }
            if (!createProjektAktiviteterListview.getItems().isEmpty()) {
                Date latestDate = findLatestDate(createProjektAktiviteterListview.getItems());
                LocalDate endDateLocalDate = CreateNewProjektEndDatePicker.getValue();
                Instant instant = endDateLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                endDate = Date.from(instant);

                if (latestDate.after(endDate)) {
                    endDate = latestDate;
                }
            } else {
                // If there are no planned activities, simply use the selected end date
                LocalDate endDateLocalDate = CreateNewProjektEndDatePicker.getValue();
                Instant instant = endDateLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                endDate = Date.from(instant);
            }
            Projekt nytProjekt = new Projekt(CreateNewProjectTitleTextField.getText(), startDate, endDate, UUID.randomUUID());

            if(!CreateNewProjektDescriptionTextArea.getText().isEmpty()){
                nytProjekt.setDescription(CreateNewProjektDescriptionTextArea.getText());
            } else {
                nytProjekt.setDescription("Ingen beskrivelse");
            }

            if(!CreateNewProjectNotesTextArea.getText().isEmpty()){
                nytProjekt.setNotes(CreateNewProjectNotesTextArea.getText());
            } else {
                nytProjekt.setNotes("Ingen noter");
            }

            if(!CreateNewProjektCreatorListView.getItems().isEmpty()){
                List<Person> creators = new ArrayList<>();
                for (PersonListCell creator : CreateNewProjektCreatorListView.getItems()) {
                    creators.add(creator.getPerson());
                }
                nytProjekt.setProjectCreator(creators);
            }
            if (!CreateNewProjektPersonListView.getItems().isEmpty()){
                List<Person> participants = new ArrayList<>();
                for (PersonListCell participant : CreateNewProjektPersonListView.getItems()) {
                    participants.add(participant.getPerson());
                }
                nytProjekt.setParticipants(participants);
            }
            if (!CreateNewProjectLokationListView.getItems().isEmpty()){
                nytProjekt.setLokation(CreateNewProjectLokationListView.getItems().getFirst().getLokation());
            }
            if (!createProjektAktiviteterListview.getItems().isEmpty()){
                nytProjekt.setProjektAktiviteter(createProjektAktiviteterListview.getItems());
            }
            logic.createProject(nytProjekt);
            projects.add(nytProjekt);
            fillCalendarWithProjects();
            CalendarTabPane.getSelectionModel().select(CalendarTab);
            CalendarTabPane.getTabs().remove(CreateNewProjektTab);
        }
    }

    public void createNewProjectAddActivityButtonPressed(ActionEvent actionEvent) {
        if (plannedActivityStartDatePicker.getValue() == null || plannedActivityEndDatePicker.getValue() == null || plannedActivityTitleTextField.getText().isEmpty() ||
                plannedActivityEndDatePicker.getValue().isBefore(plannedActivityStartDatePicker.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl i oprettelse af aktivitet");
            alert.setHeaderText("Aktiviteten kunne ikke oprettes");
            alert.setContentText("Aktiviteten skal have en titel og en start- og slutdato.");
            alert.showAndWait();
        } else {
            ProjektAktivitet aktivitet = new ProjektAktivitet(plannedActivityStartDatePicker.getValue(), plannedActivityEndDatePicker.getValue(), plannedActivityTitleTextField.getText());
            createProjektAktiviteterListview.getItems().add(aktivitet);
            createProjektAktiviteterListview.refresh();
            plannedActivityEndDatePicker.setValue(null);
            plannedActivityStartDatePicker.setValue(null);
            plannedActivityTitleTextField.clear();
        }
        //add to the nytprojektPlanlagteMøderFelt
    }

    public Date findEarliestDate(List<ProjektAktivitet> aktiviteter) {
        List<Date> dates = getDatesInActivities(aktiviteter);
        Date earliestDate = new Date(Long.MAX_VALUE);
        for (Date date : dates) {
            if (date.before(earliestDate)) {
                earliestDate = date;
            }
        }
        return earliestDate;
    }

    public Date findLatestDate(List<ProjektAktivitet> aktiviteter) {
        List<Date> dates = getDatesInActivities(aktiviteter);
        Date latestDate = new Date(Long.MIN_VALUE);
        for (Date date : dates) {
            if (date.after(latestDate)) {
                latestDate = date;
            }
        }
        return latestDate;
    }

    public List<Date> getDatesInActivities(List<ProjektAktivitet> activities){
        List<Date> dates = new ArrayList<>();
        for (ProjektAktivitet activity : activities) {
            dates.add(activity.getStartDatoAsUtilDate());
            dates.add(activity.getEndDatoAsUtilDate());
        }
        return dates;
    }


    public void editProjektButtonPressed(ActionEvent actionEvent) {
        if (CalendarTabPane.getTabs().contains(editProjectTab)){
            CalendarTabPane.getSelectionModel().select(editProjectTab);
        } else {
            CalendarTabPane.getTabs().add(editProjectTab);
            CalendarTabPane.getSelectionModel().select(editProjectTab);
        }
        setProjektForEditing(currentlySelectedProjekt);
    }

    public void setProjektForEditing(Projekt projekt){
        redigerBeskrivelseFelt.setText(projekt.getDescription());
        redigerNoterFelt.setText(projekt.getNotes());
        redigerPlanlagteMøderFelt.setText(projekt.getAktiviteter());
        redigerTitelFelt.setText(projekt.getTitel());
        Instant instant1 = Instant.ofEpochMilli(projekt.getStartDate().getTime());
        Instant instant2 = Instant.ofEpochMilli(projekt.getEndDate().getTime());
        LocalDate startDate = instant1.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = instant2.atZone(ZoneId.systemDefault()).toLocalDate();
        redigerArrangementDatoDatepicker.setValue(startDate);
        redigerSlutdatoDatepicker.setValue(endDate);
        redigerProjektejereListview.setItems(FXCollections.observableArrayList(projekt.getProjectCreator().stream().map(PersonListCell::new).collect(Collectors.toList())));
        redigerProjektdeltagereListview.setItems(FXCollections.observableArrayList(projekt.getParticipants().stream().map(PersonListCell::new).collect(Collectors.toList())));
        redigerLokationerListview.setItems(FXCollections.observableArrayList(new LokationListCell(projekt.getLokation())));
        /*
        public Button gemÆndringerKnap;
    public Button sletProjektKnap;
    public TextField RedigerTitelPåAktivitetFelt;
    public Button redigerNyAktivitetKnap;
    public DatePicker redigerSlutdatoDatepicker;
    public DatePicker RedigerStartdatoDatepicker;
    public DatePicker redigerArrangementDatoDatepicker;
    public TextField redigerTitelFelt;
    public ListView redigerProjektdeltagereListview;
    public ListView redigerProjektejereListview;
    public ListView redigerLokationerListview;
    @FXML
    private TextArea redigerBeskrivelseFelt;

    @FXML
    private TextArea redigerNoterFelt;

    @FXML
    private TextArea redigerPlanlagteMøderFelt;
         */
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
            LokationListCell droppedLokation = placesForNewProjekt.stream()
                    .filter(lokation -> lokation.getName().equals(lokationName))
                    .findFirst()
                    .orElse(null);

            // If the Lokation was found, add it to the ListView
            if (droppedLokation != null) {
                LokationListCell lCell = new LokationListCell(droppedLokation.getLokation());
                createNewProjektLokationList.add(lCell);
                CreateNewProjectLokationListView.setItems(createNewProjektLokationList);
                placesForNewProjekt.remove(droppedLokation);
                UserOrLocationListview.setItems(placesForNewProjekt);
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
            PersonListCell droppedPerson = usersForNewProjekt.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            // If the Person was found, add it to the ListView
            if (droppedPerson != null) {
                PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                createNewProjektCreatorList.add(pcell);
                CreateNewProjektCreatorListView.setItems(createNewProjektCreatorList);
                usersForNewProjekt.remove(droppedPerson);
                UserOrLocationListview.setItems(usersForNewProjekt);
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
            PersonListCell droppedPerson = usersForNewProjekt.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            // If the Person was found, add it to the ListView
            if (droppedPerson != null) {
                PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                createNewProjektPersonList.add(pcell);
                CreateNewProjektPersonListView.setItems(createNewProjektPersonList);
                usersForNewProjekt.remove(droppedPerson);
                UserOrLocationListview.setItems(usersForNewProjekt);
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
            //System.out.println("Dragged over to participant listview");
        }
        dragEvent.consume();
    }



    public void userLokationListViewDragDetected(MouseEvent mouseEvent) {

    }

    public void userLokationListViewDragDone(DragEvent dragEvent) {

    }
}