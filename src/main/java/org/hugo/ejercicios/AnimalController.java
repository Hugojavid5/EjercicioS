package org.hugo.ejercicios;

import Model.Animal;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

import Dao.DaoAnimal;
import javafx.collections.ObservableList;

public class AnimalController implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Animal> animales = DaoAnimal.cargarListado();
    }
    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }


}
