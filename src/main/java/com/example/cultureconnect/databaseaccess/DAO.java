package com.example.cultureconnect.databaseaccess;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.Projekt.ProjektAktivitet;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAO {
    Connection con;

    public DAO() {
        try{
            con = DriverManager.getConnection("jdbc:sqlserver://10.176.111.34:1433;database=CultureConnectDB;userName=CSe2023t_t_1;password=CSe2023tT1#23;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e) {
            System.err.println("Can't connect to Database: " + e.getErrorCode() + e.getMessage());
        }
        System.out.println("Forbundet til databasen... ");
    }


    //TODO CRUD for Person
    //PS for inserting person into person table, columns: CPR - String, Navn - String, Telefon - int, Email - String, Billede - Byte[]
    //make the method take in a Person object and get the values from the persons getters
    public void createPerson(Person person) {
        //prepare the sql statement
        String sqlPerson = "INSERT INTO Person (CPR, Navn, Telefon, Email, Billede) VALUES (?, ?, ?, ?, ?)";
        String sqlLogin = "INSERT INTO LoginInfo (Person_CPR, Mail, Kode) VALUES (?, ?, ?)";
        String sqlMedarbejder = "INSERT INTO MedarbejderInfo (Lokation_Navn, Person_CPR, Stilling, Ansvarlig) VALUES (?, ?, ?, ?)";
        try {
            //prepare the statement
            PreparedStatement preparedStatementPerson = con.prepareStatement(sqlPerson);
            //set the values
            preparedStatementPerson.setString(1, person.getCPR());
            preparedStatementPerson.setString(2, person.getName());
            preparedStatementPerson.setInt(3, person.getTlfNr());
            preparedStatementPerson.setString(4, person.getEmail());
            preparedStatementPerson.setBytes(5, person.getPictureAsByteArray());
            //execute the statement
            preparedStatementPerson.executeUpdate();

            //prepare statement for inserting into LoginInfo table
            PreparedStatement preparedStatementLogin = con.prepareStatement(sqlLogin);
            //set the values
            preparedStatementLogin.setString(1, person.getCPR());
            preparedStatementLogin.setString(2, person.getEmail());
            preparedStatementLogin.setString(3, person.getKode());
            //execute the statement
            preparedStatementLogin.executeUpdate();

            //prepare statement for inserting into MedarbejderInfo table
            PreparedStatement preparedStatementMedarbejder = con.prepareStatement(sqlMedarbejder);
            //set the values
            preparedStatementMedarbejder.setString(1, person.getLokation().getName());
            preparedStatementMedarbejder.setString(2, person.getCPR());
            preparedStatementMedarbejder.setString(3, person.getPosition());
            preparedStatementMedarbejder.setBoolean(4, person.isErAnsvarlig());

            preparedStatementMedarbejder.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Can't create person: " + e.getErrorCode() + e.getMessage());
        }
    }

    //PreparedStatement for reading alle the persons from the person table, and returning a list of them all
    public List<Person> readAllPersons(List<Lokation> locations){
        List<Person> persons = new ArrayList<>();
        String sql = "SELECT * FROM Person";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String CPR = resultSet.getString("CPR");
                String name = resultSet.getString("Navn");
                int tlf = resultSet.getInt("Telefon");
                String email = resultSet.getString("Email");
                byte[] picture = resultSet.getBytes("Billede");
                Image image = new Image("file:src/main/resources/images/avatar.png");
                if (picture != null) {
                    //convert bytearray to image again
                    image = new Image(new ByteArrayInputStream(picture));
                }
                persons.add(new Person(name, email, tlf, image, CPR));
            }
        } catch (SQLException e) {
            System.err.println("Can't read all persons: " + e.getErrorCode() + e.getMessage());
        }
        for (Person person : persons) {
            String sql1 = "SELECT * FROM MedarbejderInfo WHERE Person_CPR = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql1);
                preparedStatement.setString(1, person.getCPR());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    String lokation = resultSet.getString("Lokation_Navn");
                    String stilling = resultSet.getString("Stilling");
                    boolean ansvarlig = resultSet.getBoolean("Ansvarlig");
                    person.setLokation(locations.stream().filter(l -> l.getName().equals(lokation)).findFirst().orElse(null));
                    person.setPosition(stilling);
                    person.setErAnsvarlig(ansvarlig);
                }
            } catch (SQLException e) {
                System.err.println("Can't read medarbejder info: " + e.getErrorCode() + e.getMessage());
            }
        }
        for (Person person : persons) {
            if (person.isErAnsvarlig()){
                locations.stream().filter(l -> l.getName().equals(person.getLokation().getName())).findFirst().orElse(null).setAnsvarligPerson(person);
            }
        }
        return persons;
    }

    //PreparedStatement for updating a person in the person table based on the CPR
    public void updatePerson(Person person){
        String sql = "UPDATE Person SET Navn = ?, Telefon = ?, Email = ?, Billede = ? WHERE CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getTlfNr());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setBytes(4, person.getPictureAsByteArray());
            preparedStatement.setString(5, person.getCPR());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update person: " + e.getErrorCode() + e.getMessage());
        }
        //update the person in the MedarbejderInfo table, where Lokation_Navn is a foreign key to the Lokation table and Person_CPR is a foreign key to the Person table
        //update the Stilling column and Ansvarlig column
        String sql1 = "MERGE INTO MedarbejderInfo AS target " +
                "USING (SELECT ? AS Lokation_Navn, ? AS Person_CPR, ? AS Stilling, ? AS Ansvarlig) AS source " +
                "ON target.Person_CPR = source.Person_CPR " +
                "WHEN MATCHED THEN " +
                "UPDATE SET target.Stilling = source.Stilling, target.Ansvarlig = source.Ansvarlig " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (Lokation_Navn, Person_CPR, Stilling, Ansvarlig) " +
                "VALUES (source.Lokation_Navn, source.Person_CPR, source.Stilling, source.Ansvarlig);";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql1)) {
            preparedStatement.setString(1, person.getLokation().getName());
            preparedStatement.setString(2, person.getCPR());
            preparedStatement.setString(3, person.getPosition());
            preparedStatement.setBoolean(4, person.isErAnsvarlig());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't insert or update medarbejder info: " + e.getErrorCode() + " " + e.getMessage());
        }

    }

    //PreparedStatement for deleting a person in the person table based on the CPR - should delete in the MedarbejderInfo, LoginInfo, ProjektDeltager, BrugerRettigheder first
    public void deletePerson(Person person){
        String sql1 = "DELETE FROM MedarbejderInfo WHERE Person_CPR = ?";
        String sql2 = "DELETE FROM LoginInfo WHERE Person_CPR = ?";
        String sql3 = "DELETE FROM ProjektDeltager WHERE Person_CPR = ?";
        String sql4 = "DELETE FROM BrugerRettigheder WHERE Person_CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql1);
            preparedStatement.setString(1, person.getCPR());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql2);
            preparedStatement.setString(1, person.getCPR());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql3);
            preparedStatement.setString(1, person.getCPR());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql4);
            preparedStatement.setString(1, person.getCPR());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't delete person: " + e.getErrorCode() + e.getMessage());
        }
    }





    //TODO CRUD for Lokation
    //PreparedStatement for creating a location in the Lokation table, columns: Navn - String, Beskrivelse - String, Farvekode - String
    public void createLokation(Lokation lokation){
        String sql = "INSERT INTO Lokation (Navn, Beskrivelse, Farvekode) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, lokation.getName());
            preparedStatement.setString(2, lokation.getDescription());
            preparedStatement.setString(3, lokation.getFarveKode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create location: " + e.getErrorCode() + e.getMessage());
        }
    }

    //PreparedStatement for reading all the locations from the Lokation table, and returning a list of them all
    public List<Lokation> readAllLokations(){
        List<Lokation> lokations = new ArrayList<>();
        String sql = "SELECT * FROM Lokation";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("Navn");
                String description = resultSet.getString("Beskrivelse");
                String farveKode = resultSet.getString("Farvekode");
                lokations.add(new Lokation(name, description, farveKode));
            }
        } catch (SQLException e) {
            System.err.println("Can't read all locations: " + e.getErrorCode() + e.getMessage());
        }
        return lokations;
    }

    //PreparedStatement for updating a location in the Lokation table based on the name
    public void updateLokation(Lokation lokation, String newName){
        String sql = "UPDATE Lokation SET Navn = ?, Beskrivelse = ?, Farvekode = ? WHERE Navn = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, lokation.getDescription());
            preparedStatement.setString(3, lokation.getFarveKode());
            preparedStatement.setString(4, lokation.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update location: " + e.getErrorCode() + e.getMessage());
        }
    }


    //PreparedStatement for deleting a location in the Lokation table based on the name - should delete from ProjektLokation, MedarbejderInfo, Lokation, Projekt tables first
    public void deleteLokation(Lokation lokation){
        String sql1 = "DELETE FROM ProjektLokation WHERE Lokation_Navn = ?";
        String sql2 = "DELETE FROM MedarbejderInfo WHERE Lokation_Navn = ?";
        String sql3 = "DELETE FROM Lokation WHERE Navn = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql1);
            preparedStatement.setString(1, lokation.getName());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql2);
            preparedStatement.setString(1, lokation.getName());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql3);
            preparedStatement.setString(1, lokation.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't delete location: " + e.getErrorCode() + e.getMessage());
        }
    }

    //TODO CRUD for Projekt
    //PreparedStatement for creating a project in the Projekt table, ProjektID is autoincremented, columns: Navn - String, StartDato - Date, SlutDato - Date
    //another prepared statement to insert the details of the project into the ProjektInfo table. Columns: Projekt_ID - String, Beskrivelse - String, Møder - String, Noter - String, where the Projekt_ID is a foreign key to the Projekt table.


    public void createProjekt(Projekt projekt) {
        String sql = "INSERT INTO Projekt (ProjektID, Navn, StartDato, SlutDato, ArrangementDato) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.setString(2, projekt.getTitel());
            preparedStatement.setDate(3, new Date(projekt.getStartDate().getTime()));
            preparedStatement.setDate(4, new Date(projekt.getEndDate().getTime()));
            preparedStatement.setDate(5, new Date(projekt.getArrangementDato().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create project: " + e.getErrorCode() + e.getMessage());
        }
        String sql1 = "INSERT INTO ProjektInfo (Projekt_ID, Beskrivelse, Noter, ProjektFarve) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql1);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.setString(2, projekt.getDescription());
            preparedStatement.setString(3, projekt.getNotes());
            preparedStatement.setString(4, projekt.getColor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create project info: " + e.getErrorCode() + e.getMessage());
        }
        //TODO: insert the participants into the ProjektDeltagere table where Projekt_ID is a foreign key to the Projekt table and Person_CPR is a foreign key to the Person table
        //Insert into these columns: Projekt_ID - String, Person_CPR - String, Ejer - bit. if person is on the creator list, Ejer is true


        String sql2 = "INSERT INTO ProjektDeltager (Projekt_ID, Person_CPR, Ejer) VALUES (?, ?, ?)";
        List<Person> participants = projekt.getParticipants();
        participants.addAll(projekt.getProjectCreator());
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql2);
            for (Person person : participants) {
                preparedStatement.setString(1, projekt.getId());
                preparedStatement.setString(2, person.getCPR());
                preparedStatement.setBoolean(3, projekt.getProjectCreator().contains(person));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Can't create project participants: " + e.getErrorCode() + e.getMessage());
        }
        //Insert the projekt's lokation into the ProjektLokation table, where Projekt_ID is a foreign key to the Projekt table and Lokation_Navn is a foreign key to the Lokation table.
        //Insert into these columns: Projekt_ID - String, Lokation_Navn - String
        String sql3 = "INSERT INTO ProjektLokation (Projekt_ID, Lokation_Navn) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql3);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.setString(2, projekt.getLokation().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create project location: " + e.getErrorCode() + e.getMessage());
        }

        //insert the projekt's aktiviteter into the ProjektAktivitet table, where Projekt_ID is a foreign key to the Projekt table.
        //Insert into these columns: Projekt_ID - String, Aktivitet - String, StartDato - Date, SlutDato - Date
        String sql4 = "INSERT INTO ProjektAktivitet (Projekt_ID, AktivitetNavn, StartDato, SlutDato) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql4);
            for (ProjektAktivitet aktivitet : projekt.getProjektAktiviteter()) {
                preparedStatement.setString(1, projekt.getId());
                preparedStatement.setString(2, aktivitet.getAktivitet());
                preparedStatement.setDate(3, aktivitet.getStartDatoAsSqlDate());
                preparedStatement.setDate(4, aktivitet.getEndDatoAsSqlDate());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Can't create project activities: " + e.getErrorCode() + e.getMessage());
        }
    }

    //PreparedStatement for reading all the projects from the Projekt table, and returning a list of them all
    public List<Projekt> readAllProjects(List<Person> persons, List<Lokation> lokations){
        List<Projekt> projects = new ArrayList<>();
        String sql = "SELECT * FROM Projekt";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                UUID id = UUID.fromString(resultSet.getString("ProjektID"));
                String name = resultSet.getString("Navn");
                Date startDate = resultSet.getDate("StartDato");
                Date endDate = resultSet.getDate("SlutDato");
                Date arrangementDato = resultSet.getDate("ArrangementDato");
                projects.add(new Projekt( name, startDate, arrangementDato, endDate, id));
            }
        } catch (SQLException e) {
            System.err.println("Can't read all projects: " + e.getErrorCode() + e.getMessage());
        }
        //for each project in the list, get the details from the ProjektInfo table and set them on the project object
        for (Projekt projekt : projects) {
            String sql1 = "SELECT * FROM ProjektInfo WHERE Projekt_ID = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql1);
                preparedStatement.setString(1, projekt.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    projekt.setDescription(resultSet.getString("Beskrivelse"));
                    projekt.setNotes(resultSet.getString("Noter"));
                    projekt.setColor(resultSet.getString("ProjektFarve"));
                }
            } catch (SQLException e) {
                System.err.println("Can't read project info: " + e.getErrorCode() + e.getMessage());
            }
            //make a statement to get the participants from the ProjektDeltager table and set them on the project object. if they are the creator, add them to the creator list, and not the participants list.
            String sql2 = "SELECT * FROM ProjektDeltager WHERE Projekt_ID = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setString(1, projekt.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    String CPR = resultSet.getString("Person_CPR");
                    boolean isCreator = resultSet.getBoolean("Ejer");
                    for (Person person : persons) {
                        if (person.getCPR().equals(CPR)){
                            if (isCreator){
                                projekt.getProjectCreator().add(person);
                            } else {
                                projekt.getParticipants().add(person);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Can't read project participants: " + e.getErrorCode() + e.getMessage());
            }
            //make a statement to get the location from the ProjektLokation table and set it on the project object
            String sql3 = "SELECT * FROM ProjektLokation WHERE Projekt_ID = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql3);
                preparedStatement.setString(1, projekt.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    String name = resultSet.getString("Lokation_Navn");
                    for (Lokation lokation : lokations) {
                        if (lokation.getName().equals(name)){
                            projekt.setLokation(lokation);
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Can't read project location: " + e.getErrorCode() + e.getMessage());
            }
            //make a statement to get the activities from the ProjektAktivitet table and set them on the project object
            String sql4 = "SELECT * FROM ProjektAktivitet WHERE Projekt_ID = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql4);
                preparedStatement.setString(1, projekt.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Date startDato = resultSet.getDate("StartDato");
                    Date slutDato = resultSet.getDate("SlutDato");
                    String aktivitet = resultSet.getString("AktivitetNavn");
                    projekt.addAktivitet(new ProjektAktivitet(startDato.toLocalDate(), slutDato.toLocalDate(), aktivitet));
                }
            } catch (SQLException e) {
                System.err.println("Can't read project activities: " + e.getErrorCode() + e.getMessage());
            }
        }
        return projects;
    }

    //PreparedStatement for updating a project in the Projekt table based on the ID
    public void updateProjekt(Projekt projekt){
        String sql = "UPDATE Projekt SET Navn = ?, StartDato = ?, ArrangementDato = ?, SlutDato = ? WHERE ProjektID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, projekt.getTitel());
            preparedStatement.setDate(2, new Date(projekt.getStartDate().getTime()));
            preparedStatement.setDate(3, new Date(projekt.getArrangementDato().getTime()));
            preparedStatement.setDate(4, new Date(projekt.getEndDate().getTime()));
            preparedStatement.setString(5, projekt.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update project: " + e.getErrorCode() + e.getMessage());
        }
        //update the project in the ProjektInfo table, where Projekt_ID is a foreign key to the Projekt table
        //update the columns: Beskrivelse - String, Noter - String, ProjektFarve - String
        String sql1 = "UPDATE ProjektInfo SET Beskrivelse = ?, Noter = ?, ProjektFarve = ? WHERE Projekt_ID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql1);
            preparedStatement.setString(1, projekt.getDescription());
            preparedStatement.setString(2, projekt.getNotes());
            preparedStatement.setString(3, projekt.getColor());
            preparedStatement.setString(4, projekt.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update project info: " + e.getErrorCode() + e.getMessage());
        }
        //update the participants in the ProjektDeltager table, where Projekt_ID is a foreign key to the Projekt table and Person_CPR is a foreign key to the Person table
        //update the columns: Ejer - bit. if person is on the creator list, Ejer is true
        //if the person is not in the list of participants, delete the row
        String sql2 = "MERGE INTO ProjektDeltager AS target " +
                "USING (SELECT ? AS Projekt_ID, ? AS Person_CPR, ? AS Ejer) AS source " +
                "ON target.Person_CPR = source.Person_CPR AND target.Projekt_ID = source.Projekt_ID " +
                "WHEN MATCHED THEN " +
                "UPDATE SET target.Ejer = source.Ejer " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (Projekt_ID, Person_CPR, Ejer) " +
                "VALUES (source.Projekt_ID, source.Person_CPR, source.Ejer);";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql2)) {
            for (Person person : projekt.getParticipants()) {
                preparedStatement.setString(1, projekt.getId());
                preparedStatement.setString(2, person.getCPR());
                preparedStatement.setBoolean(3, false);
                preparedStatement.executeUpdate();
            }
            for (Person person : projekt.getProjectCreator()) {
                preparedStatement.setString(1, projekt.getId());
                preparedStatement.setString(2, person.getCPR());
                preparedStatement.setBoolean(3, true);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Can't insert or update project participants: " + e.getErrorCode() + " " + e.getMessage());
        }
        //update the location in the ProjektLokation table, where Projekt_ID is a foreign key to the Projekt table and Lokation_Navn is a foreign key to the Lokation table
        //update the columns: Lokation_Navn - String
        String sql3 = "UPDATE ProjektLokation SET Lokation_Navn = ? WHERE Projekt_ID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql3);
            preparedStatement.setString(1, projekt.getLokation().getName());
            preparedStatement.setString(2, projekt.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update project location: " + e.getErrorCode() + e.getMessage());
        }
        //update the activities in the ProjektAktivitet table, where Projekt_ID is a foreign key to the Projekt table
        //update the columns: Aktivitet - String, StartDato - Date, SlutDato - Date
        //if the activity is not in the list of activities, delete the row
        String sql4 = "MERGE INTO ProjektAktivitet AS target " +
                "USING (SELECT ? AS Projekt_ID, ? AS AktivitetNavn, ? AS StartDato, ? AS SlutDato) AS source " +
                "ON target.AktivitetNavn = source.AktivitetNavn AND target.Projekt_ID = source.Projekt_ID " +
                "WHEN MATCHED THEN " +
                "UPDATE SET target.StartDato = source.StartDato, target.SlutDato = source.SlutDato " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (Projekt_ID, AktivitetNavn, StartDato, SlutDato) " +
                "VALUES (source.Projekt_ID, source.AktivitetNavn, source.StartDato, source.SlutDato);";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql4)) {
            for (ProjektAktivitet aktivitet : projekt.getProjektAktiviteter()) {
                preparedStatement.setString(1, projekt.getId());
                preparedStatement.setString(2, aktivitet.getAktivitet());
                preparedStatement.setDate(3, aktivitet.getStartDatoAsSqlDate());
                preparedStatement.setDate(4, aktivitet.getEndDatoAsSqlDate());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Can't insert or update project activities: " + e.getErrorCode() + " " + e.getMessage());
        }
    }

    //PreparedStatement for deleting a project in the Projekt table based on the ID
    //make the statement delete from ProjektLokation, ProjektInfo, ProjektDeltager, ProjektAktivitet first, before deleting from the Projekt Table. All keys are the projektID
    public void deleteProjekt(Projekt projekt){
        String sql1 = "DELETE FROM ProjektLokation WHERE Projekt_ID = ?";
        String sql2 = "DELETE FROM ProjektInfo WHERE Projekt_ID = ?";
        String sql3 = "DELETE FROM ProjektDeltager WHERE Projekt_ID = ?";
        String sql4 = "DELETE FROM ProjektAktivitet WHERE Projekt_ID = ?";
        String sql5 = "DELETE FROM Projekt WHERE ProjektID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql1);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql2);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql3);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql4);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.executeUpdate();
            preparedStatement = con.prepareStatement(sql5);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't delete project: " + e.getErrorCode() + e.getMessage());
        }
    }

    public boolean checkLoginReturnUser(String brugernavn, String kodeord) {
        //create preparedstatement to check if the user exists and the password is correct
        //if the user exists, return true, else return false
        //check in the LoginInfo table, columns Email - String, Kode - String
        String sql = "SELECT * FROM LoginInfo WHERE Mail = ? AND Kode = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, brugernavn);
            preparedStatement.setString(2, kodeord);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Can't check login: " + e.getErrorCode() + e.getMessage());
        }
        return false;
    }

    //prepared statement to insert into MedarbejderInfo table
    public void createMedarbejderInfo(String lokation, String cpr, String stilling, boolean ansvarlig) {
        String sql = "INSERT INTO MedarbejderInfo (Lokation_Navn, Person_CPR, Stilling, Ansvarlig) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, lokation);
            preparedStatement.setString(2, cpr);
            preparedStatement.setString(3, stilling);
            preparedStatement.setBoolean(4, ansvarlig);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create medarbejder info: " + e.getErrorCode() + e.getMessage());
        }
    }

    //prepared statement to update ansvarlig in medarbejderInfo table
    public void updateAnsvarlig(String lokation, String cpr, boolean ansvarlig){
        String sql = "UPDATE MedarbejderInfo SET Ansvarlig = ? WHERE Lokation_Navn = ? AND Person_CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setBoolean(1, ansvarlig);
            preparedStatement.setString(2, lokation);
            preparedStatement.setString(3, cpr);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update ansvarlig: " + e.getErrorCode() + e.getMessage());
        }
    }


    //Prepared statement to delete the row where lokation and cpr is in the MedarbejderInfo table
    public void deleteAnsvarlig(String lokation, String cpr){
        String sql = "UPDATE MedarbejderInfo SET Ansvarlig = ? WHERE Lokation_Navn = ? AND Person_CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setString(2, lokation);
            preparedStatement.setString(3, cpr);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update ansvarlig: " + e.getErrorCode() + e.getMessage());
        }
    }

    //Prepared statement to select from MedarbejderInfo column lokation_navn where cpr is
    public String readLokationForPerson(String cpr){
        String sql = "SELECT Lokation_Navn FROM MedarbejderInfo WHERE Person_CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, cpr);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("Lokation_Navn");
            }
        } catch (SQLException e) {
            System.err.println("Can't read lokation for person: " + e.getErrorCode() + e.getMessage());
        }
        return null;
    }

    //Prepared statement to select from MedarbejderInfo column stilling where cpr is
    public String readStillingForPerson(String cpr){
        String sql = "SELECT Stilling FROM MedarbejderInfo WHERE Person_CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, cpr);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("Stilling");
            }
        } catch (SQLException e) {
            System.err.println("Can't read stilling for person: " + e.getErrorCode() + e.getMessage());
        }
        return null;
    }

    //Prepared statement to return a location where the name is
    public Lokation readLokation(String name){
        String sql = "SELECT * FROM Lokation WHERE Navn = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String description = resultSet.getString("Beskrivelse");
                String farveKode = resultSet.getString("Farvekode");
                return new Lokation(name, description, farveKode);
            }
        } catch (SQLException e) {
            System.err.println("Can't read location: " + e.getErrorCode() + e.getMessage());
        }
        return null;
    }

    //Prepared statement to select from logininfo column kode where cpr is
    public String readKodeForPerson(String cpr){
        String sql = "SELECT Kode FROM LoginInfo WHERE Person_CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, cpr);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("Kode");
            }
        } catch (SQLException e) {
            System.err.println("Can't read kode for person: " + e.getErrorCode() + e.getMessage());
        }
        return null;
    }

}

