package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.*;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.ConexionSingleton;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

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

public class ControladorEstadoAnimo implements Initializable {

    @FXML
    private ImageView imgDescribeTuDia, imgDescripcionDia, imgEmoji, imgSave;
    @FXML
    private Spinner<Integer> spnFuerzaSentimiento, spnGradoProductividad, spnPaciencia;
    @FXML
    private Label txtDiaMes;
    @FXML
    private ComboBox<String> cmbMomentoDia;

    private ControladorDiario cDiario;
    private ControladorDia cDia;
    private ControladorElegirEmoji cElegirEmoji;
    private ControladorPrincipal cPrincipal;

    private Dia dia;
    private DiaEstadoAnimoCR diaEstadoAnimoCR;
    private EstadoDeAnimo estadoDeAnimo;

    private DiaDAOclass diaDAOclass;
    private DiaEstadoAnimoCRDAOclass diaEstadoAnimoCRDAOclass;
    private EstadoDeAnimoDAOclass estadoDeAnimoDAOclass;
    private LocalDate fecha;
    private Connection conexion;

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarCombobox();
        inicializarSpinners();
        conexion = ConexionSingleton.getConexion();
        diaDAOclass = new DiaDAOclass(conexion);
        diaEstadoAnimoCRDAOclass = new DiaEstadoAnimoCRDAOclass(conexion);
        estadoDeAnimoDAOclass = new EstadoDeAnimoDAOclass(conexion);
    }

    private void inicializarCombobox() {
        cmbMomentoDia.getItems().addAll("Mañana", "Tarde", "Noche");
        cmbMomentoDia.getSelectionModel().select(0);
    }

    private void inicializarSpinners() {
        spnFuerzaSentimiento.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
        spnGradoProductividad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
        spnPaciencia.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
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
            }else{
                dia.setFecha(java.sql.Date.valueOf(fecha));
                diaDAOclass.update(dia);
            }

            int fuerzaSentimiento = spnFuerzaSentimiento.getValue();
            int gradoProductividad = spnGradoProductividad.getValue();
            int paciencia = spnPaciencia.getValue();
            String momentoDia = cmbMomentoDia.getValue();
            String descripcion = cDiario != null ? cDiario.getTexto() : "";
            String emoji = estadoDeAnimo != null ? estadoDeAnimo.getEmoji() : "/img/neutral.png";

            if (estadoDeAnimo == null) {
                estadoDeAnimo = new EstadoDeAnimo(emoji, paciencia, fuerzaSentimiento, gradoProductividad);
                estadoDeAnimoDAOclass.insert(estadoDeAnimo);
            } else {
                estadoDeAnimo.setFuerzaSentimiento(fuerzaSentimiento);
                estadoDeAnimo.setGradoProductividad(gradoProductividad);
                estadoDeAnimo.setPaciencia(paciencia);
                estadoDeAnimo.setEmoji(emoji);
                estadoDeAnimoDAOclass.update(estadoDeAnimo);
            }

            int idEstado = estadoDeAnimoDAOclass.findIdByAttributes(estadoDeAnimo);

            diaEstadoAnimoCR = diaEstadoAnimoCRDAOclass.findByFechaAndMomento(fecha, momentoDia);
            if (diaEstadoAnimoCR == null) {
                diaEstadoAnimoCR = new DiaEstadoAnimoCR(java.sql.Date.valueOf(fecha), momentoDia, descripcion);
                diaEstadoAnimoCR.setIdEstado(idEstado);
                diaEstadoAnimoCRDAOclass.insert(diaEstadoAnimoCR);
            } else {
                diaEstadoAnimoCR.setMomentoDia(momentoDia);
                diaEstadoAnimoCR.setDescripcion(descripcion);
                diaEstadoAnimoCR.setFecha(java.sql.Date.valueOf(fecha));
                diaEstadoAnimoCR.setIdEstado(idEstado);
                diaEstadoAnimoCRDAOclass.update(diaEstadoAnimoCR);
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
        alert.show();
    }


    @FXML
    private void elegirEmoji(MouseEvent event) throws IOException {
        abrirVentana("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorElegirEmoji.fxml", "Controlador Emoji", (loader) -> {
            cElegirEmoji = loader.getController();
            cElegirEmoji.setControladorEnlace(this);
        });
    }
    @FXML
    private void abrirBloc(MouseEvent event) throws IOException {
        if (dia == null) {
            dia = new Dia(java.sql.Date.valueOf(String.valueOf(cPrincipal.selectedDay)), 0, "", false, "");
        }
        if (diaEstadoAnimoCR == null) {
            diaEstadoAnimoCR = new DiaEstadoAnimoCR(java.sql.Date.valueOf(getFecha()), "", "");
        }

        abrirVentana("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorDiario.fxml", "Controlador Diario", (loader) -> {
            cDiario = loader.getController();
            cDiario.setControladorEnlace(this);
            cDiario.setDiaEstadoAnimoCR(diaEstadoAnimoCR);
        });

    }
    @FXML
    private void abrirControladorDia(MouseEvent event) throws IOException {
        if (diaEstadoAnimoCR == null) {
            diaEstadoAnimoCR = new DiaEstadoAnimoCR(java.sql.Date.valueOf(getFecha()), "", "");
        }
        if (estadoDeAnimo == null) {
            estadoDeAnimo = new EstadoDeAnimo("", 1, 1, 1);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorDia.fxml"));
        Parent root = loader.load();

        cDia = loader.getController();
        cDia.setControladorEnlace(this);
        cDia.setDia(dia);
        cDia.setFecha(fecha);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Controlador Dia");
        stage.show();
    }
    private void abrirVentana(String fxmlPath, String titulo, VentanaConfiguracion configuracion) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        configuracion.configurar(loader);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(titulo);
        stage.show();
    }

    public void setControladorEnlace(ControladorPrincipal c) {
        this.cPrincipal = c;
    }
    public void setDia(Dia dia) {
        this.dia = dia;
        txtDiaMes.setText(dia.getFecha().toString());
    }
    public void setDiaEstadoAnimoCR(DiaEstadoAnimoCR diaEstadoAnimoCR) {
        this.diaEstadoAnimoCR = diaEstadoAnimoCR;
    }
    public void setEstadoDeAnimo(EstadoDeAnimo estadoDeAnimo) {
        this.estadoDeAnimo = estadoDeAnimo;
        spnFuerzaSentimiento.getValueFactory().setValue(estadoDeAnimo.getFuerzaSentimiento());
        spnGradoProductividad.getValueFactory().setValue(estadoDeAnimo.getGradoProductividad());
        spnPaciencia.getValueFactory().setValue(estadoDeAnimo.getPaciencia());
        imgEmoji.setImage(new ImageView(estadoDeAnimo.getEmoji()).getImage());
    }
    public void actualizarEmoji(String emoji) {
        imgEmoji.setImage(new ImageView(emoji).getImage());
    }

    public String getCmbMomentoDia() {
        return cmbMomentoDia.getValue();
    }

    @FunctionalInterface
    private interface VentanaConfiguracion {
        void configurar(FXMLLoader loader) throws IOException;
    }
}
