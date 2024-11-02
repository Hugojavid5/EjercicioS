module org.hugo.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql.rowset;


    exports org.hugo.ejercicios.AppAnimal;
    opens org.hugo.ejercicios.AppAnimal to javafx.fxml;
    exports org.hugo.ejercicios.Controller;
    opens org.hugo.ejercicios.Controller to javafx.fxml;
}