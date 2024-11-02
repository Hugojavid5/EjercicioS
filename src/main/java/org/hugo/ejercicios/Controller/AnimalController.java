package org.hugo.ejercicios.Controller;

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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private ObservableList lstEntera = FXCollections.observableArrayList();
    private ObservableList lstFiltrada = FXCollections.observableArrayList();



    public void cargarAnimales() {
        try {
            tablaVista.getSelectionModel().clearSelection();
            txt_Nombre.setText(null);
            lstEntera.clear();
            lstFiltrada.clear();
            tablaVista.getItems().clear();
            tablaVista.getColumns().clear();

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
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }

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
    @FXML
    void infoAnimal(ActionEvent event) {
        Object selectedAnimal = tablaVista.getSelectionModel().getSelectedItem();
        if (selectedAnimal == null) {
            ArrayList<String> errores = new ArrayList<>();
            errores.add("Selecciona un animal antes de ver su información");
            alerta(errores);
        } else {
            ArrayList<String> info = new ArrayList<>();

            if (selectedAnimal instanceof Animal) {
                Animal animal = (Animal) selectedAnimal;
                info.add("Nombre: " + animal.getNombre());
                info.add("Especie: " + animal.getEspecie());
                info.add("Raza: " + animal.getRaza());
                info.add("Sexo: " + animal.getSexo());
                info.add("Edad: " + animal.getEdad());
                info.add("Peso: " + animal.getPeso());
                info.add("Observaciones: " + animal.getObservaciones());
                info.add("Fecha de primera consulta: " + (animal.getFechaPrimeraConsulta() != null ? animal.getFechaPrimeraConsulta().toString() : "No disponible"));
                String contenido = String.join("\n", info);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText(null);
                alerta.setTitle("Información");
                alerta.setContentText(contenido);
                alerta.showAndWait();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaVista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observableValue, Object oldValue, Object newValue) {
                if (newValue != null) {
                    deshabilitarMenus(false);
                } else {
                    deshabilitarMenus(true);
                }
            }
        });
        cargarAnimales();
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Editar Animal");
        editItem.setOnAction(event -> editarAnimal(null));


        MenuItem deleteItem = new MenuItem("Borrar Animal");
        deleteItem.setOnAction(event -> borrarAnimal(null));

        contextMenu.getItems().addAll(editItem, deleteItem);

        tablaVista.setContextMenu(contextMenu);
        tablaVista.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                infoAnimal(null);
            }
        });
        rootPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                txt_Nombre.requestFocus();
                event.consume();
            }
        });
        txt_Nombre.setOnKeyTyped(keyEvent -> filtrar());
    }
    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    public void deshabilitarMenus(boolean deshabilitado) {
        menu_editarAnimal.setDisable(deshabilitado);
        menu_borrarAnimal.setDisable(deshabilitado);
        menu_infoAnimal.setDisable(deshabilitado);
    }
    public void filtrar() {
        String valor = txt_Nombre.getText();
        if (valor==null) {
            tablaVista.setItems(lstEntera);
        } else {
            valor = valor.toLowerCase();
            lstFiltrada.clear();
            for (Object animal : lstEntera) {
                Animal animall = (Animal) animal;
                String nombre = animall.getNombre();
                nombre = nombre.toLowerCase();
                if (nombre.contains(valor)) {
                    lstFiltrada.add(animall);}
            }

            tablaVista.setItems(lstFiltrada);
        }
    }

}