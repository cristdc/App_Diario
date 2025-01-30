package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorDocumentacion implements Initializable {

    @FXML
    private WebView webView;
    private ControladorPrincipal cPrincipal;
    private ControladorEstadoAnimo controladorEstadoAnimo;
    private ControladorDia controladorDia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();
        URL urlDoc = getClass().getResource("/img/index.html");

        if (urlDoc != null) {
            webEngine.load(urlDoc.toExternalForm());
        } else {
            System.out.println("No se pudo encontrar el archivo HTML en los recursos.");
        }
    }

    public void setControladorEnlace(ControladorPrincipal c) {
        this.cPrincipal = c;
    }

    public void setControladorEnlace2(ControladorEstadoAnimo controladorEstadoAnimo) {
        this.controladorEstadoAnimo = controladorEstadoAnimo;
    }

    public void setControladorEnlace3(ControladorDia controladorDia) {
        this.controladorDia = controladorDia;
    }
}
