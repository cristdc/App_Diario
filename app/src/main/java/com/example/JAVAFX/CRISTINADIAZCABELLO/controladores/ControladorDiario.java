package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class ControladorDiario implements Initializable {
    @FXML
    private TextField txtDiario;

    private ControladorEstadoAnimo cEstadoAnimo;
    private DiaEstadoAnimoCR diaEstadoAnimoCR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtDiario.setTextFormatter(new javafx.scene.control.TextFormatter<String>(change -> {
            if (change.getControlNewText().length() > 500) {
                return null;
            }
            return change;
        }));

        txtDiario.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (cEstadoAnimo != null) {
                    cEstadoAnimo.actualizarDescripcionTemporal(newValue);
                }
            }
        });
    }

    public void setControladorEnlace(ControladorEstadoAnimo c, String descripcion) {
        cEstadoAnimo = c;
        if (descripcion != null && !descripcion.isEmpty()) {
            txtDiario.setText(descripcion);
        }
    }


    public void setDiaEstadoAnimoCR(DiaEstadoAnimoCR diaEstadoAnimoCR) {
        this.diaEstadoAnimoCR = diaEstadoAnimoCR;
        if (diaEstadoAnimoCR != null) {
            txtDiario.setText(diaEstadoAnimoCR.getDescripcion());
        }
    }


    public String getTexto() {
        return txtDiario.getText();
    }
}
