package com.example.cultureconnect.Person;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Projekt.Projekt;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Person {
    private String name;
    private List<Projekt> myProjects;
    private String position;
    private Lokation lokation;//måske liste
    private String arbejdsområde;
    private String email;
    private int tlfNr;
    private Image picture;
    private String CPR;
    private String kode;
    private boolean erAnsvarlig;


    public Person(String name, String email, int tlfNr, Image picture, String CPR) {
        this.name = name;
        this.CPR = CPR;
        this.email = email;
        this.tlfNr = tlfNr;
        this.picture = picture;
        this.erAnsvarlig = false;
    } //konstruktør for person

    public boolean isErAnsvarlig() {
        return erAnsvarlig;
    }

    public void setErAnsvarlig(boolean erAnsvarlig) {
        this.erAnsvarlig = erAnsvarlig;
    }

    public String getName() {
        return name;
    }

    public String getCPR() {
        return CPR;
    }

    public void setCPR(String CPR) {
        this.CPR = CPR;
    }

    public void setName(String name) {
        if(name == null){
            throw new IllegalArgumentException("Personer skal have et navn");
        }
        this.name = name;
    }

    public List<Projekt> getMyProjects() {
        return myProjects;
    }

    public void setMyProjects(List<Projekt> myProjects) {
        this.myProjects = myProjects;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Lokation getLokation() {
        return lokation;
    }

    public void setLokation(Lokation lokation) {
        this.lokation = lokation;
    }

    public String getArbejdsområde() {
        return arbejdsområde;
    }

    public void setArbejdsområde(String arbejdsområde) {
        this.arbejdsområde = arbejdsområde;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTlfNr() {
        return tlfNr;
    }

    public void setTlfNr(int tlfNr) {
        this.tlfNr = tlfNr;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public byte[] getPictureAsByteArray() {
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(picture, null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", s);
            return s.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Can't convert image to byte array");
        }
    }

    //make a to string showing the name only
    @Override
    public String toString() {
        return name;
    }
}
