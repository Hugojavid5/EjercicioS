package org.hugo.ejercicios.AppAnimal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * La clase {@code App} es la entrada principal de la aplicación JavaFX que gestiona
 * la interfaz de usuario para la gestión de animales
 */
public class App extends Application {

    /**
     * Inicializa y muestra la ventana principal de la aplicación.
     *
     * @param stage el escenario principal de la aplicación.
     * @throws IOException si ocurre un error al cargar el archivo FXML.
     */
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

    /**
     * El metodo principal que inicia la aplicación JavaFX.
     *
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}
