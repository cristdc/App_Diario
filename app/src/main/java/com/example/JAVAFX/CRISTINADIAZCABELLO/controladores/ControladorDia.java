package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.DiaDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.DiaEstadoAnimoCRDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.EstadoDeAnimoDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.ConexionSingleton;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
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
    private DiaEstadoAnimoCR diaEstadoAnimoCR;
    private EstadoDeAnimo estadoDeAnimo;

    private DiaDAOclass diaDAOclass;
    private DiaEstadoAnimoCRDAOclass diaEstadoAnimoCRDAOclass;
    private EstadoDeAnimoDAOclass estadoDeAnimoDAOclass;
    private LocalDate fecha;
    private Connection conexion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarComboBoxTiempo();
        configurarSliderSueño();
        conexion = ConexionSingleton.getConexion();
        diaDAOclass = new DiaDAOclass(conexion);
        diaEstadoAnimoCRDAOclass = new DiaEstadoAnimoCRDAOclass(conexion);
        estadoDeAnimoDAOclass = new EstadoDeAnimoDAOclass(conexion);
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
        if (diaEstadoAnimoCR == null || estadoDeAnimo == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Por favor, rellena y guarda el resto de datos antes de rellenar está pantalla.");
        } else {
            try {
                if (conexion == null) {
                    conexion = ConexionSingleton.getConexion();
                }

                dia = diaDAOclass.findByFecha(fecha);
                if (dia == null) {
                    dia = new Dia(java.sql.Date.valueOf(fecha), 0, "", false, "");
                    diaDAOclass.insert(dia);
                } else {
                    dia.setFecha(java.sql.Date.valueOf(fecha));
                    dia.setCalidadSueno((int) sliderSueño.getValue());
                    dia.setClima(cmbTiempo.getValue());
                    dia.setRetos(controladorAñadirReto.getRetos());
                    dia.setSiesta(chkSiesta.isSelected());
                    diaDAOclass.update(dia);
                }

                mostrarAlerta(Alert.AlertType.INFORMATION, "Datos guardados correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
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

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
        sliderSueño.setValue(dia.getCalidadSueno());
        chkSiesta.setSelected(dia.isSiesta());
        cmbTiempo.setValue(dia.getClima());
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

}
