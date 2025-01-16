package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.ConexionSingleton;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControladorInforme implements Initializable {

    private ControladorEstadoAnimo controladorEstadoAnimo;
    private Connection conexion;
    Map parametros = new HashMap();
    @FXML
    private WebView wv;
    @FXML
    private Button btnInformeBasicoImg, btnInformeGrafica, btnInformeCompuesto;
    @FXML
    private TextField miTexto;
    @FXML
    private CheckBox chkIncrustado;
    @FXML
    private ComboBox<String> tipoGrafica;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = ConexionSingleton.getConexion();

        chkIncrustado.selectedProperty().addListener((observable, valorAnt, valorAct) -> {
            miTexto.setDisable(valorAct);
        });
        miTexto.setDisable(false);
        tipoGrafica.setItems(FXCollections.observableArrayList("informeGrafica2.jasper", "informeGrafica.jasper"));
    }

    @FXML
    void abrirInformeBasico(ActionEvent event) {
        if (chkIncrustado.isSelected()) {
            lanzaInforme("/informeBasico.jasper", parametros, 0);
        } else {
            parametros.put("ParameterEMOJI", "%" + miTexto.getText() + "%");
            lanzaInforme("/informeParametros.jasper", parametros, 1);
        }
    }

    @FXML
    void abrirInformeCompuesto(ActionEvent event) {
        if (chkIncrustado.isSelected()) {
            lanzaInforme("/informeCompuesta.jasper", parametros, 0);
        } else {
            parametros.put("ParameterEMOJI", "%" + miTexto.getText() + "%");
            lanzaInforme("/informeCompuesta.jasper", parametros, 1);
        }
    }

    @FXML
    void abrirInformeGrafica(ActionEvent event) {
        if (tipoGrafica.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText("Alerta de Informe");
            alert.setContentText("Introduce un valor para la búsqueda");
            alert.showAndWait();
        } else {
            tipoGrafica.setOnAction(e -> {
                String seleccionado = tipoGrafica.getSelectionModel().getSelectedItem();
                System.out.println("Cambio detectado en ComboBox: " + seleccionado);
            });
            if (chkIncrustado.isSelected()) {
                lanzaInforme("/" + tipoGrafica.getSelectionModel().getSelectedItem(), parametros, 0);
            } else {//2 - Informe NO incrustado usa parámetro(nueva ventana)
                lanzaInforme("/" + tipoGrafica.getSelectionModel().getSelectedItem(), parametros, 1);
            }
        }
    }

    private void lanzaInforme(String rutaInf, Map<String, Object> param, int tipo) {
        try {
            try (InputStream reportStream = getClass().getResourceAsStream(rutaInf)) {
                if (reportStream == null) {
                    throw new IllegalArgumentException("Report file not found: " + rutaInf);
                }
                JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

                JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, this.conexion);

                if (!jasperPrint.getPages().isEmpty()) {
                    String pdfOutputPath = "informeBasico.pdf";
                    JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);

                    String outputHtmlFile = "informeHTML.html";
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, outputHtmlFile);

                    if (tipo == 0) {
                        wv.getEngine().load(new File(outputHtmlFile).toURI().toString());
                    } else {
                        WebView wvnuevo = new WebView();
                        wvnuevo.getEngine().load(new File(outputHtmlFile).toURI().toString());
                        StackPane stackPane = new StackPane(wvnuevo);
                        Scene scene = new Scene(stackPane, 600, 500);
                        Stage stage = new Stage();
                        stage.setTitle("Informe en HTML");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(true);
                        stage.setScene(scene);
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Alerta de Informe");
                    alert.setContentText("La búsqueda " + miTexto.getText() + " no generó páginas");
                    alert.showAndWait();
                }

            } catch (JRException e) {
                System.out.println("Error al generar el informe: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error cargando el informe: " + ex.getMessage());
        }
    }

    public void setControladorEnlace(ControladorEstadoAnimo controladorEstadoAnimo) {
        this.controladorEstadoAnimo = controladorEstadoAnimo;
    }

}

