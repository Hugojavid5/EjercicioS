package org.hugo.ejercicios.AppAnimal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/animales.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        try {
            Image img = new Image(getClass().getResource("/Imagenes/icono.png").toString());
            stage.getIcons().add(img);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
        scene.getStylesheets().add(getClass().getResource("/Estilos/Estilos.css").toExternalForm());
        stage.setTitle("Animales");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}