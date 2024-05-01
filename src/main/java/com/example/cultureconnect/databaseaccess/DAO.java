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
        String sql = "INSERT INTO Person (CPR, Navn, Telefon, Email, Billede) VALUES (?, ?, ?, ?, ?)";
        try {
            //prepare the statement
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            //set the values
            preparedStatement.setString(1, person.getCPR());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getTlfNr());
            preparedStatement.setString(4, person.getEmail());
            preparedStatement.setBytes(5, person.getPictureAsByteArray());
            //execute the statement
            preparedStatement.executeUpdate();
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
                //convert bytearray to image again
                Image image = new Image(new ByteArrayInputStream(picture));
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
}
