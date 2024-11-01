module org.hugo.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.hugo.ejercicios to javafx.fxml;
    exports org.hugo.ejercicios;
}