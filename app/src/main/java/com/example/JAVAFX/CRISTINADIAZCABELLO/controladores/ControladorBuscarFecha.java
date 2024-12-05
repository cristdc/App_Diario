package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControladorBuscarFecha implements Initializable{

    @FXML
    private Button btnBuscarFecha;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label lbFecha;
    
    private ControladorPrincipal cPrincipal;

    @FXML
    void buscarFecha(ActionEvent event) {
        if (datePicker.getValue() != null && cPrincipal != null) {
            LocalDate fechaSeleccionada = datePicker.getValue();
            cPrincipal.irAFecha(fechaSeleccionada);
            lbFecha.setText("Fecha seleccionada: " + fechaSeleccionada);

            Stage stage = (Stage) btnBuscarFecha.getScene().getWindow();
            stage.close();
        } else {
            lbFecha.setText("Por favor \nselecciona \nuna fecha.");
            lbFecha.setStyle("-fx-font-size: 12px;");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarAnimacion(btnBuscarFecha);
    }

    private void configurarAnimacion(Button btn) {
        btn.setOnMouseEntered(event -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), btn);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
        });

        btn.setOnMouseExited(event -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), btn);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
    }


    public void setControladorEnlace(ControladorPrincipal c) {
        cPrincipal = c;
    }

}
