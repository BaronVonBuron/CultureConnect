package com.example.cultureconnect.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CultureConnectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CultureConnectApplication.class.getResource("/com/example/cultureconnect/CultureConnectMainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1200,700);
        stage.setTitle("CultureConnect");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}