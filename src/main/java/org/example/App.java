package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        ResourceBundle rb = ResourceBundle.getBundle("messages", Locale.getDefault());

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/main.fxml"), rb
        );

        Scene scene = new Scene(fxmlLoader.load(), 700, 450);


        primaryStage.setScene(scene);
        primaryStage.setTitle(rb.getString("app.title"));

        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }


}
