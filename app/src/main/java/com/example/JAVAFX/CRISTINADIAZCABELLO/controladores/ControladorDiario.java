package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class ControladorDiario implements Initializable {
    @FXML
    private TextField txtDiario;

    private ControladorEstadoAnimo cEstadoAnimo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setControladorEnlace(ControladorEstadoAnimo c) {
        cEstadoAnimo = c;
    }

    public void setDiaEstadoAnimoCR(DiaEstadoAnimoCR diaEstadoAnimoCR) {
        txtDiario.setText(diaEstadoAnimoCR.getDescripcion());
    }

    public String getTexto() {
        return txtDiario.getText();
    }

    public void setTexto(String descripcion) {
        txtDiario.setText(descripcion);
    }
}
