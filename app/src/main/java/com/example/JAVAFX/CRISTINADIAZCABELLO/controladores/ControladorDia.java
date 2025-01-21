package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.DiaDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.DiaEstadoAnimoCRDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.EstadoDeAnimoDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.ConexionSingleton;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private ImageView imgMoon, imgPostIt, imgTiempo, imgZZZ, imgSave;
    @FXML
    private Slider sliderSueño;

    private ControladorEstadoAnimo controladorEstadoAnimo;
    private ControladorAñadirReto controladorAñadirReto;

    private Dia dia;

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
        configurarAnimacion(imgSave);
        configurarAnimacion2(imgPostIt);
        configurarAnimacionRotar(imgMoon);
        Tooltip tooltipSave = new Tooltip("Guardar todo.");
        Tooltip.install(imgSave, tooltipSave);
    }

    private void configurarAnimacionRotar(ImageView imageView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), imageView);
        rotateTransition.setByAngle(20);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();
    }

    private void configurarAnimacion(ImageView imageView) {
        imageView.setOnMouseEntered(event -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), imageView);
            scaleUp.setToX(1.2);
            scaleUp.setToY(1.2);
            scaleUp.play();
        });

        imageView.setOnMouseExited(event -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), imageView);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
    }
    private void configurarAnimacion2(ImageView imageView) {
        imageView.setOnMouseEntered(event -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), imageView);
            scaleUp.setToX(1.05);
            scaleUp.setToY(1.05);
            scaleUp.play();
        });

        imageView.setOnMouseExited(event -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), imageView);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
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

        try {
            if (conexion == null) {
                conexion = ConexionSingleton.getConexion();
            }

            dia = diaDAOclass.findByFecha(fecha);
            if (dia == null) {
                dia = new Dia(java.sql.Date.valueOf(fecha), 0, "", false, "");
                diaDAOclass.insert(dia);
            } else {
                dia.setRetos("");
                if(controladorAñadirReto != null) {
                    dia.setRetos(controladorAñadirReto.getRetos());
                }
                dia.setFecha(java.sql.Date.valueOf(fecha));
                dia.setCalidadSueno((int) sliderSueño.getValue());
                dia.setClima(cmbTiempo.getValue());
                dia.setSiesta(chkSiesta.isSelected());
                diaDAOclass.update(dia);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Datos guardados correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage());
        }

    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));

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
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));
        stage.show();
    }

    private Stage crearVentanaModal(Parent root, String titulo) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));
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
