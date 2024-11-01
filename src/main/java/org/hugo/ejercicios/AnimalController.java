package org.hugo.ejercicios;

import Dao.DaoAnimal;
import Model.Animal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseButton;
import java.time.LocalDate;
import java.util.Optional;


/**
 * Clase que controla los eventos de la ventana
 */
public class AnimalController implements Initializable {
    @FXML // fx:id="btnEditar"
    private MenuItem btnEditar; // Value injected by FXMLLoader
    @FXML // fx:id="btnEliminar"
    private MenuItem btnEliminar; // Value injected by FXMLLoader
    @FXML // fx:id="filtroNombre"
    private TextField filtroNombre; // Value injected by FXMLLoader
    @FXML // fx:id="tabla"
    private TableView<Animal> tabla; // Value injected by FXMLLoader
    private ObservableList<Animal> masterData = FXCollections.observableArrayList();
    private ObservableList<Animal> filteredData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carga inicial
        cargarTabla();
        cargarAnimales();
        // Context Menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editarItem = new MenuItem("Editar");
        MenuItem borrarItem = new MenuItem("Eliminar");
        contextMenu.getItems().addAll(editarItem,borrarItem);
        editarItem.setOnAction(this::editar);
        borrarItem.setOnAction(this::eliminar);
        tabla.setRowFactory(tv -> {
            TableRow<Animal> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    tabla.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
        // Event Listener para celdas de la tabla
        tabla.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            deshabilitarMenus(newValue == null);
        });
        // Event Listener para el filtro
        filtroNombre.setOnKeyTyped(keyEvent -> filtrar());
        // Doble-click para editar
        tabla.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                editar(null);
            }
        });
    }

    /**
     * Función que carga los animales a la lista
     */
    public void cargarAnimales() {
        filtroNombre.setText(null);
        tabla.getItems().clear();
        masterData.clear();
        filteredData.clear();
        ObservableList<Animal> animales = DaoAnimal.cargarListado();
        masterData.addAll(animales);
        tabla.setItems(animales);
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Editar". Abre una ventana para editar el animal seleccionado
     *
     * @param event
     */
    @FXML
    void editar(ActionEvent event) {
        Animal animal = tabla.getSelectionModel().getSelectedItem();
        if (animal == null) {
            alerta("Tienes que seleccionar un animal de la lista");
        } else {
            try {
                Window ventana = tabla.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DatosAnimal.fxml"));
                DatosAnimalController controlador = new DatosAnimalController(animal);
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icono.png")));
                stage.setTitle("Edita un animal o varios animales");
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarAnimales();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta("Error abriendo ventana, por favor inténtelo de nuevo");
            }
        }
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Eliminar". Elimina un animal
     *
     * @param event
     */
    @FXML
    void eliminar(ActionEvent event) {
        Animal animal = tabla.getSelectionModel().getSelectedItem();
        if (animal == null) {
            alerta("Tienes que seleccionar un animal de la lista");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(tabla.getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Confirmación");
            alert.setContentText("¿Estás seguro que quieres eliminar ese animal?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                if (DaoAnimal.eliminar(animal)) {
                    confirmacion("Animal eliminado correctamente");
                    cargarAnimales();
                } else {
                    alerta("No se ha podido eliminar ese animal, por favor inténtelo de nuevo");
                }
            }
        }
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Nuevo". Abre una ventana para añadir objetos de la tabla seleccionada
     *
     * @param event
     */
    @FXML
    void nuevo(ActionEvent event) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DatosAnimal.fxml"));
            DatosAnimalController controlador = new DatosAnimalController();
            fxmlLoader.setController(controlador);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icono.png")));
            stage.setTitle("Añade un animal o varios animales");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarAnimales();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            alerta("Error abriendo ventana, por favor inténtelo de nuevo");
        }
    }
    public void filtrar() {
        String valor = filtroNombre.getText();
        if (valor != null) {
            valor = valor.toLowerCase();
            if (valor.isEmpty()) {
                tabla.setItems(masterData);
            } else {
                filteredData.clear();
                for (Animal animal : masterData) {
                    String nombre = animal.getNombre();
                    nombre = nombre.toLowerCase();
                    if (nombre.contains(valor)) {
                        filteredData.add(animal);
                    }
                }
                tabla.setItems(filteredData);
            }
        }
    }
    /**
     * Función para habilitar o deshabilitar los botones de edición del menu
     *
     * @param deshabilitado true/false
     */
    public void deshabilitarMenus(boolean deshabilitado) {
        btnEditar.setDisable(deshabilitado);
        btnEliminar.setDisable(deshabilitado);
    }

    /**
     * Función que carga las columnas de la tabla
     */
    public void cargarTabla() {
        TableColumn<Animal, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn<Animal, String> colNombre = new TableColumn<>("NOMBRE");
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        TableColumn<Animal, String> colEspecie = new TableColumn<>("ESPECIE");
        colEspecie.setCellValueFactory(new PropertyValueFactory("especie"));
        TableColumn<Animal, String> colRaza = new TableColumn<>("RAZA");
        colRaza.setCellValueFactory(new PropertyValueFactory("raza"));
        TableColumn<Animal, String> colSexo = new TableColumn<>("SEXO");
        colSexo.setCellValueFactory(new PropertyValueFactory("sexo"));
        TableColumn<Animal, Integer> colEdad = new TableColumn<>("EDAD");
        colEdad.setCellValueFactory(new PropertyValueFactory("edad"));
        TableColumn<Animal, Integer> colPeso = new TableColumn<>("PESO");
        colPeso.setCellValueFactory(new PropertyValueFactory("peso"));
        TableColumn<Animal, LocalDate> colFecha = new TableColumn<>("PRIMERA CONSULTA");
        colFecha.setCellValueFactory(new PropertyValueFactory("fecha_primera_consulta"));
        tabla.getColumns().addAll(colId,colNombre,colEspecie,colRaza,colSexo,colEdad,colPeso,colFecha);
    }

    /**
     * Función que muestra un mensaje de alerta al usuario
     *
     * @param texto contenido de la alerta
     */
    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    /**
     * Función que muestra un mensaje de confirmación al usuario
     *
     * @param texto contenido del mensaje
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}