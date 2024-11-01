module org.hugo.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.hugo.ejercicios to javafx.fxml;
    exports org.hugo.ejercicios;
}