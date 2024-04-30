package com.example.cultureconnect.calendar;

import com.example.cultureconnect.Projekt.Projekt;
import javafx.scene.control.TableCell;

import java.util.Calendar;
import java.util.Locale;

public class CalendarCell {

/*
skal modtage et projekt
skal kunne vise projektets titel
skal sættes i tableview på baggrund af projektets start og slutdato
skal vise cellen som en farvet blok
skal kunne klikkes på, for at kunne vise projektets indhold
skal kunne splittes op til flere celler, hvis projektet har en pause imellem start og slut
skal kunne være tom hvis der ikke er et projekt
start/slutdato bestemmer hvilken uge/kolonne cellen skal starte i.
fylder projektet mere end 1 uge, skal den kunne optage flere celler i en række.
 */

    private Projekt projekt;
    public CalendarCell(Projekt p){
        this.projekt = p;
    }
    //TODO setup cell factory


    public void handleProjektInfo(){
        TableCell tc = new TableCell<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(projekt.getStartDate());
    }
    public TableCell getCell(){

        return null;
    }


}
