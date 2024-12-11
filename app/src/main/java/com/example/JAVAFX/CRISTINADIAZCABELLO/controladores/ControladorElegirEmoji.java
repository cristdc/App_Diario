package com.example.JAVAFX.CRISTINADIAZCABELLO.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControladorElegirEmoji implements Initializable {

    @FXML
    private Button btnAngry, btnHappy, btnHapyy, btnSad, btnSmile;

    private ControladorEstadoAnimo controladorEstadoAnimo;
    private Map<Button, Emoji> botonEmojiMap;
    private String image;

    public String getImage() {
        return image;
    }

    private enum Emoji {
        HAPPY("/img/happy.png"),
        HAPPYY("/img/happyy.png"),
        SAD("/img/sad.png"),
        ANGRY("/img/angry.png"),
        SMILE("/img/smile.png");

        private final String imagePath;

        Emoji(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    public void setControladorEnlace(ControladorEstadoAnimo controlador) {
        this.controladorEstadoAnimo = controlador;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botonEmojiMap = Map.of(
                btnHappy, Emoji.HAPPY,
                btnHapyy, Emoji.HAPPYY,
                btnSad, Emoji.SAD,
                btnAngry, Emoji.ANGRY,
                btnSmile, Emoji.SMILE
        );
    }

    @FXML
    private void elegirEmoji(ActionEvent event) {
        if (controladorEstadoAnimo == null) {
            mostrarAlerta("El controlador de estado de ánimo no está configurado.");
            return;
        }

        Button botonSeleccionado = (Button) event.getSource();
        Emoji emojiSeleccionado = botonEmojiMap.get(botonSeleccionado);
        image = emojiSeleccionado.getImagePath();

        if (emojiSeleccionado != null) {
            controladorEstadoAnimo.actualizarEmoji(emojiSeleccionado.getImagePath());
            mostrarAlerta("Se ha cambiado el emoji. Puedes cerrar sin pérdida de datos.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cambio de Emoji");
        alert.setContentText(mensaje);
        ImageView icono = new ImageView(new Image(getClass().getResource("/img/star.png").toString()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/img/star.png").toString()));

        alert.showAndWait();
    }
}
