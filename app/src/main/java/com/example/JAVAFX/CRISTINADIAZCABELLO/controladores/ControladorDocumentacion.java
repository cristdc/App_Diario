package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorDocumentacion implements Initializable {

    @FXML
    private WebView webView;
    private ControladorPrincipal cPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();

        String urlDoc = new File("src/main/resources/index.html").toURI().toString();
        webEngine.load(urlDoc);

    }
    public void setControladorEnlace(ControladorPrincipal c) {
        this.cPrincipal = c;
    }
}
