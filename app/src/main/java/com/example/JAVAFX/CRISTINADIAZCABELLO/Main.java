package com.example.JAVAFX.CRISTINADIAZCABELLO;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primeraEscena) throws Exception { 
        try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorPrincipal.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                primeraEscena.setScene(scene);
                
                primeraEscena.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
