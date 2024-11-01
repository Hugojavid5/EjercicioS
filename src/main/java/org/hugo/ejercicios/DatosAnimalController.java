package org.hugo.ejercicios;

import Dao.DaoAnimal;
import Model.Animal;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DatosAnimalController implements Initializable {
    private Animal animal;
    private Blob imagen;

    @FXML
    private Button btt_FotoBorrar;

    @FXML
    private DatePicker fecha;

    @FXML
    private ImageView foto;

    @FXML
    private RadioButton rb_Femenino;

    @FXML
    private RadioButton rb_Masculino;

    @FXML
    private ToggleGroup tg_Sexo;

    @FXML
    private TextField txt_Edad;

    @FXML
    private TextField txt_Especie;

    @FXML
    private TextField txt_Nombre;

    @FXML
    private TextField txt_Observaciones;

    @FXML
    private TextField txt_Peso;

    @FXML
    private TextField txt_Raza;
    /**
     * Constructor que define el animal a editar
     *
     * @param animal a editar
     */
    public DatosAnimalController(Animal animal) {
        this.animal = animal;
    }
    /**
     * Constructor vacío para añadir un nuevo animal
     */
    public DatosAnimalController() {
        this.animal = null;
    }
    /**
     * Función que se ejecuta cuando se carga la ventana
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.imagen = null;
        if (this.animal!=null) {
            txt_Nombre.setText(animal.getNombre());
            txt_Especie.setText(animal.getEspecie());
            txt_Raza.setText(animal.getRaza());
            if (animal.getSexo().equals("Femenino")) {
                rb_Femenino.setSelected(true);
            }
            txt_Edad.setText(animal.getEdad() + "");
            txt_Peso.setText(animal.getPeso() + "");
            txt_Observaciones.setText(animal.getObservaciones());
            fecha.setValue(animal.getFecha_primera_consulta());
            if (animal.getFoto() != null) {
                System.out.println("Has image");
                this.imagen = animal.getFoto();
                try {
                    InputStream imagen = animal.getFoto().getBinaryStream();
                    foto.setImage(new Image(imagen));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                btt_FotoBorrar.setDisable(false);
            }
        }
    }
    @FXML
    void seleccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una foto del animal");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg","*.png"));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                alerta("La imagen no puede ser mayor a 64KB");
            } else {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoAnimal.convertFileToBlob(file);
                this.imagen = blob;
                foto.setImage(new Image(imagen));
                btt_FotoBorrar.setDisable(false);
            }
        } catch (IOException|NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Imagen no seleccionada");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void borrarFoto(ActionEvent event) {
        imagen = null;
        foto.setImage(new Image(getClass().getResourceAsStream("/images/animal.png")));
        btt_FotoBorrar.setDisable(true);
    }
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txt_Nombre.getScene().getWindow();
        stage.close();
    }
    @FXML
    void guardar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Animal nuevo = new Animal();
            nuevo.setNombre(txt_Nombre.getText());
            nuevo.setEspecie(txt_Especie.getText());
            nuevo.setRaza(txt_Raza.getText());
            if (rb_Masculino.isSelected()) {
                nuevo.setSexo("Masculino");
            } else {
                nuevo.setSexo("Femenino");
            }
            nuevo.setEdad(Integer.parseInt(txt_Edad.getText()));
            nuevo.setPeso(Integer.parseInt(txt_Peso.getText()));
            nuevo.setObservaciones(txt_Observaciones.getText());
            nuevo.setFecha_primera_consulta(fecha.getValue());
            nuevo.setFoto(this.imagen);
            if (this.animal == null) {
                int id = DaoAnimal.insertar(nuevo);
                if (id == -1) {
                    alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                } else {
                    confirmacion("Animal añadido correctamente");
                    this.cancelar(null);
                }
            } else {
                if (DaoAnimal.modificar(this.animal, nuevo)) {
                    confirmacion("Animal actualizado correctamente");
                    this.cancelar(null);
                } else {
                    alerta("Ha habido un error almacenando los datos. Por favor vuelva a intentarlo");
                }
            }
        }
    }
    public String validar() {
        String error = "";
        if (txt_Nombre.getText().isEmpty()) {
            error += "El campo nombre no puede estar vacío\n";
        }
        if (txt_Especie.getText().isEmpty()) {
            error += "El campo especie no puede estar vacío\n";
        }
        if (txt_Raza.getText().isEmpty()) {
            error += "El campo raza no puede estar vacío\n";
        }
        if (txt_Edad.getText().isEmpty()) {
            error += "El campo edad no puede estar vacío\n";
        } else {
            try {
                Integer.parseInt(txt_Edad.getText());
            } catch (NumberFormatException e) {
                error += "El campo edad tiene que ser numérico\n";
            }
        }
        if (txt_Peso.getText().isEmpty()) {
            error += "El campo peso no puede estar vacío\n";
        } else {
            try {
                Integer.parseInt(txt_Peso.getText());
            } catch (NumberFormatException e) {
                error += "El campo peso tiene que ser numérico\n";
            }
        }
        if (txt_Observaciones.getText().isEmpty()) {
            error += "El campo observaciones no puede estar vacío\n";
        }
        if (fecha.getValue() == null) {
            error += "El campo fecha de la primera consulta no puede estar vacío\n";
        }
        return error;
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