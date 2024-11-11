package org.hugo.ejercicios.Controller;

import org.hugo.ejercicios.Dao.DaoAnimal;
import org.hugo.ejercicios.Model.Animal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatosAnimalController implements Initializable {
    private Object animal = null;
    private Animal ap;
    @FXML
    private Button btt_Cancelar;

    @FXML
    private Button btt_Guardar;

    @FXML
    private Button btt_Img;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ImageView imgView;

    @FXML
    private Label lbl_Datos;

    @FXML
    private Label lbl_Edad;

    @FXML
    private Label lbl_Especie;

    @FXML
    private Label lbl_Fecha;

    @FXML
    private Label lbl_Nombre;

    @FXML
    private Label lbl_Observaciones;

    @FXML
    private Label lbl_Peso;

    @FXML
    private Label lbl_Raza;

    @FXML
    private Label lbl_Sexo;

    @FXML
    private FlowPane panelBotones;

    @FXML
    private GridPane rootPane;

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

    @FXML
    private TextField txt_Sexo;

    private Blob imagenBlob;
    /**
     * Constructor que inicializa el controlador con un objeto de tipo animal.
     *
     * @param animal El objeto animal que se cargará en el controlador.
     */
    public DatosAnimalController(Object animal) {
        this.animal = animal;
    }

    /**
     * Constructor vacío.
     */
    public DatosAnimalController(){}
    /**
     * Establece el objeto animal en el controlador.
     *
     * @param animal El objeto animal a establecer.
     */
    public void setAnimal(Object animal) {
        this.animal = animal;
    }
    /**
     * Metodo de inicialización para configurar el controlador despues de cargar su contenido.
     *
     * @param url La ubicación utilizada para resolver rutas relativas.
     * @param resourceBundle El recurso que contiene propiedades de localización específicas.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btt_Guardar.setDefaultButton(true);
        btt_Cancelar.setCancelButton(true);

        if (animal == null) {
            System.out.println("Null");
        } else {
            Animal anm = (Animal) animal;
            this.ap = anm;

            txt_Nombre.setText(anm.getNombre());
            txt_Especie.setText(anm.getEspecie());
            txt_Raza.setText(anm.getRaza());
            txt_Sexo.setText(anm.getSexo());
            txt_Edad.setText(String.valueOf(anm.getEdad()));
            txt_Peso.setText(String.valueOf(anm.getPeso()));
            txt_Observaciones.setText(anm.getObservaciones());

            if (anm.getFoto() != null) {
                System.out.println("Has image");
                this.imagenBlob = anm.getFoto();
                InputStream imagen = null;
                try {
                    imagen = anm.getFoto().getBinaryStream();
                    imgView.setImage(new Image(imagen));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
/**
 * Cierra la ventana sin realizar cambios.
 *
 * @param event Evento de acción que activa la operación de cancelación.
 */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txt_Nombre.getScene().getWindow();
        stage.close();
    }
    /**
     * Guarda o modifica el animal basado en los datos del formulario.
     *
     * @param event Evento de acción que activa la operación de guardado.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> lst = new ArrayList<>();
        String error = "";

        // Validación del campo nombre
        if (txt_Nombre.getText().isEmpty()) {
            lst.add("Campo nombre no puede estar vacío");
        }

        // Validación del campo especie
        if (txt_Especie.getText().isEmpty()) {
            lst.add("Campo especie no puede estar vacío");
        }

        // Validación del campo raza
        if (txt_Raza.getText().isEmpty()) {
            lst.add("Campo raza no puede estar vacío");
        }

        // Validación del campo sexo
        if (txt_Sexo.getText().isEmpty()) {
            lst.add("Campo sexo no puede estar vacío");
        }

        // Validación del campo edad
        if (txt_Edad.getText().isEmpty()) {
            lst.add("Campo edad no puede estar vacío");
        } else {
            try {
                int edad = Integer.parseInt(txt_Edad.getText());
            } catch (NumberFormatException e) {
                lst.add("Campo edad tiene que ser numérico");
            }
        }

        // Validación del campo peso
        if (txt_Peso.getText().isEmpty()) {
            lst.add("Campo peso no puede estar vacío");
        } else {
            try {
                double peso = Double.parseDouble(txt_Peso.getText());
            } catch (NumberFormatException e) {
                lst.add("Campo peso tiene que ser numérico");
            }
        }

        // Validación del campo observaciones
        if (txt_Observaciones.getText().isEmpty()) {
            lst.add("Campo observaciones no puede estar vacío");
        }

        // Si hay errores, mostrar alerta
        if (!lst.isEmpty()) {
            alerta(lst);
        } else {
            // Guardar o modificar el animal
            boolean resultado;
            if (this.ap == null) {
                resultado = crearAnimal(); // Implementa esta lógica según tu modelo
            } else {
                resultado = modificarAnimal(); // Implementa esta lógica según tu modelo
            }

            // Cerrar la ventana si la operación fue exitosa
            if (resultado) {
                Stage stage = (Stage) txt_Nombre.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Modifica el animal existente en la base de datos con los datos ingresados en el formulario.
     *
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarAnimal() {
        try {
            Animal animal = new Animal();
            animal.setId(ap.getId());
            animal.setNombre(txt_Nombre.getText());
            animal.setEspecie(txt_Especie.getText());
            animal.setRaza(txt_Raza.getText());
            animal.setSexo(txt_Sexo.getText());

            if (txt_Edad.getText().isEmpty()) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("Campo edad no puede estar vacío");
                alerta(lst);
                return false;
            }
            animal.setEdad(Integer.parseInt(txt_Edad.getText()));

            if (txt_Peso.getText().isEmpty()) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("Campo peso no puede estar vacío");
                alerta(lst);
                return false;
            }
            animal.setPeso(Double.parseDouble(txt_Peso.getText()));

            // Validación y establecimiento de las observaciones
            if (txt_Observaciones.getText().isEmpty()) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("Campo observaciones no puede estar vacío");
                alerta(lst);
                return false;
            }
            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                animal.setFechaPrimeraConsulta(date);
            } else {
                System.out.println("No date selected.");
            }
            animal.setObservaciones(txt_Observaciones.getText());

            // Establecimiento de la imagen
            animal.setFoto(imagenBlob);

            // Lógica para modificar el animal en la base de datos
            if (!DaoAnimal.modificar(ap, animal)) { // `ap` es la instancia original
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se han podido cargar los datos.");
                alerta(lst);
                return false;
            }

            // Confirmación de la actualización
            ArrayList<String> lst = new ArrayList<>();
            lst.add("Animal actualizado correctamente");
            confirmacion(lst);
            return true;

        } catch (NumberFormatException e) {
            alerta(new ArrayList<>(List.of("Errores de formato numérico. Asegúrese de que los campos edad y peso sean numéricos.")));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            alerta(new ArrayList<>(List.of("Se produjo un error al modificar el animal.")));
            return false;
        }
    }
    /**
     * Crea un nuevo animal con los datos ingresados en el formulario y lo guarda en la base de datos.
     *
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean crearAnimal() {
        try {
            Animal animal = new Animal();
            animal.setNombre(txt_Nombre.getText());
            animal.setEspecie(txt_Especie.getText());
            animal.setRaza(txt_Raza.getText());
            animal.setSexo(txt_Sexo.getText());

            // Validación y establecimiento de la edad
            if (txt_Edad.getText().isEmpty()) {
                alerta(new ArrayList<>(List.of("Campo edad no puede estar vacío")));
                return false;
            }
            animal.setEdad(Integer.parseInt(txt_Edad.getText()));

            // Validación y establecimiento del peso
            if (txt_Peso.getText().isEmpty()) {
                alerta(new ArrayList<>(List.of("Campo peso no puede estar vacío")));
                return false;
            }
            animal.setPeso(Double.parseDouble(txt_Peso.getText()));

            // Validación y establecimiento de las observaciones
            if (txt_Observaciones.getText().isEmpty()) {
                alerta(new ArrayList<>(List.of("Campo observaciones no puede estar vacío")));
                return false;
            }
            animal.setObservaciones(txt_Observaciones.getText());
            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                // Convert LocalDate to Date
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                animal.setFechaPrimeraConsulta(date);
            } else {
                // Handle case where no date is selected
                alerta(new ArrayList<>(List.of("Por favor, seleccione una fecha para la primera consulta.")));
            }
            // Establecimiento de la imagen
            animal.setFoto(imagenBlob);

            // Lógica para insertar el animal en la base de datos
            int id_animal = DaoAnimal.insertar(animal);
            if (id_animal == -1) {
                alerta(new ArrayList<>(List.of("No se han podido cargar los datos.")));
                return false;
            } else {
                animal.setId(id_animal);
                confirmacion(new ArrayList<>(List.of("Animal creado correctamente")));
                return true;
            }
        } catch (NumberFormatException e) {
            alerta(new ArrayList<>(List.of("Errores de formato numérico. Asegúrese de que los campos edad y peso sean numéricos.")));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            alerta(new ArrayList<>(List.of("Se produjo un error al crear el animal.")));
            return false;
        }
    }
    /**
     * Muestra una alerta de error con los mensajes especificados.
     *
     * @param textos Lista de mensajes de error para mostrar en la alerta.
     */
    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación con los mensajes especificados.
     *
     * @param textos Lista de mensajes de confirmación para mostrar en la alerta.
     */
    public void confirmacion(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    /**
     * Permite seleccionar una imagen del sistema de archivos para asociarla con el animal.
     *
     * @param actionEvent Evento de acción que activa la selección de imagen.
     */
    @FXML
    public void elegirImg(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Animal");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null); // Cambia null por la ventana principal si es necesario
        if (file != null) {
            try {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoAnimal.convertFileToBlob(file);
                this.imagenBlob = blob;
                imgView.setImage(new Image(imagen));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
