package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.DiaDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.DiaEstadoAnimoCRDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.EstadoDeAnimoDAOclass;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.ConexionSingleton;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.swing.*;
import java.io.File;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarAnimacion1(imgInforme);
        conexion = ConexionSingleton.getConexion();
    }

    @FXML
    private Button btnInformeBasicoImg;

    @FXML
    private Button btnInformeGrafica;

    @FXML
    private TextField miTexto;

    @FXML
    private CheckBox chkIncrustado;

    @FXML
    private ImageView imgInforme;

    @FXML
    void abrirInformeBasico(ActionEvent event) {
        if (chkIncrustado.isSelected()) {
            lanzaInforme("/report/informeBasico.jasper", parametros, 0);
        } else {
            parametros.put("Parametro", "%" + miTexto.getText() + "%");
            lanzaInforme("/report/informeBasico.jasper", parametros, 1);
        }
    }

    @FXML
    void abrirInformeGrafica(ActionEvent event) {

    }

    private void lanzaInforme(String rutaInf, Map<String, Object> param, int tipo) {
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(rutaInf));
            try {
                System.out.println(this.conexion);
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, this.conexion);

                if (!jasperPrint.getPages().isEmpty()) {

                    String pdfOutputPath = "informe.pdf";
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
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
            }
        } catch (JRException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void setControladorEnlace(ControladorEstadoAnimo controladorEstadoAnimo) {
        this.controladorEstadoAnimo = controladorEstadoAnimo;
    }
    private void configurarAnimacion1(ImageView imageView) {
        imageView.setOnMouseEntered(event -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), imageView);
            scaleUp.setToX(1.03);
            scaleUp.setToY(1.03);
            scaleUp.play();
        });

        imageView.setOnMouseExited(event -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), imageView);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
    }

}

