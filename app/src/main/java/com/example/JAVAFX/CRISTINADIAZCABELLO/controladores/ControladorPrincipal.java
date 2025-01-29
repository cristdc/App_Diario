package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import com.example.JAVAFX.CRISTINADIAZCABELLO.Dao.*;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.ConexionSingleton;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;
import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.System.exit;

public class ControladorPrincipal implements Initializable {

    @FXML
    private ImageView imgBuscar, imgLeftArrow, imgRightArrow;
    @FXML
    private Label txtAño, txtMes;
    @FXML
    private GridPane calendarGrid;

    private ControladorBuscarFecha controladorBuscarFecha;
    private ControladorEstadoAnimo controladorEstadoAnimo;

    private YearMonth currentYearMonth;
    public int selectedDay;
    private Connection conexion;

    @FXML
    void buscarUnaFecha(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorBuscarFecha.fxml"));
        Parent root = loader.load();
        controladorBuscarFecha = loader.getController();
        controladorBuscarFecha.setControladorEnlace(this);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("Buscador de fechas");
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));
        stage.show();
    }

    private void actualizarCalendario() {
        txtMes.setText(currentYearMonth.getMonth().toString());
        txtAño.setText(String.valueOf(currentYearMonth.getYear()));

        calendarGrid.getChildren().clear();
        setDiasSemana();

        LocalDate primerDiaDelMes = currentYearMonth.atDay(1);
        int diaDeSemanaDeInicio = primerDiaDelMes.getDayOfWeek().getValue();
        LocalDate fechaCalendario = primerDiaDelMes.minusDays(diaDeSemanaDeInicio - 1);

        List<Integer> seleccionado = obtenerDiasRellenos(primerDiaDelMes);
        for (int semana = 1; semana < 7; semana++) {
            for (int diaDeSemana = 0; diaDeSemana < 7; diaDeSemana++) {
                crearBotonDeDia(fechaCalendario, semana, diaDeSemana, seleccionado.contains(fechaCalendario.getDayOfMonth()));
                fechaCalendario = fechaCalendario.plusDays(1);
            }
        }
    }

    private void setDiasSemana() {
        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (int i = 0; i < diasSemana.length; i++) {
            Label diaLabel = new Label(diasSemana[i]);
            diaLabel.setStyle(
                    "-fx-text-fill: #f2d3ab; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-family: Tahoma; " +
                            "-fx-font: 14px;"
            );
            diaLabel.setMinWidth(50);
            diaLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(diaLabel, i, 0);
        }
    }

    private void crearBotonDeDia(LocalDate fechaCalendario, int semana, int diaDeSemana, boolean seleccionado) {
        if (fechaCalendario.getMonth().equals(currentYearMonth.getMonth())) {
            Button botonDia = new Button(String.valueOf(fechaCalendario.getDayOfMonth()));
            botonDia.setMinSize(50, 50);

            String estiloNormal;

            String estiloHover = "-fx-font: 14px \"Tahoma\"; "
                    + "-fx-background-color:  #0d2b45; "
                    + "-fx-font-weight: bold; "
                    + "-fx-border-color: #f2d3ab; "
                    + "-fx-border-width: 1px;"
                    + "-fx-text-fill: #f2d3ab;";

            if (seleccionado) {
                estiloNormal = "-fx-font: 14px \"Tahoma\"; "
                        + "-fx-background-color: #103851; "
                        + "-fx-font-weight: bold; "
                        + "-fx-border-color: #f2d3ab; "
                        + "-fx-border-width: 1px;"
                        + "-fx-text-fill: #FFB341;";
            } else {
                estiloNormal = "-fx-font: 14px \"Tahoma\"; "
                        + "-fx-background-color: #113A5B; "
                        + "-fx-font-weight: bold; "
                        + "-fx-border-color: #f2d3ab; "
                        + "-fx-border-width: 1px;"
                        + "-fx-text-fill: #f2d3ab;";
            }

            botonDia.setStyle(estiloNormal);

            botonDia.setOnMouseEntered(event -> botonDia.setStyle(estiloHover));
            botonDia.setOnMouseExited(event -> botonDia.setStyle(estiloNormal));

            botonDia.setOnAction(event -> manejarClickBotonDia(fechaCalendario));

            calendarGrid.add(botonDia, diaDeSemana, semana);
        }
    }
    private void abrirVentana(String fxmlPath, String titulo, ControladorPrincipal.VentanaConfiguracion configuracion) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        configuracion.configurar(loader);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(titulo);
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));
        stage.show();
    }


    private void manejarClickBotonDia(LocalDate fechaCalendario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorEstadoAnimo.fxml"));

            Parent root = loader.load();

            controladorEstadoAnimo = loader.getController();

            controladorEstadoAnimo.setControladorEnlace(this);


            controladorEstadoAnimo.setFecha(fechaCalendario);

            selectedDay = fechaCalendario.getDayOfMonth();

            Dia diaSeleccionado = obtenerDiaSeleccionado();
            DiaEstadoAnimoCR diaEstadoAnimoSeleccionado = obtenerDiaEstadoAnimoSeleccionado();
            EstadoDeAnimo estadoAnimoSeleccionado = null;

            if (diaEstadoAnimoSeleccionado != null) {
                estadoAnimoSeleccionado = obtenerEstadoAnimoSeleccionado();
            }
            if (diaEstadoAnimoSeleccionado != null) {
                controladorEstadoAnimo.setDiaEstadoAnimoCR(diaEstadoAnimoSeleccionado);
            }
            if (estadoAnimoSeleccionado != null) {
                controladorEstadoAnimo.setEstadoDeAnimo(estadoAnimoSeleccionado);
            }

            if (diaSeleccionado != null) {
                controladorEstadoAnimo.setDia(diaSeleccionado);
            } else {
                Dia diaVacio = new Dia(java.sql.Date.valueOf(fechaCalendario), 0, "", false, "");
                controladorEstadoAnimo.setDia(diaVacio);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setTitle("Estado Ánimo");
            stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));
            stage.setOnHiding(event -> actualizarCalendario());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    public Dia obtenerDiaSeleccionado() {
        return obtenerDia("SELECT * FROM `Dia` WHERE fecha = ?");
    }

    public DiaEstadoAnimoCR obtenerDiaEstadoAnimoSeleccionado() {
        return obtenerDiaEstadoAnimo("SELECT * FROM `Dia_EstadoAnimo_CR` WHERE fecha = ?");
    }

    public EstadoDeAnimo obtenerEstadoAnimoSeleccionado() {
        return obtenerEstadoAnimo("SELECT e.* from Estado_de_Animo as e inner join Dia_EstadoAnimo_CR as cr ON e.id_estado = cr.id_estado where cr.fecha = ? AND cr.momento_dia = ?");
    }

    private Dia obtenerDia(String consulta) {
        LocalDate fechaSeleccionada = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth().getValue(), selectedDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = fechaSeleccionada.format(formatter);

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, formattedDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(ps);
                    return new Dia(rs.getDate("fecha"), rs.getInt("calidad_sueño"), rs.getString("clima"), rs.getBoolean("siesta"), rs.getString("retos"));
                }
            }
        } catch (SQLException e) {
            mostrarAlertaError("Error al obtener los datos del día", e.getMessage());
        }
        return null;
    }

    private Dia obtenerDia(LocalDate fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = fecha.format(formatter);
        String consulta = "SELECT * FROM Dia WHERE fecha = ?";
        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, formattedDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(ps);
                    return new Dia(rs.getDate("fecha"), rs.getInt("calidad_sueño"), rs.getString("clima"), rs.getBoolean("siesta"), rs.getString("retos"));
                }
            }
        } catch (SQLException e) {
            mostrarAlertaError("Error al obtener los datos del día", e.getMessage());
        }
        return null;
    }

    private ArrayList<Integer> obtenerDiasRellenos(LocalDate fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        String formattedDate = fecha.format(formatter);
        String consulta = "SELECT DATE_FORMAT(fecha,'%e') FROM Dia WHERE date_format(fecha,'%m-%Y') = ? ;";
        ArrayList<Integer> dias = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, formattedDate);
            System.out.println(formattedDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(ps);
                    dias.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            mostrarAlertaError("Error al obtener los datos del día", e.getMessage());
        }
        System.out.println(dias);
        return dias;
    }

    private DiaEstadoAnimoCR obtenerDiaEstadoAnimo(String consulta) {
        LocalDate fechaSeleccionada = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth().getValue(), selectedDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = fechaSeleccionada.format(formatter);

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, formattedDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DiaEstadoAnimoCR(
                            rs.getDate("fecha"),
                            rs.getString("momento_dia"),
                            rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            mostrarAlertaError("Error al obtener el estado de ánimo", e.getMessage());
        }
        return null;
    }

    private EstadoDeAnimo obtenerEstadoAnimo(String consulta) {
        LocalDate fechaSeleccionada = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth().getValue(), selectedDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = fechaSeleccionada.format(formatter);
        String momentoDia = controladorEstadoAnimo.getCmbMomentoDia();

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setString(1, formattedDate);
            ps.setString(2, momentoDia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EstadoDeAnimo(
                            rs.getString("emoji"),
                            rs.getInt("paciencia"),
                            rs.getInt("fuerza_sentimiento"),
                            rs.getInt("grado_productividad")
                    );
                }
            }
        } catch (SQLException e) {
            mostrarAlertaError("Error al obtener el estado de ánimo de la base de datos", e.getMessage());
        }
        return null;
    }

    private void mostrarAlertaError(String encabezado, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        ImageView icono = new ImageView(new Image(getClass().getResource("/img/star.png").toString()));
        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));

        alerta.showAndWait();
    }

    @FXML
    private void pasarMesSiguiente() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        actualizarCalendario();
    }

    @FXML
    private void pasarMesPasado() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        actualizarCalendario();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentYearMonth = YearMonth.now();
        selectedDay = LocalDate.now().getDayOfMonth();

        conexion = ConexionSingleton.getConexion();
        actualizarCalendario();
        animarFlechas();
        configurarAnimacion(imgBuscar);

        Tooltip tooltipBuscar = new Tooltip("Buscar fecha.");
        Tooltip.install(imgBuscar, tooltipBuscar);
        Tooltip tooltipIzquierda = new Tooltip("Mes anterior.");
        Tooltip.install(imgLeftArrow, tooltipIzquierda);
        Tooltip tooltipDerecha = new Tooltip("Mes siguiente.");
        Tooltip.install(imgRightArrow, tooltipDerecha);

        configurarEventosDeTeclado();
    }

    public void irAFecha(LocalDate fecha) {
        currentYearMonth = YearMonth.of(fecha.getYear(), fecha.getMonthValue());
        selectedDay = fecha.getDayOfMonth();
        actualizarCalendario();
    }

    private void animarFlechas() {
        animarFlechaIzquierda();
        animarFlechaDerecha();
    }

    private void animarFlechaIzquierda() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(imgLeftArrow.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(imgLeftArrow.translateXProperty(), -10)),
                new KeyFrame(Duration.seconds(3), new KeyValue(imgLeftArrow.translateXProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    private void animarFlechaDerecha() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(imgRightArrow.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(imgRightArrow.translateXProperty(), 10)),
                new KeyFrame(Duration.seconds(3), new KeyValue(imgRightArrow.translateXProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
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

    private void configurarEventosDeTeclado() {
        calendarGrid.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getRoot().requestFocus(); // Asegura el foco en la escena
                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case RIGHT:
                            pasarMesSiguiente();
                            break;
                        case LEFT:
                            pasarMesPasado();
                            break;
                        case KeyCode.F1:
                            try {
                                abrirVentana("/com/example/JAVAFX/CRISTINADIAZCABELLO/vistas/ControladorDocumentacion.fxml", "Documentación", loader -> {
                                    ControladorDocumentacion controladorBienvenida = loader.getController();
                                    controladorBienvenida.setControladorEnlace(this);
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            break;
                    }
                });
            }
        });
    }

    @FunctionalInterface
    private interface VentanaConfiguracion {
        void configurar(FXMLLoader loader) throws IOException;
    }

}
