package com.example.cultureconnect.databaseaccess;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
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
    public void createPerson(Person person){
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
            preparedStatementMedarbejder.setString(1, String.valueOf(person.getLokation()));
            preparedStatementMedarbejder.setString(2, person.getCPR());
            preparedStatementMedarbejder.setString(3, person.getPosition());
            preparedStatementMedarbejder.setBoolean(4, false);


        } catch (SQLException e) {
            System.err.println("Can't create person: " + e.getErrorCode() + e.getMessage());
        }
    }

    //PreparedStatement for reading alle the persons from the person table, and returning a list of them all
    public List<Person> readAllPersons(){
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
                Image image = new Image("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
                if (picture != null) {
                    //convert bytearray to image again
                    image = new Image(new ByteArrayInputStream(picture));
                }
                persons.add(new Person(name, email, tlf, image, CPR));
            }
        } catch (SQLException e) {
            System.err.println("Can't read all persons: " + e.getErrorCode() + e.getMessage());
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
    }

    //PreparedStatement for deleting a person in the person table based on the CPR
    public void deletePerson(Person person){
        String sql = "DELETE FROM Person WHERE CPR = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
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


    //PreparedStatement for deleting a location in the Lokation table based on the name
    public void deleteLokation(Lokation lokation){
        String sql = "DELETE FROM Lokation WHERE Navn = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
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
        String sql = "INSERT INTO Projekt (ProjektID, Navn, StartDato, SlutDato) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.setString(2, projekt.getTitel());
            preparedStatement.setDate(3, new Date(projekt.getStartDate().getTime()));
            preparedStatement.setDate(4, new Date(projekt.getEndDate().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create project: " + e.getErrorCode() + e.getMessage());
        }
        String sql1 = "INSERT INTO ProjektInfo (Projekt_ID, Beskrivelse, Møder, Noter) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql1);
            preparedStatement.setString(1, projekt.getId());
            preparedStatement.setString(2, projekt.getDescription());
            preparedStatement.setString(3, projekt.getAktiviteter());
            preparedStatement.setString(4, projekt.getNotes());
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

    }

    //PreparedStatement for reading all the projects from the Projekt table, and returning a list of them all
    public List<Projekt> readAllProjects(){
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
                projects.add(new Projekt( name, startDate, endDate, id));
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
                    projekt.setAktiviteter(resultSet.getString("Møder"));
                    projekt.setNotes(resultSet.getString("Noter"));
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
                    for (Person person : readAllPersons()) {
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
        }
        return projects;
    }

    //PreparedStatement for updating a project in the Projekt table based on the ID
    public void updateProject(Projekt projekt){
        String sql = "UPDATE Projekt SET Navn = ?, StartDato = ?, SlutDato = ? WHERE ProjektID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, projekt.getTitel());
            preparedStatement.setDate(2, new Date(projekt.getStartDate().getTime()));
            preparedStatement.setDate(3, new Date(projekt.getEndDate().getTime()));
            preparedStatement.setString(4, projekt.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update project: " + e.getErrorCode() + e.getMessage());
        }
    }

    //PreparedStatement for deleting a project in the Projekt table based on the ID
    public void deleteProject(Projekt projekt){
        String sql = "DELETE FROM Projekt WHERE ProjektID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
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
}
