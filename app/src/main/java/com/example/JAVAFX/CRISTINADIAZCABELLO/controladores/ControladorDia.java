package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorDia implements Initializable {

    @FXML
    private CheckBox chkSiesta;
    @FXML
    private ComboBox<String> cmbTiempo;
    @FXML
    private ImageView imgFlechaAtras, imgFondo, imgMoon, imgPostIt, imgTiempo, imgZZZ, imgSave;
    @FXML
    private Label lbCalidadSueño, lbSiesta;
    @FXML
    private Slider sliderSueño;

    private ControladorEstadoAnimo controladorEstadoAnimo;
    private ControladorAñadirReto controladorAñadirReto;

    private Dia dia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarComboBoxTiempo();
        configurarSliderSueño();
    }
    private void configurarComboBoxTiempo() {
        cmbTiempo.getItems().addAll("Soleado", "Nublado", "Lluvia", "Granizo");
    }
    private void configurarSliderSueño() {
        sliderSueño.setMin(0);
        sliderSueño.setMax(10);
    }

    @FXML
    private void save(MouseEvent event) {
        // Implementar lógica de guardado según corresponda
    }

    @FXML
    private void abrirRetos(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorAñadirReto.fxml"));
        Parent root = loader.load();

        controladorAñadirReto = loader.getController();
        controladorAñadirReto.setControladorEnlace(this);
        controladorAñadirReto.setDia(getDia());

        Stage stage = crearVentanaModal(root, "Controlador Retos");
        stage.show();
    }
    private Stage crearVentanaModal(Parent root, String titulo) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        return stage;
    }

    public void setControladorEnlace(ControladorEstadoAnimo controlador) {
        this.controladorEstadoAnimo = controlador;
    }

    public Dia getDia() {return dia;}
    public void setDia(Dia dia) {
        this.dia = dia;
        sliderSueño.setValue(dia.getCalidadSueno());
        chkSiesta.setSelected(dia.isSiesta());
        cmbTiempo.setValue(dia.getClima());
    }
}
