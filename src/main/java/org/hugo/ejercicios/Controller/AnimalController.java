package org.hugo.ejercicios.Controller;

import javafx.application.Platform;
import org.hugo.ejercicios.BBDD.ConexionBBDD;
import org.hugo.ejercicios.Dao.DaoAnimal;
import org.hugo.ejercicios.Model.Animal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador de la interfaz de usuario para gestionar animales.
 * Permite añadir, editar, borrar y visualizar información sobre animales.
 */
public class AnimalController implements Initializable {

    @FXML
    private MenuItem menu_aniadirAnimal;

    @FXML
    private MenuBar menu_barraMenu;

    @FXML
    private MenuItem menu_borrarAnimal;

    @FXML
    private MenuItem menu_editarAnimal;

    @FXML
    private MenuItem menu_infoAnimal;

    @FXML
    private Label lbl_Listado;

    @FXML
    private Label lbl_Nombre;

    @FXML
    private Menu menuAnimales;

    @FXML
    private Menu menuAyuda;

    @FXML
    private FlowPane panelListado;

    @FXML
    private VBox rootPane;

    @FXML
    private TableView<Animal> tablaVista;

    @FXML
    private TextField txt_Nombre;
    @FXML
    private ResourceBundle resources;

    private ObservableList<Animal> lstEntera = FXCollections.observableArrayList();
    private ObservableList<Animal> lstFiltrada = FXCollections.observableArrayList();

    /**
     * Carga la lista de animales en la tabla vista.
     * Se establece la configuración de las columnas y se llena con datos de la base de datos.
     */
    public void cargarAnimales() {
        try {
            tablaVista.getSelectionModel().clearSelection();
            txt_Nombre.setText(null);
            lstEntera.clear();
            lstFiltrada.clear();
            tablaVista.getItems().clear();
            tablaVista.getColumns().clear();

            // Configuración de columnas de la tabla
            TableColumn<Animal, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getId()));

