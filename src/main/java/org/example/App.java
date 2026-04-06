package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Remove the ResourceBundle line completely.

        // 2. Load the FXML without passing a ResourceBundle to the constructor.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));

        // 3. Load the scene
        Scene scene = new Scene(fxmlLoader.load(), 700, 450);

        primaryStage.setScene(scene);

        // 4. Set a temporary title or leave it blank.
        // Your Controller's initialize() will set the real localized title from the DB.
        primaryStage.setTitle("Fuel Consumption Calculator");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}