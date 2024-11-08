module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.intissar.examen to javafx.fxml;
    exports com.intissar.examen;
}