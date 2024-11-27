package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControladorAñadirReto implements Initializable {

    @FXML
    private ImageView imgDelete;
    @FXML
    private ImageView imgSave;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField txtReto;

    private ControladorDia cDia;

    @FXML
    void add(MouseEvent event) {

    }

    @FXML
    void delete(MouseEvent event) {

    }

    @FXML
    public void save(MouseEvent event) {

    }

    public void setDia(Dia dia) {
        txtReto.setText(dia.getRetos());
    }

    public void setControladorEnlace(ControladorDia c) {
        cDia = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
