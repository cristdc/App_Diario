package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControladorAñadirReto implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea txtTexto;
    private ControladorDia cDia;

    public void setDia(Dia dia) {
        txtTexto.setText(dia.getRetos());
    }

    public void setControladorEnlace(ControladorDia c) {
        cDia = c;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
