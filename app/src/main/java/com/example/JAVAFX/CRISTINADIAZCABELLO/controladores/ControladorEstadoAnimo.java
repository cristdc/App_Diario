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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorEstadoAnimo implements Initializable {

    @FXML
    private ImageView imgDescribeTuDia, imgDescripcionDia, imgEmoji, imgSave, imgDelete;
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
    private int id_estado;
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
    void delete(MouseEvent event) {
        try {
            if (conexion == null) {
                conexion = ConexionSingleton.getConexion();
            }

            diaEstadoAnimoCRDAOclass.delete(fecha, cmbMomentoDia.getValue());
            estadoDeAnimoDAOclass.delete(id_estado);
            diaDAOclass.delete(fecha);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Todos los datos han sido eliminados correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar los datos: " + e.getMessage());
        }
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
                dia.setFecha(java.sql.Date.valueOf(fecha));
                diaDAOclass.update(dia);
            }
            System.out.println("DIA: " + dia.toString());


            int fuerzaSentimiento = spnFuerzaSentimiento.getValue();
            int gradoProductividad = spnGradoProductividad.getValue();
            int paciencia = spnPaciencia.getValue();
            String momentoDia = cmbMomentoDia.getValue();
            String descripcion = cDiario != null ? cDiario.getTexto() : "";
            String emoji = cElegirEmoji != null ? cElegirEmoji.getImage() : "/img/neutral.png";

            estadoDeAnimo = estadoDeAnimoDAOclass.getEstadoDeAnimo(fecha, momentoDia);
            if (estadoDeAnimo == null) {
                System.out.println("entro a insertaaaarrrrr");
                estadoDeAnimo = new EstadoDeAnimo(emoji, paciencia, fuerzaSentimiento, gradoProductividad);
                int id = estadoDeAnimoDAOclass.insert(estadoDeAnimo);
                estadoDeAnimo.setId_estado(id);
                id_estado = estadoDeAnimo.getId_estado();
            } else {
                System.out.println("entro a updateeeaaarrrrr");
                estadoDeAnimo.setFuerzaSentimiento(fuerzaSentimiento);
                estadoDeAnimo.setGradoProductividad(gradoProductividad);
                estadoDeAnimo.setPaciencia(paciencia);
                estadoDeAnimo.setEmoji(emoji);
                estadoDeAnimoDAOclass.update(estadoDeAnimo);
            }
            System.out.println("ESTADO DE ANIMO: " + estadoDeAnimo.toString());


            diaEstadoAnimoCR = diaEstadoAnimoCRDAOclass.findByFechaAndMomento(fecha, momentoDia);
            if (diaEstadoAnimoCR == null) {
                diaEstadoAnimoCR = new DiaEstadoAnimoCR(java.sql.Date.valueOf(fecha), momentoDia, descripcion);
                System.out.println("id_estado: " + estadoDeAnimo.getId_estado());
                diaEstadoAnimoCR.setIdEstado(estadoDeAnimo.getId_estado());
                System.out.println("id_estado: " + diaEstadoAnimoCR.getIdEstado());
                diaEstadoAnimoCRDAOclass.insert(diaEstadoAnimoCR);
                System.out.println("id_estado: " + diaEstadoAnimoCR.getIdEstado());
            } else {
                diaEstadoAnimoCR.setMomentoDia(momentoDia);
                diaEstadoAnimoCR.setDescripcion(descripcion);
                diaEstadoAnimoCR.setFecha(java.sql.Date.valueOf(fecha));
                diaEstadoAnimoCRDAOclass.update(diaEstadoAnimoCR);
            }
            System.out.println("DIA ESTADO ANIMO CR: " + diaEstadoAnimoCR.toString());

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
        if (diaEstadoAnimoCR == null || estadoDeAnimo == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Debes rellenar y guardar el resto de datos antes de continuar.");

        } else {
            diaEstadoAnimoCR = new DiaEstadoAnimoCR(java.sql.Date.valueOf(getFecha()), "", "");
            estadoDeAnimo = new EstadoDeAnimo("", 1, 1, 1);
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
        imgEmoji.setImage(new Image(emoji));
    }

    public String getCmbMomentoDia() {
        return cmbMomentoDia.getValue();
    }

    @FunctionalInterface
    private interface VentanaConfiguracion {
        void configurar(FXMLLoader loader) throws IOException;
    }
}
