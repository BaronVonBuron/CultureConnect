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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CultureConnectController {
    private final int calendarColumns = 52;
    private final int calendarRows = 35;//skal sættes af antallet af projekter.
    private final int columnWidth = 60;
    private final int rowHeight = 45;
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
    public Button RedigerProjektRedigerAktivitetKnap;
    public Button RedigerProjektSletAktivitetKnap;
    public ListView<ProjektAktivitet> EditProjektAktiviteterListview;
    public Button NytProjektRedigerAktivitetKnap;
    public Button NytProjektSletAktivitetKnap;
    public ColorPicker ProjektColorpicker;
    public TextField projektSøgefelt;
    public Button projektSøgKnap;
    public ChoiceBox filterChooser;
    public Button tilbageTilIDageKnap;
    public Button KalenderVenstreKnap;
    public Button KalenderHøjreKnap;
    public Label KalenderLabel;
    public ListView<ProjektAktivitet> ProjektAktiviteterListview;
    public ListView<PersonListCell> CreateNewProjektCreatorListView;
    public ListView<LokationListCell> CreateNewProjectLokationListView;
    public Label ProjektTitelLabel;
    public DatePicker plannedActivityStartDatePicker;
    public DatePicker plannedActivityEndDatePicker;
    //Create new projekt tab slut////////////////////////////////////////////
    public TextField plannedActivityTitleTextField;
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
    Tooltip projektTooltip = new Tooltip();
    ObservableList<PersonListCell> users = FXCollections.observableArrayList();
    //Edit projekt tab slut////////////////////////////////////////////
    ObservableList<PersonListCell> usersForNewProjekt;
    ObservableList<LokationListCell> places = FXCollections.observableArrayList();
    ObservableList<LokationListCell> placesForNewProjekt;
    List<Projekt> projects = new ArrayList<>();
    //SimpleIntegerProperty count = new SimpleIntegerProperty();
    int rowHeightTextareas = 10;
    private ObservableList<PersonListCell> createNewProjektPersonList;
    private ObservableList<PersonListCell> createNewProjektCreatorList;
    private ObservableList<LokationListCell> createNewProjektLokationList;
    private ObservableList<PersonListCell> redigerProjektPersonList;
    private ObservableList<PersonListCell> redigerProjektCreatorList;
    private ObservableList<LokationListCell> redigerProjektLokationList;
    @FXML
    private TextArea redigerBeskrivelseFelt;
    @FXML
    private TextArea redigerNoterFelt;
    @FXML
    private TextArea redigerPlanlagteMøderFelt;
    private Projekt currentlySelectedProjekt;
    private Logic logic;

    public void initialize() {
        this.logic = Logic.getInstance();
        this.logic.setMainWindowController(this);
        startSequence();
        loginSequence();
        colorValidation();
        populateFilterChooser();

        autoExpandingTextareas(CreateNewProjektDescriptionTextArea, CreateNewProjectNotesTextArea,
                redigerBeskrivelseFelt, redigerNoterFelt
        );

        //eventlistener, to see which tab is selected
        CalendarTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(CreateNewProjektTab)) {
                if (UserToggleButton.isSelected()) {
                    UserOrLocationListview.setItems(placesForNewProjekt);
                    UserOrLocationListview.setItems(usersForNewProjekt);
                } else {
                    UserOrLocationListview.setItems(usersForNewProjekt);
                    UserOrLocationListview.setItems(placesForNewProjekt);
                }
            } else if (newValue.equals(CalendarTab)) {

                if (UserToggleButton.isSelected()) {
                    UserOrLocationListview.setItems(users);
                } else {
                    UserOrLocationListview.setItems(places);
                }
            } else if (newValue.equals(editProjectTab)) {
                if (UserToggleButton.isSelected()) {
                    UserOrLocationListview.setItems(redigerProjektPersonList);
                } else {
                    UserOrLocationListview.setItems(redigerProjektLokationList);
                }
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
                if (newVal.intValue() > rowHeightTextareas) {
                    count.set(count.get() + newVal.intValue());
                }
            });
        }
    }

    public void startSequence() {
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

    public void loginSequence() {
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


    public void fillCalendarWithProjects() {
        //clear the gridpane of projektCells
        CalendarGridPane.getChildren().removeIf(node -> node instanceof StackPane);
        this.projects = logic.getProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects to show");
        } else {
            int noOfProjects = 1;
            AtomicInteger reuseableRow = new AtomicInteger();
            HashMap<Integer, Projekt> projektGrid = new HashMap<>();
            for (Projekt projekt : projects) {
                if (noOfProjects > 15) {
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
                pcell.setHeight(rowHeight-10);
                pcell.setWidth(columnWidth * length);
                if (projekt.getEndDate().before(new Date())) {
                    pcell.setFill(Paint.valueOf("#b6b2b2"));
                } else {
                    pcell.setFill(Paint.valueOf(projekt.getColor()));
                }
                //set title on the project cells
                Label title = new Label(projekt.getTitel());
                title.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
                title.setMouseTransparent(true);
                StackPane sp = new StackPane();
                sp.getChildren().addAll(pcell, title);

                if (reuseableRow.get() > 0) {
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
                Label label = new Label();
                if (row == 0) {
                    label.setText("  Uge " + col+ "  ");
                    label.setStyle("-fx-underline: true");
                    label.setStyle("-fx-border-color: grey; -fx-border-width: 1;");
                    label.setMinWidth(columnWidth);
                    label.setMinHeight(rowHeight);
                    label.setAlignment(javafx.geometry.Pos.CENTER);
                    CalendarGridPane.setAlignment(javafx.geometry.Pos.CENTER);
                    CalendarGridPane.add(label, col, row);
                }
                CalendarCell cell = new CalendarCell(); // Create a new Cell instance
                cell.setWidth(columnWidth); // Set the width of the cell
                cell.setHeight(rowHeight); // Set the height of the cell
                if (col == getCurrentWeekNumber()){
                    int currentWeek = getCurrentWeekNumber();

                    Rectangle highlight = new Rectangle();
                    highlight.setWidth(columnWidth);
                    highlight.setHeight(rowHeight * (calendarRows - 1)); // Adjust the height to leave space for the label
                    highlight.setFill(Color.LIGHTGREY); // Set the color you want for the current week
                    highlight.setOpacity(0.2);

                    Label label2 = new Label(" Uge " + currentWeek);
                    label2.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-underline: true");
                    label2.setStyle("-fx-border-color: grey; -fx-border-width: 1;");
                    label2.setMinWidth(columnWidth);
                    label2.setMinHeight(rowHeight);

                    // Create a VBox and add the label and highlight to it
                    VBox vbox = new VBox();
                    vbox.setStyle("-fx-background-color: lightgrey");
                    vbox.setPrefHeight(rowHeight);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.getChildren().addAll(label2, highlight);

                    // Create a StackPane and add the VBox to it
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(vbox);

                    // Add the StackPane to the GridPane at the current week column and span it across all rows
                    CalendarGridPane.add(stackPane, currentWeek, 0, 1, calendarRows);

                    // Bring the label to the front
                    label.toFront();

                }
                CalendarGridPane.add(cell, col, row); // Add the cell to the GridPane at the specified column and row
            }
        }
        CalendarGridPane.setGridLinesVisible(false);
        customizeGridLines();
    }

    public void customizeGridLines() {
        for (int row = 0; row < calendarRows; row++) {
            for (int col = 0; col <= calendarColumns; col++) {
                // Add vertical lines for all rows
                Rectangle verticalLine = createLine(2, rowHeight, Color.GREY);
                CalendarGridPane.add(verticalLine, col, row);
            }
        }
    }

    private Rectangle createLine(double width, double height, Color color) {
        Rectangle line = new Rectangle(width, height);
        line.setFill(color);
        return line;
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
        if (!CalendarTabPane.getTabs().contains(CreateNewProjektTab)) {
            CalendarTabPane.getTabs().add(CreateNewProjektTab);
            CalendarTabPane.getSelectionModel().select(CreateNewProjektTab);
            createNewProjektCreatorList = FXCollections.observableArrayList();
            createNewProjektPersonList = FXCollections.observableArrayList();
            createNewProjektLokationList = FXCollections.observableArrayList();
            usersForNewProjekt = FXCollections.observableArrayList();
            placesForNewProjekt = FXCollections.observableArrayList();
            CreateNewProjektEndDatePicker.setValue(null);
            CreateNewProjectTitleTextField.clear();
            CreateNewProjektDescriptionTextArea.clear();
            CreateNewProjectNotesTextArea.clear();
            createProjektAktiviteterListview.getItems().clear();
            plannedActivityStartDatePicker.setValue(null);
            plannedActivityEndDatePicker.setValue(null);
            plannedActivityTitleTextField.clear();
            usersForNewProjekt.addAll(users);
            placesForNewProjekt.addAll(places);
            for (PersonListCell user : usersForNewProjekt) {
                if (user.getPerson().getEmail().equals(logic.getCurrentUser().getEmail())) {
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
            if (UserToggleButton.isSelected()) {
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

                NewUserDialogController controller = loader.getController();
                controller.setMainController(this);

                addUser.show();
                addUser.setOnHidden(e -> updateList());

            } catch (IOException e) {
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
                addLocation.setOnHidden(e -> updateList());

            } catch (IOException e) {
                System.out.println("Can't open new location window");
            }
        }
    }

    public void listviewSearchButtonPressed() { //TODO null pointer safety
        if (ListviewSearchTextField != null && UserToggleButton.isSelected()) {
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
        if (CreateNewProjektTab.isSelected()) {
            UserOrLocationListview.setItems(usersForNewProjekt);
        } else if (CalendarTab.isSelected()){
            UserOrLocationListview.setItems(users);
        } else if (editProjectTab.isSelected()) {
            UserOrLocationListview.setItems(redigerProjektPersonList);
        }
        AdminMenuNewUserOrLocationButton.setText("Ny Bruger");

        UserToggleButton.setSelected(true);
    }

    public void locationToggleButtonPressed(ActionEvent event) {
        if (CreateNewProjektTab.isSelected()) {
            UserOrLocationListview.setItems(placesForNewProjekt);
        } else if (CalendarTab.isSelected()) {
            UserOrLocationListview.setItems(places);
        } else if (editProjectTab.isSelected()) {
            UserOrLocationListview.setItems(redigerProjektLokationList);
        }
        AdminMenuNewUserOrLocationButton.setText("Ny Lokation");

        LocationToggleButton.setSelected(true);
    }

    public void loadUsers() {
        users.clear();
        List<Person> persons = logic.getPersons();
        List<PersonListCell> cells = new ArrayList<>();
        for (Person person : persons) {
            cells.add(new PersonListCell(person));
        }
        users.addAll(cells);
        //UserOrLocationListview.getItems().clear();
        UserOrLocationListview.setItems(users);
    }

    public void loadLokations() {
        places.clear();
        List<Lokation> lokations = logic.getLocations();
        List<LokationListCell> cells = new ArrayList<>();
        for (Lokation lokation : lokations) {
            cells.add(new LokationListCell(lokation));
        }
        places.clear();
        places.addAll(cells);
        //UserOrLocationListview.getItems().clear();
        UserOrLocationListview.setItems(places);
    }

    public void SearchFieldKeyPressed(KeyEvent keyEvent) {
        listviewSearchButtonPressed();
    }

    public void GridPaneClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof ProjektCell cell && !this.projektTooltip.isShowing()) {
            openSmallProjektDialogWindow(cell.getProjekt(), mouseEvent);
        } else {
            this.projektTooltip.hide();
        }
    }

    public void openSmallProjektDialogWindow(Projekt projekt, MouseEvent mouseEvent) {
        VBox dialogVBox = new VBox();
        Label titleLabel = new Label(projekt.getTitel());
        titleLabel.getStyleClass().add("ProjectTooltipTitle");
        Label descriptionLabel = new Label(projekt.getDescription());
        descriptionLabel.setWrapText(true);
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

    public void openProjektTab(Projekt projekt) {
        this.currentlySelectedProjekt = projekt;
        //check if the projekt tab is already open with the projekt based on the title. If it is, select the tab, if not, create a new tab.
        if (CalendarTabPane.getTabs().contains(ProjektTab)) {
            if (ProjektTitelLabel.getText().equals(projekt.getTitel())) {
                setProjektInformation(projekt);
                CalendarTabPane.getSelectionModel().select(ProjektTab);
            }
        } else {
            CalendarTabPane.getTabs().remove(ProjektTab);
            CalendarTabPane.getTabs().add(ProjektTab);
            setProjektInformation(projekt);
            CalendarTabPane.getSelectionModel().select(ProjektTab);
        }
    }

    public void setProjektInformation(Projekt projekt) {
        ProjektTab.setText(projekt.getTitel());
        ObservableList<PersonListCell> tempUsers = FXCollections.observableArrayList();
        ObservableList<PersonListCell> tempCreators = FXCollections.observableArrayList();
        ObservableList<LokationListCell> tempPlaces = FXCollections.observableArrayList();
        ProjektAktiviteterListview.setItems(FXCollections.observableArrayList(projekt.getProjektAktiviteter()));
        projekt.getProjectCreator().forEach(person -> tempCreators.add(new PersonListCell(person)));
        projekt.getParticipants().forEach(person -> tempUsers.add(new PersonListCell(person)));
        tempPlaces.add(new LokationListCell(projekt.getLokation()));
        projektLokationerListview.setItems(tempPlaces);
        projektProjektejereListview.setItems(tempCreators);
        projektProjektdeltagereListview.setItems(tempUsers);
        ProjektTitelLabel.setText(projekt.getTitel());
        projektBeskrivelse.setText(projekt.getDescription());
        projektDato.setText(projekt.getArrangementDato().toString().substring(0, 10));
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


        if (alert.getResult() == buttonType) {
            CreateNewProjectLokationListView.getItems().clear();
            CreateNewProjektCreatorListView.getItems().clear();
            CreateNewProjektPersonListView.getItems().clear();
            CalendarTabPane.getSelectionModel().select(CalendarTab);
            CalendarTabPane.getTabs().remove(CreateNewProjektTab);
        }
    }

    public void createProjektButtonPressed(ActionEvent actionEvent) {
        if (CreateNewProjectTitleTextField.getText().isEmpty() ||
                CreateNewProjektEndDatePicker.getValue() == null ||
                CreateNewProjektCreatorListView.getItems().isEmpty() || CreateNewProjectLokationListView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl i oprettelse af projekt");
            alert.setHeaderText("Projektet kunne ikke oprettes");
            alert.setContentText("Projektet skal have en projektejer, lokation, titel og en slutdato.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CultureConnectCSS.css").toExternalForm());
            dialogPane.getStyleClass().add("Alerts");
            alert.showAndWait();
        } else {
            Date startDate;
            Date endDate;
            Date arrangementDate = Date.from(Instant.from(CreateNewProjektEndDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
            Projekt nytProjekt = new Projekt(CreateNewProjectTitleTextField.getText(), startDate, arrangementDate, endDate, UUID.randomUUID());

            if (!CreateNewProjektDescriptionTextArea.getText().isEmpty()) {
                nytProjekt.setDescription(CreateNewProjektDescriptionTextArea.getText());
            } else {
                nytProjekt.setDescription("Ingen beskrivelse");
            }

            if (!CreateNewProjectNotesTextArea.getText().isEmpty()) {
                nytProjekt.setNotes(CreateNewProjectNotesTextArea.getText());
            } else {
                nytProjekt.setNotes("Ingen noter");
            }

            if (!CreateNewProjektCreatorListView.getItems().isEmpty()) {
                List<Person> creators = new ArrayList<>();
                for (PersonListCell creator : CreateNewProjektCreatorListView.getItems()) {
                    creators.add(creator.getPerson());
                }
                nytProjekt.setProjectCreator(creators);
            }
            if (!CreateNewProjektPersonListView.getItems().isEmpty()) {
                List<Person> participants = new ArrayList<>();
                for (PersonListCell participant : CreateNewProjektPersonListView.getItems()) {
                    participants.add(participant.getPerson());
                }
                nytProjekt.setParticipants(participants);
            }
            if (!CreateNewProjectLokationListView.getItems().isEmpty()) {
                nytProjekt.setLokation(CreateNewProjectLokationListView.getItems().getFirst().getLokation());
            }
            if (!createProjektAktiviteterListview.getItems().isEmpty()) {
                nytProjekt.setProjektAktiviteter(createProjektAktiviteterListview.getItems());
            }
            if (ProjektColorpicker.getValue() != null) {
                nytProjekt.setColor(ProjektColorpicker.getValue().toString());
            } else {
                nytProjekt.setColor("ffffff");
            }
            logic.createProject(nytProjekt);
            fillCalendarWithProjects();
            CreateNewProjectLokationListView.getItems().clear();
            CreateNewProjektCreatorListView.getItems().clear();
            CreateNewProjektPersonListView.getItems().clear();
            CalendarTabPane.getSelectionModel().select(CalendarTab);
            CalendarTabPane.getTabs().remove(CreateNewProjektTab);
        }
    }

    public void createNewProjectAddActivityButtonPressed(ActionEvent actionEvent) {
        if (plannedActivityStartDatePicker.getValue() == null || plannedActivityEndDatePicker.getValue() == null || plannedActivityTitleTextField.getText().isEmpty() ||
                plannedActivityEndDatePicker.getValue().isBefore(plannedActivityStartDatePicker.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl i oprettelse af aktivitet");
            alert.setHeaderText("Aktiviteten kunne ikke oprettes");
            alert.setContentText("Aktiviteten skal have en titel og en start- og slutdato.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CultureConnectCSS.css").toExternalForm());
            dialogPane.getStyleClass().add("Alerts");
            alert.showAndWait();
        } else {
            ProjektAktivitet aktivitet = new ProjektAktivitet(plannedActivityStartDatePicker.getValue(), plannedActivityEndDatePicker.getValue(), plannedActivityTitleTextField.getText());
            createProjektAktiviteterListview.getItems().add(aktivitet);
            createProjektAktiviteterListview.refresh();
            plannedActivityEndDatePicker.setValue(null);
            plannedActivityStartDatePicker.setValue(null);
            plannedActivityTitleTextField.clear();
        }
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

    public List<Date> getDatesInActivities(List<ProjektAktivitet> activities) {
        List<Date> dates = new ArrayList<>();
        for (ProjektAktivitet activity : activities) {
            dates.add(activity.getStartDatoAsUtilDate());
            dates.add(activity.getEndDatoAsUtilDate());
        }
        return dates;
    }


    public void editProjektButtonPressed(ActionEvent actionEvent) {
        if (CalendarTabPane.getTabs().contains(editProjectTab)) {
            CalendarTabPane.getSelectionModel().select(editProjectTab);
            CalendarTabPane.getTabs().remove(ProjektTab);
        } else {
            CalendarTabPane.getTabs().add(editProjectTab);
            CalendarTabPane.getSelectionModel().select(editProjectTab);
            CalendarTabPane.getTabs().remove(ProjektTab);
        }
        setProjektForEditing(currentlySelectedProjekt);
    }

    public void setProjektForEditing(Projekt projekt) {
        redigerBeskrivelseFelt.setText(projekt.getDescription());
        redigerNoterFelt.setText(projekt.getNotes());
        EditProjektAktiviteterListview.setItems(FXCollections.observableArrayList(projekt.getProjektAktiviteter()));
        redigerTitelFelt.setText(projekt.getTitel());
        Instant instant2 = Instant.ofEpochMilli(projekt.getArrangementDato().getTime());
        LocalDate arrangementDato = instant2.atZone(ZoneId.systemDefault()).toLocalDate();
        redigerArrangementDatoDatepicker.setValue(arrangementDato);
        redigerProjektejereListview.setItems(FXCollections.observableArrayList(projekt.getProjectCreator().stream().map(PersonListCell::new).collect(Collectors.toList())));
        redigerProjektdeltagereListview.setItems(FXCollections.observableArrayList(projekt.getParticipants().stream().map(PersonListCell::new).collect(Collectors.toList())));
        redigerLokationerListview.setItems(FXCollections.observableArrayList(new LokationListCell(projekt.getLokation())));

        redigerProjektPersonList = FXCollections.observableArrayList();
        redigerProjektLokationList = FXCollections.observableArrayList();
        //add users to the observableList and filter out the ones that are already in the projekt, by their names, as the personListCell is not the same object.
        for (int i = 0; i < users.size(); i++) {
            boolean isCreator = false;
            boolean isParticipant = false;
            for (Person creator : projekt.getProjectCreator()) {
                if (users.get(i).getName().equals(creator.getName())) {
                    isCreator = true;
                    break;
                }
            }
            for (Person participant : projekt.getParticipants()) {
                if (users.get(i).getName().equals(participant.getName())) {
                    isParticipant = true;
                    break;
                }
            }
            if (!isCreator && !isParticipant) {
                redigerProjektPersonList.add(users.get(i));
            }
        }
        //add locations to the observableList and filter out the ones that are already in the projekt, by their names, as the lokationListCell is not the same object.
        for (int i = 0; i < places.size(); i++) {
            if (!places.get(i).getName().equals(projekt.getLokation().getName())) {
                redigerProjektLokationList.add(places.get(i));
            }
        }

        if (UserToggleButton.isSelected()) {
            UserOrLocationListview.setItems(redigerProjektLokationList);
            UserOrLocationListview.setItems(redigerProjektPersonList);
        } else {
            UserOrLocationListview.setItems(redigerProjektPersonList);
            UserOrLocationListview.setItems(redigerProjektLokationList);
        }
    }

    public void createNewProjektEndDatePickerPressed(ActionEvent actionEvent) {

    }

    public void redigerNyAktivitetKnapPressed(ActionEvent actionEvent) {
        if (RedigerStartdatoDatepicker.getValue() != null && redigerSlutdatoDatepicker.getValue() != null && RedigerTitelPåAktivitetFelt.getText() != null) {
            ProjektAktivitet aktivitet = new ProjektAktivitet(RedigerStartdatoDatepicker.getValue(), redigerSlutdatoDatepicker.getValue(), RedigerTitelPåAktivitetFelt.getText());
            EditProjektAktiviteterListview.getItems().add(aktivitet);
            EditProjektAktiviteterListview.refresh();
            RedigerStartdatoDatepicker.setValue(null);
            redigerSlutdatoDatepicker.setValue(null);
            RedigerTitelPåAktivitetFelt.clear();
        }
    }

    public void sletProjektKnapPressed(ActionEvent actionEvent) {
        //alert to make sure, else delete the projekt
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Slet projekt");
        alert.setHeaderText("Er du sikker på at du vil slette projektet?");
        alert.setContentText("Projektet kan ikke gendannes, hvis det slettes.");
        alert.getButtonTypes().clear();
        ButtonType buttonType = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonType1 = new ButtonType("Nej", ButtonBar.ButtonData.CANCEL_CLOSE);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
        alert.getButtonTypes().addAll(buttonType, buttonType1);
        alert.showAndWait();
        if (alert.getResult() == buttonType) {
            logic.deleteProjekt(currentlySelectedProjekt);
            fillCalendarWithProjects();
            CalendarTabPane.getSelectionModel().select(CalendarTab);
            CalendarTabPane.getTabs().remove(editProjectTab);
        }
    }

    public void gemÆndringerKnapPressed(ActionEvent actionEvent) {
        if (redigerTitelFelt.getText().isEmpty() || redigerArrangementDatoDatepicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fejl i redigering af projekt");
            alert.setHeaderText("Projektet kunne ikke redigeres");
            alert.setContentText("Projektet skal have en titel og en slutdato.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CultureConnectCSS.css").toExternalForm());
            dialogPane.getStyleClass().add("Alerts");
            alert.showAndWait();
        } else {
            Projekt projekt = currentlySelectedProjekt;
            projekt.setTitel(redigerTitelFelt.getText());
            projekt.setDescription(redigerBeskrivelseFelt.getText());
            projekt.setNotes(redigerNoterFelt.getText());
            projekt.setArrangementDato(Date.from(Instant.from(redigerArrangementDatoDatepicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));

            if (!redigerProjektejereListview.getItems().isEmpty()) {
                List<Person> creators = new ArrayList<>();
                for (PersonListCell creator : redigerProjektejereListview.getItems()) {
                    creators.add(creator.getPerson());
                }
                projekt.setProjectCreator(creators);
            }
            if (!redigerProjektdeltagereListview.getItems().isEmpty()) {
                List<Person> participants = new ArrayList<>();
                for (PersonListCell participant : redigerProjektdeltagereListview.getItems()) {
                    participants.add(participant.getPerson());
                }
                projekt.setParticipants(participants);
            }
            if (!redigerLokationerListview.getItems().isEmpty()) {
                projekt.setLokation(redigerLokationerListview.getItems().getFirst().getLokation());
            }
            if (!EditProjektAktiviteterListview.getItems().isEmpty()) {
                projekt.setProjektAktiviteter(EditProjektAktiviteterListview.getItems());
            }
            logic.updateProjekt(projekt);
            fillCalendarWithProjects();
            CalendarTabPane.getSelectionModel().select(CalendarTab);
            CalendarTabPane.getTabs().remove(editProjectTab);
        }
        //TODO save the changes to the projekt
    }

    public void createProjektLokationDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        if (db.hasString()) {
            String lokationName = db.getString();
            LokationListCell droppedLokation = placesForNewProjekt.stream()
                    .filter(lokation -> lokation.getName().equals(lokationName))
                    .findFirst()
                    .orElse(null);
            if (droppedLokation != null) {
                LokationListCell lCell = new LokationListCell(droppedLokation.getLokation());
                createNewProjektLokationList.add(lCell);
                CreateNewProjectLokationListView.setItems(createNewProjektLokationList);
                placesForNewProjekt.remove(droppedLokation);
                UserOrLocationListview.setItems(placesForNewProjekt);
            }
            dragEvent.setDropCompleted(true);
        } else {
            dragEvent.setDropCompleted(false);
        }
        dragEvent.consume();
    }

    public void createProjektLokationDraggedOver(DragEvent dragEvent) {
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
        }
        dragEvent.consume();
    }

    public void createProjektParticipantPersonDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        if (db.hasString()) {
            String personName = db.getString();
            PersonListCell droppedPerson = usersForNewProjekt.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            if (droppedPerson != null) {
                PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                createNewProjektPersonList.add(pcell);
                CreateNewProjektPersonListView.setItems(createNewProjektPersonList);
                usersForNewProjekt.remove(droppedPerson);
                UserOrLocationListview.setItems(usersForNewProjekt);
            }
            dragEvent.setDropCompleted(true);
        } else {
            dragEvent.setDropCompleted(false);
        }
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


    public void userLokationListViewDragDropped(DragEvent dragEvent) {
        if(editProjectTab.isSelected()){
            if (UserToggleButton.isSelected()) {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasString()) {
                    String personName = db.getString();
                    System.out.println(personName + " 1");
                    PersonListCell droppedPerson = redigerProjektdeltagereListview.getItems().stream()
                            .filter(person -> person.getName().equals(personName))
                            .findFirst()
                            .orElse(redigerProjektejereListview.getItems().stream()
                                    .filter(person -> person.getName().equals(personName))
                                    .findFirst()
                                    .orElse(null));
                    System.out.println(droppedPerson.getName() + " 2");
                    boolean isCreator = false;
                    for (PersonListCell creator : redigerProjektejereListview.getItems()) {
                        if (creator.getName().equals(personName)) {
                            isCreator = true;
                            break;

                        }
                    }
                    if (droppedPerson != null && !isCreator) {
                        PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());

                        for (PersonListCell personcell : redigerProjektdeltagereListview.getItems()) {
                            if (personcell.getName().equals(droppedPerson.getName())) {
                                redigerProjektdeltagereListview.getItems().remove(personcell);
                                redigerProjektdeltagereListview.refresh();
                                break;
                            }
                        }
                        redigerProjektPersonList.add(pcell);
                        UserOrLocationListview.setItems(redigerProjektPersonList);
                    } else if (droppedPerson != null && isCreator) {
                        PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                        for (PersonListCell personCell : redigerProjektejereListview.getItems()) {
                            if (personCell.getName().equals(droppedPerson.getName())) {
                                redigerProjektejereListview.getItems().remove(personCell);
                                redigerProjektejereListview.refresh();
                                break;
                            }
                        }
                        redigerProjektPersonList.add(pcell);
                        UserOrLocationListview.setItems(redigerProjektPersonList);
                    }
                    dragEvent.setDropCompleted(true);
                } else {
                    dragEvent.setDropCompleted(false);
                }
                dragEvent.consume();
            } else {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasString()) {
                    String lokationName = db.getString();
                    LokationListCell droppedLokation = redigerLokationerListview.getItems().stream()
                            .filter(lokation -> lokation.getName().equals(lokationName))
                            .findFirst()
                            .orElse(null);
                    if (droppedLokation != null) {
                        LokationListCell lCell = new LokationListCell(droppedLokation.getLokation());
                        for (LokationListCell lokationcell : redigerLokationerListview.getItems()) {
                            if (lokationcell.getName().equals(droppedLokation.getName())) {
                                redigerLokationerListview.getItems().remove(lokationcell);
                                redigerLokationerListview.refresh();
                                break;
                            }
                        }
                        redigerProjektLokationList.add(lCell);
                        UserOrLocationListview.setItems(redigerProjektLokationList);
                    }
                    dragEvent.setDropCompleted(true);
                } else {
                    dragEvent.setDropCompleted(false);
                }
                dragEvent.consume();
            }
        } else if (CreateNewProjektTab.isSelected()) {
            if (UserToggleButton.isSelected()) {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasString()) {
                    String personName = db.getString();
                    System.out.println(personName + " 1");
                    PersonListCell droppedPerson = createNewProjektPersonList.stream()
                            .filter(person -> person.getName().equals(personName))
                            .findFirst()
                            .orElse(createNewProjektCreatorList.stream()
                                    .filter(person -> person.getName().equals(personName))
                                    .findFirst()
                                    .orElse(null));
                    System.out.println(droppedPerson.getName() + " 2");
                    boolean isCreator = false;
                    for (PersonListCell creator : createNewProjektCreatorList) {
                        if (creator.getName().equals(personName)) {
                            isCreator = true;
                            break;

                        }
                    }
                    if (droppedPerson != null && !isCreator) {
                        PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());

                        for (PersonListCell personcell : createNewProjektPersonList) {
                            if (personcell.getName().equals(droppedPerson.getName())) {
                                createNewProjektPersonList.remove(personcell);
                                CreateNewProjektPersonListView.setItems(createNewProjektPersonList);
                                break;
                            }
                        }
                        usersForNewProjekt.add(pcell);
                        UserOrLocationListview.setItems(usersForNewProjekt);
                    } else if (droppedPerson != null && isCreator) {
                        PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                        for (PersonListCell personCell : createNewProjektCreatorList) {
                            if (personCell.getName().equals(droppedPerson.getName())) {
                                createNewProjektCreatorList.remove(personCell);
                                CreateNewProjektCreatorListView.setItems(createNewProjektCreatorList);
                                break;
                            }
                        }
                        usersForNewProjekt.add(pcell);
                        UserOrLocationListview.setItems(usersForNewProjekt);
                    }
                    dragEvent.setDropCompleted(true);
                } else {
                    dragEvent.setDropCompleted(false);
                }
                dragEvent.consume();
            } else {
                Dragboard db = dragEvent.getDragboard();
                if (db.hasString()) {
                    String lokationName = db.getString();
                    LokationListCell droppedLokation = createNewProjektLokationList.stream()
                            .filter(lokation -> lokation.getName().equals(lokationName))
                            .findFirst()
                            .orElse(null);
                    if (droppedLokation != null) {
                        LokationListCell lCell = new LokationListCell(droppedLokation.getLokation());
                        for (LokationListCell lokationcell : createNewProjektLokationList) {
                            if (lokationcell.getName().equals(droppedLokation.getName())) {
                                createNewProjektLokationList.remove(lokationcell);
                                CreateNewProjectLokationListView.setItems(createNewProjektLokationList);
                                break;
                            }
                        }
                        placesForNewProjekt.add(lCell);
                        UserOrLocationListview.setItems(placesForNewProjekt);
                    }
                    dragEvent.setDropCompleted(true);
                } else {
                    dragEvent.setDropCompleted(false);
                }
            }
        }
    }


    public void updateList() {
        UserOrLocationListview.getItems().clear();
        if (UserToggleButton.isSelected()) {
            loadUsers();
        } else {
            loadLokations();
        }
    }

    public void RedigerProjektSletAktivitetKnapPressed(ActionEvent actionEvent) {
        if (EditProjektAktiviteterListview.getSelectionModel().getSelectedItem() != null) {
            EditProjektAktiviteterListview.getItems().remove(EditProjektAktiviteterListview.getSelectionModel().getSelectedItem());
        }
    }

    public void RedigerProjektRedigerAktivitetKnapPressed(ActionEvent actionEvent) {
        if (EditProjektAktiviteterListview.getSelectionModel().getSelectedItem() != null) {
            ProjektAktivitet aktivitet = EditProjektAktiviteterListview.getSelectionModel().getSelectedItem();
            RedigerStartdatoDatepicker.setValue(aktivitet.getStartDato());
            redigerSlutdatoDatepicker.setValue(aktivitet.getSlutDato());
            RedigerTitelPåAktivitetFelt.setText(aktivitet.getAktivitet());
            EditProjektAktiviteterListview.getItems().remove(aktivitet);
        }
    }

    public void NytProjektSletAktivitetKnapPressed(ActionEvent actionEvent) {
        if (createProjektAktiviteterListview.getSelectionModel().getSelectedItem() != null) {
            createProjektAktiviteterListview.getItems().remove(createProjektAktiviteterListview.getSelectionModel().getSelectedItem());
        }
    }

    public void NytProjektRedigerAktivitetKnapPressed(ActionEvent actionEvent) {
        if (createProjektAktiviteterListview.getSelectionModel().getSelectedItem() != null) {
            ProjektAktivitet aktivitet = createProjektAktiviteterListview.getSelectionModel().getSelectedItem();
            plannedActivityStartDatePicker.setValue(aktivitet.getStartDato());
            plannedActivityEndDatePicker.setValue(aktivitet.getSlutDato());
            plannedActivityTitleTextField.setText(aktivitet.getAktivitet());
            createProjektAktiviteterListview.getItems().remove(aktivitet);
        }
    }

    public void projektSøgefeltKeyPressed(KeyEvent keyEvent) {
        projektSøgefelt.textProperty().addListener((observable, oldValue, newValue) ->
                fieldFilterProjects(newValue));
    }

    public void projektSøgKnapPressed(ActionEvent actionEvent) {
        projektSøgefelt.textProperty().addListener((observable, oldValue, newValue) -> {
            fieldFilterProjects(newValue);
        });
    }

    public void tilbageTilIDageKnapPressed(ActionEvent actionEvent) {
        CalendarScrollPane.setHvalue(getCurrentWeekNumber() / 52.0);
    }

    public void KalenderVenstreKnapPressed(ActionEvent actionEvent) {
    }

    public void KalenderHøjreKnapPressed(ActionEvent actionEvent) {
    }


    public void redigerProjektdeltagereListviewDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        if (db.hasString()) {
            String personName = db.getString();
            PersonListCell droppedPerson = redigerProjektPersonList.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            if (droppedPerson != null) {
                PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                redigerProjektdeltagereListview.getItems().add(pcell);
                redigerProjektdeltagereListview.refresh();
                redigerProjektPersonList.remove(droppedPerson);
                UserOrLocationListview.setItems(redigerProjektPersonList);
            }
            dragEvent.setDropCompleted(true);
        } else {
            dragEvent.setDropCompleted(false);
        }
        dragEvent.consume();
    }




    public void redigerProjektejereListviewDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        if (db.hasString()) {
            String personName = db.getString();
            PersonListCell droppedPerson = redigerProjektPersonList.stream()
                    .filter(person -> person.getName().equals(personName))
                    .findFirst()
                    .orElse(null);
            if (droppedPerson != null) {
                PersonListCell pcell = new PersonListCell(droppedPerson.getPerson());
                redigerProjektejereListview.getItems().add(pcell);
                redigerProjektejereListview.refresh();
                redigerProjektPersonList.remove(droppedPerson);
                UserOrLocationListview.setItems(redigerProjektPersonList);
            }
            dragEvent.setDropCompleted(true);
        } else {
            dragEvent.setDropCompleted(false);
        }
        dragEvent.consume();
    }


    public void redigerLokationListviewDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        if (db.hasString()) {
            String lokationName = db.getString();
            LokationListCell droppedLokation = redigerProjektLokationList.stream()
                    .filter(lokation -> lokation.getName().equals(lokationName))
                    .findFirst()
                    .orElse(null);
            if (droppedLokation != null) {
                LokationListCell lCell = new LokationListCell(droppedLokation.getLokation());
                redigerLokationerListview.getItems().add(lCell);
                redigerLokationerListview.refresh();
                redigerProjektLokationList.remove(droppedLokation);
                UserOrLocationListview.setItems(redigerProjektLokationList);
            }
            dragEvent.setDropCompleted(true);
        } else {
            dragEvent.setDropCompleted(false);
        }
        dragEvent.consume();
    }

    public void redigerLokationListviewDragOver(DragEvent dragEvent) {
        // Accept the drag operation if the dragboard has a String
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public void redigerProjektejereListviewDragOver(DragEvent dragEvent) {
        // Accept the drag operation if the dragboard has a String
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public void redigerProjektdeltagereListviewDragOver(DragEvent dragEvent) {
        // Accept the drag operation if the dragboard has a String
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    public void colorValidation(){
        ProjektColorpicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getRed() > 0.9 && newValue.getGreen() > 0.9 && newValue.getBlue() > 0.9){
                ProjektColorpicker.setValue(oldValue);
                illegalColor();
            }
            if (Math.abs(newValue.getRed() - newValue.getGreen()) < 0.01 && Math.abs(newValue.getGreen() - newValue.getBlue()) < 0.01){
                ProjektColorpicker.setValue(oldValue);
                illegalColor();
            }
        });
    }

    public void illegalColor(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl i valg af farve");
        alert.setHeaderText("Igangværende projekter må ikke være hvide eller grå");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
        alert.showAndWait();
    }


    public void populateFilterChooser(){
        ObservableList<String> filteringsMuligheder = FXCollections.observableArrayList();
        filteringsMuligheder.add("Alle");
        filteringsMuligheder.add("Mine projekter");
        filteringsMuligheder.add("Deltager i");
        filteringsMuligheder.add("Min virksomhed");

        filterChooser.setItems(filteringsMuligheder);
        filterChooser.setValue("Alle");
        projectFilterChoices();
    }

    public void projectFilterChoices(){
        filterChooser.valueProperty().addListener((observable, oldValue, newValue) -> {
            String filterValue = (String) newValue;
            String currentUserName = logic.getCurrentUser().getName();
            Lokation currentUserLokation = logic.getCurrentUser().getLokation();

            for (Node node : CalendarGridPane.getChildren()) {
                if (node instanceof StackPane) {
                    StackPane stackPane = (StackPane) node;
                    if (!stackPane.getChildren().isEmpty() && stackPane.getChildren().get(0) instanceof
                            ProjektCell) {
                        ProjektCell cell = (ProjektCell) stackPane.getChildren().get(0);
                        Projekt projekt = cell.getProjekt();
                        boolean matchFilter = false;

                        switch (filterValue) {
                            case "Alle":
                                matchFilter = true;
                                break;
                            case "Mine projekter":
                                for (Person person : projekt.getProjectCreator()) {
                                    if (person.getName().equals(currentUserName)) {
                                        matchFilter = true;
                                        break;
                                    }
                                }
                                break;
                            case "Deltager i":
                                for (Person person : projekt.getParticipants()) {
                                    if (person.getName().equals(currentUserName)) {
                                        matchFilter = true;
                                        break;
                                    }
                                }
                                break;
                            case "Min virksomhed":
                                Lokation projektLokation = projekt.getLokation();
                                if (projektLokation != null && currentUserLokation != null &&
                                        projektLokation.getName().equals(currentUserLokation.getName())) {
                                    matchFilter = true;
                                }
                                break;
                        }

                        if (matchFilter) {
                            node.setOpacity(1);
                        } else {
                            node.setOpacity(0.1);
                        }
                    }
                }
            }
        });
    }

    public void fieldFilterProjects(String query) {
        String lowerCaseQuery = query.toLowerCase();

        for (Node node : CalendarGridPane.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                if (!stackPane.getChildren().isEmpty() && stackPane.getChildren().get(0) instanceof
                        ProjektCell) {
                    ProjektCell cell = (ProjektCell) stackPane.getChildren().get(0);
                    Projekt projekt = cell.getProjekt();
                    boolean matchFilter = false;

                    //title match
                    if (projekt.getTitel().toLowerCase().contains(lowerCaseQuery)) {
                        matchFilter = true;
                    }

                    // creator match
                    if (!matchFilter) {
                        for (Person person : projekt.getProjectCreator()) {
                            if (person.getName().toLowerCase().contains(lowerCaseQuery)) {
                                matchFilter = true;
                                break;
                            }
                        }
                    }

                    // deltager match
                    if (!matchFilter) {
                        for (Person person : projekt.getParticipants()) {
                            if (person.getName().toLowerCase().contains(lowerCaseQuery)) {
                                matchFilter = true;
                                break;
                            }
                        }
                    }

                    // lokation match
                    if (!matchFilter && projekt.getLokation() != null) {
                        if (projekt.getLokation().getName().toLowerCase().contains(lowerCaseQuery)) {
                            matchFilter = true;
                        }
                    }

                    if (matchFilter) {
                        node.setOpacity(1);
                    } else {
                        node.setOpacity(0.1);
                    }
                }
            }
        }
    }

    public void userLokationListViewDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();

    }
}

