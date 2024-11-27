package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

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

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    public void setControladorEnlace(ControladorPrincipal c) {
        cPrincipal = c;
    }

}