            TableColumn<Animal, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getNombre()));

            TableColumn<Animal, String> colEspecie = new TableColumn<>("Especie");
            colEspecie.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getEspecie()));

            TableColumn<Animal, String> colRaza = new TableColumn<>("Raza");
            colRaza.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getRaza()));

            TableColumn<Animal, String> colSexo = new TableColumn<>("Sexo");
            colSexo.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getSexo()));

            TableColumn<Animal, Integer> colEdad = new TableColumn<>("Edad");
            colEdad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getEdad()));

            TableColumn<Animal, Double> colPeso = new TableColumn<>("Peso");
            colPeso.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getPeso()));

            TableColumn<Animal, String> colObservaciones = new TableColumn<>("Observaciones");
            colObservaciones.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getObservaciones()));

            tablaVista.getColumns().addAll(colId, colNombre, colEspecie, colRaza, colSexo, colEdad, colPeso, colObservaciones);

            ObservableList<Animal> animales = DaoAnimal.cargarListado();

            if (animales != null && !animales.isEmpty()) {
                lstEntera.setAll(animales);
                tablaVista.setItems(animales);
            } else {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se encontraron Animales.");
                alerta(lst);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

    /**
     * Abre una ventana para añadir un nuevo animal.
     * Carga el controlador y la vista correspondientes, y recarga la lista de animales tras cerrar la ventana.
     *
     * @param event Evento de acción.
     */
    @FXML
    void aniadirAnimal(ActionEvent event) {
        try {
            Window ventana = txt_Nombre.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/datosAnimal.fxml"));
            DatosAnimalController controlador = new DatosAnimalController();
            fxmlLoader.setController(controlador);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/Imagenes/veet.jpg").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
            scene.getStylesheets().add(getClass().getResource("/Estilos/Estilos.css").toExternalForm());
            stage.setTitle("Añade un animal");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarAnimales();

        } catch (IOException e) {
            ArrayList<String> lst = new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }

    /**
     * Elimina el animal seleccionado de la lista.
     * Muestra un diálogo de confirmación antes de realizar la acción.
     *
     * @param event Evento de acción.
     */
    @FXML
    void borrarAnimal(ActionEvent event) {
        Animal animal = tablaVista.getSelectionModel().getSelectedItem();

        if (animal == null) {
            ArrayList<String> lst = new ArrayList<>();
            lst.add("No has seleccionado ningún animal.");
            alerta(lst);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tablaVista.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estás seguro que quieres eliminar este animal? Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DaoAnimal.eliminar(animal)) {
                cargarAnimales();
                confirmacion("Animal eliminado correctamente");
            } else {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se ha podido eliminar el animal.");
                alerta(lst);
            }
        }
    }

    /**
     * Abre una ventana para editar el animal seleccionado.
     *
     * @param event Evento de acción.
     */
    @FXML
    void editarAnimal(ActionEvent event) {
        Animal animal = (Animal) tablaVista.getSelectionModel().getSelectedItem();

        if (animal == null) {
            ArrayList<String> lst = new ArrayList<>();
            lst.add("No has seleccionado ningún animal.");
            alerta(lst);
        } else {
            try {
                Window ventana = tablaVista.getScene().getWindow();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DatosAnimal.fxml"));
                DatosAnimalController controlador = new DatosAnimalController(animal);
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                try {
                    Image img = new Image(getClass().getResource("/Imagenes/icono.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen: " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/Estilos/Estilos.css").toExternalForm());

                stage.setTitle("Edita un animal");
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                cargarAnimales();

            } catch (IOException e) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se ha podido abrir la ventana.");
                alerta(lst);
            }
        }
    }

    /**
     * Muestra información detallada sobre el animal seleccionado.
     *
     * @param event Evento de acción.
     */
    @FXML
    void infoAnimal(ActionEvent event) {
        Object selectedAnimal = tablaVista.getSelectionModel().getSelectedItem();
        if (selectedAnimal == null) {
            ArrayList<String> errores = new ArrayList<>();
            errores.add("Selecciona un animal antes de ver su información");
            alerta(errores);
        } else {

        }
    }

    /**
     * Inicializa el controlador.
     * Configura el evento para el campo de texto y carga la lista de animales.
     *
     * @param url La URL que se utiliza para localizar los elementos de la vista.
     * @param resourceBundle El paquete de recursos que se utiliza para localizar los elementos de la vista.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resources = resourceBundle;
        // Controlar acceso a la base de datos
        try {
            new ConexionBBDD();
        } catch (SQLException | FileNotFoundException e) {
            String mensajeError = resources != null ? resources.getString("No se puede acceder a la base de datos") : "No se puede acceder a la base de datos";
            ArrayList<String> mensajes = new ArrayList<>();
            mensajes.add(mensajeError + ": " + e.getLocalizedMessage());
            alerta(mensajes);
            Platform.exit(); // Cierra la aplicación
            return;
        }

        cargarAnimales();
        txt_Nombre.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                filtrarPorNombre(txt_Nombre.getText());
            }
        });
    }



    /**
     * Filtra la lista de animales por nombre y actualiza la tabla vista.
     *
     * @param nombre El nombre por el que filtrar.
     */
    private void filtrarPorNombre(String nombre) {
        lstFiltrada.clear();
        if (nombre.isEmpty()) {
            tablaVista.setItems(lstEntera);
            return;
        }

        for (Animal animal : lstEntera) {
            if (animal.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                lstFiltrada.add(animal);
            }
        }
        tablaVista.setItems(lstFiltrada);
    }

    /**
     * Muestra una alerta con un mensaje.
     *
     * @param mensajes Lista de mensajes a mostrar en la alerta.
     */
    public void alerta(ArrayList<String> mensajes) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Atención");
        alert.setContentText(String.join("\n", mensajes));
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación con un mensaje.
     *
     * @param mensaje Mensaje a mostrar en la alerta de confirmación.
     */
    public void confirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
