module org.hugo.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    exports org.hugo.ejercicios.Model;
    exports org.hugo.ejercicios.Dao;
    opens org.hugo.ejercicios to javafx.fxml;
    exports org.hugo.ejercicios;
}