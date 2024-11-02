module org.hugo.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.hugo.ejercicios to javafx.fxml;
    opens org.hugo.ejercicios.Controller to javafx.fxml;
    exports org.hugo.ejercicios.AppAnimal;
    opens org.hugo.ejercicios.AppAnimal to javafx.fxml;
}